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
    }

    @When("^I search for \"([^\"]*)\"$")
    public void i_search_for(String search) {
        WebElement searchBox = Driver.getDriver().findElement(By.xpath("//input[@name='_nkw']"));
        searchBox.clear();
        searchBox.sendKeys(search + Keys.ENTER);
    }

    @Then("^I should see the results for \"([^\"]*)\"$")
    public void i_should_see_the_results_for(String expectedKeyword) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));

        // Scroll down to make sure elements are visible (headless-safe)
        ((JavascriptExecutor) Driver.getDriver())
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // Wait for the results heading to appear
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(@class,'srp-controls__count-heading')]")
                )
        );

        String headingText = heading.getText().toLowerCase();
        Assert.assertTrue(
                "Search results did not contain expected keyword. Found: " + headingText,
                headingText.contains(expectedKeyword.toLowerCase())
        );
    }

    // ADD THIS METHOD TO FIX THE UNDEFINED STEP ERROR
    @Then("I should see the results")
    public void i_should_see_the_results() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));

        // Wait for the results heading to appear
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(@class,'srp-controls__count-heading')]")
                )
        );

        // Simple assertion that results are displayed
        Assert.assertTrue("Search results should be visible", heading.isDisplayed());

        System.out.println("Search results heading: " + heading.getText());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        Driver.closeDriver();
    }
}