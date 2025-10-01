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
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Driver.getDriver().manage().window().maximize();
    }

    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() {
        // Open eBay home page with retry logic
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(30));

        try {
            Driver.getDriver().get("https://www.ebay.com");

            // Wait for page to load completely - check multiple elements
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gh-ac")));
            System.out.println("✓ eBay home page loaded successfully");

        } catch (Exception e) {
            System.out.println("⚠ Page load issue: " + e.getMessage());
            // Try refreshing
            Driver.getDriver().navigate().refresh();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gh-ac")));
        }
    }

    @When("^I search for \"([^\"]*)\"$")
    public void i_search_for(String search) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));

        try {
            // Wait for search box and enter search term
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.id("gh-ac")));
            searchBox.clear();
            searchBox.sendKeys(search);

            System.out.println("✓ Entered search term: " + search);

            // Use both ENTER key and click search button for reliability
            WebElement searchButton = Driver.getDriver().findElement(By.id("gh-btn"));
            searchButton.click();

            System.out.println("✓ Search initiated");

            // Wait for search results page to start loading
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("https://www.ebay.com/")));

        } catch (Exception e) {
            System.out.println("⚠ Search failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("I should see the results")
    public void i_should_see_the_results() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(25));
        boolean resultsFound = false;
        String foundBy = "";

        System.out.println("🔍 Checking for search results...");

        // Wait a moment for results to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 1. First, check current URL and title
        String currentUrl = Driver.getDriver().getCurrentUrl();
        String pageTitle = Driver.getDriver().getTitle().toLowerCase();

        System.out.println("🌐 Current URL: " + currentUrl);
        System.out.println("📄 Page Title: " + pageTitle);

        // URL-based verification
        if (currentUrl.contains("ebay.com/sch/") || currentUrl.contains("_nkw=") || currentUrl.contains("_from=")) {
            resultsFound = true;
            foundBy = "URL pattern";
            System.out.println("✓ Results confirmed by URL pattern");
        }

        // Title-based verification
        if (!resultsFound && (pageTitle.contains("results for") || pageTitle.contains("ebay") && pageTitle.contains("search"))) {
            resultsFound = true;
            foundBy = "Page title";
            System.out.println("✓ Results confirmed by page title");
        }

        // 2. Try multiple element-based selectors
        String[] elementSelectors = {
                "//h1[contains(@class, 'srp-controls__count-heading')]",
                "//span[contains(@class, 'srp-controls__count-heading')]",
                "//h1[contains(text(), 'results for')]",
                "//span[contains(text(), 'results for')]",
                "//div[contains(@class, 'srp-controls')]//h1",
                "//div[contains(@class, 'srp-controls')]//span",
                "//*[contains(text(), 'results') and contains(text(), 'for')]",
                "//h1[@class='srp-controls__count-heading']"
        };

        if (!resultsFound) {
            for (String selector : elementSelectors) {
                try {
                    List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(selector)));
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        resultsFound = true;
                        foundBy = "Element: " + selector;
                        System.out.println("✓ Results found using: " + selector);
                        System.out.println("📊 Results text: " + elements.get(0).getText());
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("✗ Selector failed: " + selector);
                }
            }
        }

        // 3. Check for product listings as final fallback
        if (!resultsFound) {
            try {
                List<WebElement> productItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//li[contains(@class, 's-item')] | //div[contains(@class, 's-item')]")
                ));
                if (productItems.size() > 3) { // Need multiple items to confirm it's results page
                    resultsFound = true;
                    foundBy = "Product listings count: " + productItems.size();
                    System.out.println("✓ Results confirmed by product items: " + productItems.size());
                }
            } catch (Exception e) {
                System.out.println("✗ No product listings found");
            }
        }

        // 4. Check for search results container
        if (!resultsFound) {
            try {
                List<WebElement> resultContainers = Driver.getDriver().findElements(
                        By.xpath("//div[contains(@class, 'srp-river')] | //ul[contains(@class, 'srp-results')]")
                );
                if (!resultContainers.isEmpty()) {
                    resultsFound = true;
                    foundBy = "Results container";
                    System.out.println("✓ Results container found");
                }
            } catch (Exception e) {
                System.out.println("✗ No results container found");
            }
        }

        // Final verification with detailed failure info
        if (resultsFound) {
            System.out.println("✅ SUCCESS: Search results verified via: " + foundBy);
        } else {
            System.out.println("❌ FAILURE: No search results indicators found");
            System.out.println("Debug info - Page source contains:");
            String pageSource = Driver.getDriver().getPageSource();
            System.out.println(" - 'results': " + pageSource.toLowerCase().contains("results"));
            System.out.println(" - 'srp-': " + pageSource.contains("srp-"));
            System.out.println(" - 's-item': " + pageSource.contains("s-item"));
        }

        Assert.assertTrue("Search results should be visible. Verification method: " + foundBy, resultsFound);
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                System.out.println("❌ SCENARIO FAILED: " + scenario.getName());
                try {
                    final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "failure-screenshot");
                    System.out.println("📸 Screenshot taken");
                } catch (Exception e) {
                    System.out.println("⚠ Could not take screenshot: " + e.getMessage());
                }
            } else {
                System.out.println("✅ SCENARIO PASSED: " + scenario.getName());
            }
        } catch (Exception e) {
            System.out.println("⚠ Error in teardown: " + e.getMessage());
        } finally {
            Driver.closeDriver();
        }
    }
}