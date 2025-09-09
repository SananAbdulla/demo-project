package loopcamp;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;


public class StepDefs {

    @Given("^I am on the home page$")
    public void i_am_on_the_home_page() throws Throwable {
        Driver.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Driver.getDriver().manage().window().maximize();
//        Driver.getDriver().get("https://www.etsy.com");
//        Driver.getDriver().get("http://www.amazon.com");
        Driver.getDriver().get("http://www.ebay.com");
    }

    @When("^I search for \"([^\"]*)\"$")
    public void i_search_for(String search) throws Throwable {
//        Driver.getDriver().findElement(By.cssSelector("input[aria-label^='Search']")).sendKeys(search + Keys.ENTER);
//        Driver.getDriver().findElement(By.xpath("//*[@id='twotabsearchtextbox']")).sendKeys(search + Keys.ENTER);
        Driver.getDriver().findElement(By.xpath("//input[@name=\'_nkw\']")).sendKeys(search + Keys.ENTER);

    }

    @Then("^I should see the results$")
    public void i_should_see_the_results() throws Throwable {
        Thread.sleep(2000);
//        Assert.assertTrue(Driver.getDriver().findElement(By.cssSelector("span[class^='a-color-state']")).getText().contains("wooden loop"));
//        Assert.assertTrue(Driver.getDriver().getCurrentUrl().contains("search"));
        Assert.assertTrue(Driver.getDriver().findElement(By.xpath("(//h1[@class='srp-controls__count-heading']//span)[2]")).getText().contains("glass teapot"));
    }

    @Then("^I should see more results$")
    public void i_should_see_more_results() throws Throwable {
        Thread.sleep(2000);
//        Assert.assertTrue(Driver.getDriver().findElement(By.cssSelector("span[class^='a-color-state']")).getText().contains("useless box"));
//        Assert.assertTrue(Driver.getDriver().getCurrentUrl().contains("search"));
        Assert.assertTrue(Driver.getDriver().findElement(By.xpath("(//h1[@class='srp-controls__count-heading']//span)[2]")).getText().contains("useless box"));

    }

    @After
//    public void tearDown(Scenario scenario) {
//        if (scenario.isFailed()) {
//            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
//            scenario.attach(screenshot, "image/png", scenario.getName());
//        }
//        Driver.closeDriver();

    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = Driver.getDriver();
            if (driver != null) {
                if (scenario.isFailed()) {
                    try {
                        final byte[] screenshot = ((TakesScreenshot) driver)
                                .getScreenshotAs(OutputType.BYTES);
                        scenario.attach(screenshot, "image/png", "screenshot");
                    } catch (WebDriverException e) {
                        System.out.println("⚠ Could not take screenshot: " + e.getMessage());
                    }
                }
                Driver.closeDriver();
            }
        } catch (Exception e) {
            System.out.println("⚠ TearDown failed: " + e.getMessage());
        }

    }

}


//    }
//
//}
