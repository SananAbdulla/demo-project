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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.PageLoadStrategy;

import java.net.URL;
import java.time.Duration;

public class Driver {
    private Driver() {
    }

    private static WebDriver driver;
    private static ChromeOptions chromeOptions;
    private static FirefoxOptions firefoxOptions;
    private static DesiredCapabilities desiredCapabilities;
    static String browser;

    public static WebDriver getDriver() {
        if (driver == null) {
            browser = System.getProperty("BROWSER") != null ?
                    System.getProperty("BROWSER") :
                    ConfigurationReader.getProperty("browser");
            System.out.println("Browser: " + browser);

            switch (browser.toLowerCase()) {

                // ===== Remote Chrome =====
                case "remote-chrome":
                    try {
                        String gridAddress = "100.26.201.67";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        desiredCapabilities = new DesiredCapabilities();
                        desiredCapabilities.setBrowserName("chrome");
                        driver = new RemoteWebDriver(url, desiredCapabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // ===== Remote Firefox =====
                case "remote-firefox":
                    try {
                        String gridAddress = "100.26.201.67";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        desiredCapabilities = new DesiredCapabilities();
                        desiredCapabilities.setBrowserName("firefox");
                        driver = new RemoteWebDriver(url, desiredCapabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // ===== Chrome =====
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;

                // ===== Chrome Headless =====
                case "chrome-headless":
                case "chrome-linux":
                    WebDriverManager.chromedriver().setup();
                    chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    driver = new ChromeDriver(chromeOptions);
                    break;

                // ===== Remote Chrome Linux =====
                case "remote-chrome-linux":
                    try {
                        String gridAddress = "3.80.243.50";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        desiredCapabilities = new DesiredCapabilities();
                        chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--window-size=1920,1080");
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        desiredCapabilities.merge(chromeOptions);
                        driver = new RemoteWebDriver(url, desiredCapabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // ===== Firefox =====
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;

                // ===== Firefox Headless =====
                case "firefox-headless":
                case "firefox-linux":
                    WebDriverManager.firefoxdriver().setup();
                    firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--disable-gpu");
                    firefoxOptions.addArguments("--no-sandbox");
                    firefoxOptions.addArguments("--window-size=1920,1080");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                // ===== Remote Firefox Linux =====
                case "remote-firefox-linux":
                    try {
                        String gridAddress = "3.80.243.50";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        desiredCapabilities = new DesiredCapabilities();
                        desiredCapabilities.setBrowserName("firefox");
                        FirefoxOptions remoteFirefoxOptions = new FirefoxOptions();
                        remoteFirefoxOptions.addArguments("--headless");
                        remoteFirefoxOptions.addArguments("--disable-gpu");
                        remoteFirefoxOptions.addArguments("--no-sandbox");
                        remoteFirefoxOptions.addArguments("--window-size=1920,1080");
                        desiredCapabilities.merge(remoteFirefoxOptions);
                        driver = new RemoteWebDriver(url, desiredCapabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // ===== Internet Explorer =====
                case "ie":
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        throw new WebDriverException("IE not supported on Mac");
                    }
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;

                // ===== Edge =====
                case "edge":
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        throw new WebDriverException("Edge not supported on Mac");
                    }
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;

                // ===== Safari =====
                case "safari":
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        throw new WebDriverException("Safari not supported on Windows");
                    }
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driver = new SafariDriver();
                    break;

                default:
                    throw new WebDriverException("Unknown browser type: " + browser);
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