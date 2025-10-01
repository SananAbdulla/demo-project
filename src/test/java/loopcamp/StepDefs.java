package loopcamp;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class StepDefs {

    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() {
        Driver.getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        Driver.getDriver().manage().window().maximize();
        Driver.getDriver().get("https://www.ebay.com");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void i_search_for(String search) {
        Driver.getDriver().findElement(By.xpath("//input[@name='_nkw']")).sendKeys(search + Keys.ENTER);
    }

    @Then("^I should see the results$")
    public void i_should_see_the_results() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));

        WebElement keywordSpan = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[@class='srp-controls__count-heading']//span[@class='BOLD']")
                )
        );

        String actualText = keywordSpan.getText().toLowerCase();
        Assert.assertTrue("Search results did not contain expected keyword. Found: " + actualText,
                actualText.contains("glass teapot"));
    }

    @Then("^I should see more results$")
    public void i_should_see_more_results() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));

        WebElement keywordSpan = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[@class='srp-controls__count-heading']//span[@class='BOLD']")
                )
        );

        String actualText = keywordSpan.getText().toLowerCase();
        Assert.assertTrue("Search results did not contain expected keyword. Found: " + actualText,
                actualText.contains("useless box"));
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
