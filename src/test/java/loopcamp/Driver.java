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

import java.net.URL;

public class Driver {
    static String browser;

    private Driver() {
    }

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            browser = System.getProperty("BROWSER") != null
                    ? System.getProperty("BROWSER")
                    : ConfigurationReader.getProperty("browser");

            System.out.println("Browser: " + browser);

            switch (browser) {

                // ==================== Local Browsers ====================
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;

                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeHeadless = new ChromeOptions();
                    chromeHeadless.addArguments("--headless=new");
                    chromeHeadless.addArguments("--disable-gpu");
                    chromeHeadless.addArguments("--no-sandbox");
                    chromeHeadless.addArguments("--disable-dev-shm-usage");
                    chromeHeadless.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeHeadless);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;

                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxHeadless = new FirefoxOptions();
                    firefoxHeadless.addArguments("--headless");
                    firefoxHeadless.addArguments("--disable-gpu");
                    firefoxHeadless.addArguments("--no-sandbox");
                    driver = new FirefoxDriver(firefoxHeadless);
                    break;

                case "ie":
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        throw new WebDriverException("IE not supported on Mac");
                    }
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;

                case "edge":
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        throw new WebDriverException("Edge not supported on Mac");
                    }
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;

                case "safari":
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        throw new WebDriverException("Safari not supported on Windows");
                    }
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driver = new SafariDriver();
                    break;

                // ==================== Linux / Jenkins ====================
                case "chrome-linux":
                case "remote-chrome-linux":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions linuxChrome = new ChromeOptions();
                    linuxChrome.addArguments("--headless=new");
                    linuxChrome.addArguments("--disable-gpu");
                    linuxChrome.addArguments("--no-sandbox");
                    linuxChrome.addArguments("--disable-dev-shm-usage");
                    linuxChrome.addArguments("--remote-allow-origins=*");

                    if (browser.startsWith("remote")) {
                        try {
                            String gridAddress = "34.224.27.248"; // your grid
                            DesiredCapabilities capabilities = new DesiredCapabilities();
                            capabilities.merge(linuxChrome);
                            driver = new RemoteWebDriver(new URL("http://" + gridAddress + ":4444/wd/hub"), capabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        driver = new ChromeDriver(linuxChrome);
                    }
                    break;

                case "firefox-linux":
                case "remote-firefox-linux":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions linuxFirefox = new FirefoxOptions();
                    linuxFirefox.addArguments("--headless");
                    linuxFirefox.addArguments("--disable-gpu");
                    linuxFirefox.addArguments("--no-sandbox");

                    if (browser.startsWith("remote")) {
                        try {
                            String gridAddress = "34.224.27.248"; // your grid
                            DesiredCapabilities capabilities = new DesiredCapabilities();
                            capabilities.setBrowserName("firefox");
                            capabilities.merge(linuxFirefox);
                            driver = new RemoteWebDriver(new URL("http://" + gridAddress + ":4444/wd/hub"), capabilities);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        driver = new FirefoxDriver(linuxFirefox);
                    }
                    break;

                // ==================== Remote Selenium Grid ====================
                case "remote-chrome":
                    try {
                        String gridAddress = "100.26.201.67";
                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        capabilities.setBrowserName("chrome");
                        driver = new RemoteWebDriver(new URL("http://" + gridAddress + ":4444/wd/hub"), capabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "remote-firefox":
                    try {
                        String gridAddress = "100.26.201.67";
                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        capabilities.setBrowserName("firefox");
                        driver = new RemoteWebDriver(new URL("http://" + gridAddress + ":4444/wd/hub"), capabilities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
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