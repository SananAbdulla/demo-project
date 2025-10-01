package loopcamp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;

public class Driver {

    private Driver() {}

    private static WebDriver driver;
    private static String browser;

    public static WebDriver getDriver() {
        if (driver == null) {
            if (System.getProperty("BROWSER") == null) {
                browser = ConfigurationReader.getProperty("browser");
            } else {
                browser = System.getProperty("BROWSER");
            }

            System.out.println("Browser: " + browser);

            try {
                switch (browser) {
                    case "remote-chrome":
                        driver = new RemoteWebDriver(
                                new URL("http://34.224.27.248:4444/"),
                                new ChromeOptions().setBrowserVersion("127")
                        );
                        break;

                    case "remote-firefox":
                        driver = new RemoteWebDriver(
                                new URL("http://34.224.27.248:4444/"),
                                new FirefoxOptions().setBrowserVersion("127")
                        );
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver();
                        break;

                    case "chrome-headless":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions headlessChrome = new ChromeOptions();
                        headlessChrome.addArguments("--headless=new");
                        driver = new ChromeDriver(headlessChrome);
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                        break;

                    case "firefox-headless":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions headlessFirefox = new FirefoxOptions();
                        headlessFirefox.addArguments("--headless");
                        driver = new FirefoxDriver(headlessFirefox);
                        break;

                    case "ie":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("IE is not supported on Mac");
                        }
                        WebDriverManager.iedriver().setup();
                        driver = new InternetExplorerDriver();
                        break;

                    case "edge":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("Edge is not supported on Mac");
                        }
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                        break;

                    case "safari":
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            throw new WebDriverException("Safari is not supported on Windows");
                        }
                        driver = new SafariDriver();
                        break;

                    case "chrome-linux":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions linuxChrome = new ChromeOptions();
                        linuxChrome.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                        driver = new ChromeDriver(linuxChrome);
                        break;

                    case "remote-chrome-linux":
                        ChromeOptions remoteLinuxChrome = new ChromeOptions();
                        remoteLinuxChrome.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new RemoteWebDriver(
                                new URL("http://34.224.27.248:4444/"),
                                remoteLinuxChrome
                        );
                        break;

                    case "firefox-linux":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions linuxFirefox = new FirefoxOptions();
                        linuxFirefox.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = new FirefoxDriver(linuxFirefox);
                        break;

                    case "remote-firefox-linux":
                        FirefoxOptions remoteLinuxFirefox = new FirefoxOptions();
                        remoteLinuxFirefox.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = new RemoteWebDriver(
                                new URL("http://34.224.27.248:4444/"),
                                remoteLinuxFirefox
                        );
                        break;

                    default:
                        throw new RuntimeException("Unsupported browser: " + browser);
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create WebDriver for browser: " + browser, e);
            }
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
