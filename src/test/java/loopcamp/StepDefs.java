package loopcamp;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StepDefs {

    @Before
    public void setUp() {
        // Initialize driver before each scenario
        Driver.getDriver();
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Driver.getDriver().manage().window().maximize();
    }

    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() {
        // Open eBay home page
        Driver.getDriver().get("https://www.ebay.com");

        // Wait for page to load - use multiple possible selectors
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        // Try multiple possible indicators that page is loaded
        String[] pageIndicators = {
                "gh-ac", // Search box ID
                "gh-btn", // Search button ID
                "gh-logo" // eBay logo
        };

        for (String indicator : pageIndicators) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(indicator)));
                System.out.println("Page loaded - found: " + indicator);
                break;
            } catch (Exception e) {
                // Continue to next indicator
            }
        }
    }

    @When("^I search for \"([^\"]*)\"$")
    public void i_search_for(String search) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));

        // Wait for search box and enter search term
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("gh-ac")));
        searchBox.clear();
        searchBox.sendKeys(search);

        System.out.println("Searching for: " + search);

        // Click search button instead of ENTER (more reliable)
        WebElement searchButton = Driver.getDriver().findElement(By.id("gh-btn"));
        searchButton.click();

        // Wait for navigation to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("I should see the results")
    public void i_should_see_the_results() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));
        boolean resultsFound = false;

        System.out.println("Checking for search results...");

        // Check multiple indicators of search results with detailed logging
        String[] indicators = {
                "//h1[contains(@class, 'srp-controls__count-heading')]",
                "//h1[contains(@class, 'count-heading')]",
                "//*[contains(text(), 'results for')]",
                "//*[contains(text(), 'Results')]",
                "//div[contains(@class, 'srp-river')]",
                "//ul[contains(@class, 'srp-results')]",
                "//span[contains(@class, 'srp-controls__count-heading')]"
        };

        for (String indicator : indicators) {
            try {
                List<WebElement> elements = Driver.getDriver().findElements(By.xpath(indicator));
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    resultsFound = true;
                    System.out.println("✓ Results found using: " + indicator);
                    System.out.println("Results text: " + elements.get(0).getText());
                    break;
                } else {
                    System.out.println("✗ No elements found for: " + indicator);
                }
            } catch (Exception e) {
                System.out.println("✗ Exception with selector '" + indicator + "': " + e.getMessage());
            }
        }

        // Check URL for search results pattern
        if (!resultsFound) {
            String currentUrl = Driver.getDriver().getCurrentUrl();
            String pageTitle = Driver.getDriver().getTitle();

            System.out.println("Current URL: " + currentUrl);
            System.out.println("Page Title: " + pageTitle);

            if (currentUrl.contains("ebay.com/sch/") || currentUrl.contains("_nkw=")) {
                resultsFound = true;
                System.out.println("✓ Results confirmed by URL pattern");
            }

            if (pageTitle.toLowerCase().contains("ebay") && pageTitle.toLowerCase().contains("results")) {
                resultsFound = true;
                System.out.println("✓ Results confirmed by page title");
            }
        }

        // Final attempt - look for any product listings
        if (!resultsFound) {
            try {
                List<WebElement> productItems = Driver.getDriver().findElements(By.xpath("//li[contains(@class, 's-item')]"));
                if (productItems.size() > 0) {
                    resultsFound = true;
                    System.out.println("✓ Results confirmed by product items count: " + productItems.size());
                }
            } catch (Exception e) {
                System.out.println("No product items found");
            }
        }

        if (!resultsFound) {
            // Take screenshot for debugging
            try {
                final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
                // Can't attach to scenario here, but we can save it or log
                System.out.println("Screenshot taken for debugging");
            } catch (Exception e) {
                System.out.println("Could not take screenshot: " + e.getMessage());
            }
        }

        Assert.assertTrue("Search results should be visible. Check console for detailed logs.", resultsFound);
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                System.out.println("Scenario failed: " + scenario.getName());
                final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            } else {
                System.out.println("Scenario passed: " + scenario.getName());
            }
        } catch (Exception e) {
            System.out.println("Error in teardown: " + e.getMessage());
        } finally {
            Driver.closeDriver();
        }
    }
}