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
    private Driver() {}

    private static WebDriver driver;
    private static String browser;

    public static WebDriver getDriver() {
        if (driver == null) {
            // Pick from system property first, fallback to config file
            browser = System.getProperty("BROWSER", ConfigurationReader.getProperty("browser"));
            System.out.println("Browser: " + browser);

            try {
                switch (browser) {
                    case "remote-chrome":
                        driver = initRemoteDriver("54.226.36.175", "chrome");
                        break;

                    case "remote-firefox":
                        driver = initRemoteDriver("54.226.36.175", "firefox");
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver();
                        break;

                    case "chrome-headless":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions chromeHeadless = new ChromeOptions();
                        chromeHeadless.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                                "--disable-gpu", "--window-size=1920,1080", "--remote-allow-origins=*");
                        driver = new ChromeDriver(chromeHeadless);
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                        break;

                    case "firefox-headless":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions firefoxHeadless = new FirefoxOptions();
                        firefoxHeadless.addArguments("--headless", "--no-sandbox", "--disable-gpu");
                        driver = new FirefoxDriver(firefoxHeadless);
                        break;

                    case "ie":
                        if (isMac()) throw new WebDriverException("IE not supported on Mac");
                        WebDriverManager.iedriver().setup();
                        driver = new InternetExplorerDriver();
                        break;

                    case "edge":
                        if (isMac()) throw new WebDriverException("Edge not supported on Mac");
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                        break;

                    case "safari":
                        if (isWindows()) throw new WebDriverException("Safari not supported on Windows");
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driver = new SafariDriver();
                        break;

                    case "chrome-linux":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions chromeLinux = new ChromeOptions();
                        chromeLinux.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                                "--disable-gpu", "--window-size=1920,1080", "--remote-allow-origins=*");
                        driver = new ChromeDriver(chromeLinux);
                        break;

                    case "remote-chrome-linux":
                        ChromeOptions remoteChromeOpts = new ChromeOptions();
                        remoteChromeOpts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                                "--disable-gpu", "--window-size=1920,1080");
                        driver = initRemoteDriverWithOptions("34.227.172.25", remoteChromeOpts);
                        break;

                    case "firefox-linux":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions firefoxLinux = new FirefoxOptions();
                        firefoxLinux.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = new FirefoxDriver(firefoxLinux);
                        break;

                    case "remote-firefox-linux":
                        FirefoxOptions remoteFirefoxOpts = new FirefoxOptions();
                        remoteFirefoxOpts.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = initRemoteDriverWithOptions("34.227.172.25", remoteFirefoxOpts);
                        break;

                    default:
                        System.out.println("⚠ Unknown browser: " + browser + " → Falling back to headless Chrome.");
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions defaultOptions = new ChromeOptions();
                        defaultOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                                "--disable-gpu", "--window-size=1920,1080", "--remote-allow-origins=*");
                        driver = new ChromeDriver(defaultOptions);
                        break;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to initialize driver for browser: " + browser);
                e.printStackTrace();
                // Safe fallback: headless Chrome
                if (driver == null) {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions fallback = new ChromeOptions();
                    fallback.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
                            "--disable-gpu", "--window-size=1920,1080");
                    driver = new ChromeDriver(fallback);
                }
            }
        }
        return driver;
    }

    private static WebDriver initRemoteDriver(String gridAddress, String browserName) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browserName);
        return new RemoteWebDriver(url, caps);
    }

    private static WebDriver initRemoteDriverWithOptions(String gridAddress, ChromeOptions options) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        return new RemoteWebDriver(url, options);
    }

    private static WebDriver initRemoteDriverWithOptions(String gridAddress, FirefoxOptions options) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        return new RemoteWebDriver(url, options);
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
