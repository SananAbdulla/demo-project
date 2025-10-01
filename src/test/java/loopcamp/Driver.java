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
import java.time.Duration;

public class Driver {
    private static String browser;
    private static WebDriver driver;

    private Driver() {
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            // Get browser from system property or configuration file
            if (System.getProperty("BROWSER") == null) {
                browser = ConfigurationReader.getProperty("browser");
            } else {
                browser = System.getProperty("BROWSER");
            }
            System.out.println("🚀 Initializing Browser: " + browser);

            switch (browser) {
                case "remote-chrome":
                    setupRemoteChrome("54.226.36.175", false);
                    break;

                case "remote-firefox":
                    setupRemoteFirefox("54.226.36.175", false);
                    break;

                case "chrome":
                    setupLocalChrome(false);
                    break;

                case "chrome-headless":
                    setupLocalChrome(true);
                    break;

                case "firefox":
                    setupLocalFirefox(false);
                    break;

                case "firefox-headless":
                    setupLocalFirefox(true);
                    break;

                case "ie":
                    setupInternetExplorer();
                    break;

                case "edge":
                    setupEdge();
                    break;

                case "safari":
                    setupSafari();
                    break;

                case "chrome-linux":
                    setupLinuxChrome();
                    break;

                case "remote-chrome-linux":
                    setupRemoteChrome("34.229.219.164", true);
                    break;

                case "firefox-linux":
                    setupLinuxFirefox();
                    break;

                case "remote-firefox-linux":
                    setupRemoteFirefox("34.227.172.25", true);
                    break;

                default:
                    throw new WebDriverException("Unknown browser: " + browser);
            }

            // Common driver configurations
            if (driver != null) {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driver.manage().window().maximize();
                System.out.println("✅ Driver initialized successfully: " + browser);
            }
        }
        return driver;
    }

    // ========== CHROME METHODS ==========
    private static void setupLocalChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        driver = new ChromeDriver(options);
    }

    private static void setupLinuxChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
    }

    private static void setupRemoteChrome(String gridAddress, boolean isLinux) {
        try {
            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
            ChromeOptions options = new ChromeOptions();

            if (isLinux) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
            }

            // Common Chrome options for remote execution
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-extensions");
            options.addArguments("--no-first-run");
            options.addArguments("--no-default-browser-check");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

            driver = new RemoteWebDriver(url, options);

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize remote chrome: " + e.getMessage());
            throw new RuntimeException("Remote chrome initialization failed", e);
        }
    }

    // ========== FIREFOX METHODS ==========
    private static void setupLocalFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        driver = new FirefoxDriver(options);
    }

    private static void setupLinuxFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        driver = new FirefoxDriver(options);
    }

    private static void setupRemoteFirefox(String gridAddress, boolean isLinux) {
        try {
            URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
            FirefoxOptions options = new FirefoxOptions();

            if (isLinux) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
            }

            driver = new RemoteWebDriver(url, options);

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize remote firefox: " + e.getMessage());
            throw new RuntimeException("Remote firefox initialization failed", e);
        }
    }

    // ========== OTHER BROWSERS ==========
    private static void setupInternetExplorer() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new WebDriverException("Your operating system does not support Internet Explorer");
        }
        WebDriverManager.iedriver().setup();
        driver = new InternetExplorerDriver();
    }

    private static void setupEdge() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new WebDriverException("Your operating system does not support Edge");
        }
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
    }

    private static void setupSafari() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            throw new WebDriverException("Your operating system does not support Safari");
        }
        WebDriverManager.getInstance(SafariDriver.class).setup();
        driver = new SafariDriver();
    }

    public static void closeDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("🔚 Driver closed successfully");
            } catch (Exception e) {
                System.err.println("⚠ Error while closing driver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    // Helper method to get current browser name
    public static String getBrowserName() {
        return browser;
    }
}