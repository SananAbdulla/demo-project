//package loopcamp;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverException;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.ie.InternetExplorerDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.safari.SafariDriver;
//
//import java.net.URL;
//
//public class Driver {
//    static String browser;
//
//    private Driver() {
//    }
//
//    private static WebDriver driver;
//    private static ChromeOptions chromeOptions;
//    private static FirefoxOptions firefoxOptions;
//
//    private static DesiredCapabilities desiredCapabilities;
//
//    public static WebDriver getDriver() {
//        if (driver == null) {
//            if (System.getProperty("BROWSER") == null) {
//                browser = ConfigurationReader.getProperty("browser");
//            } else {
//                browser = System.getProperty("BROWSER");
//            }
//            System.out.println("Browser: " + browser);
//            switch (browser) {
//                case "remote-chrome":
//                    try {
//                        // assign your grid server address
//                        String gridAddress = "54.226.36.175";
//                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
//                        desiredCapabilities = new DesiredCapabilities();
//                        desiredCapabilities.setBrowserName("chrome");
//                        driver = new RemoteWebDriver(url, desiredCapabilities);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//                case "remote-firefox":
//                    try {
//                        // assign your grid server address
//                        String gridAddress = "54.226.36.175";
//                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
//                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//                        desiredCapabilities.setBrowserName("firefox");
//                        driver = new RemoteWebDriver(url, desiredCapabilities);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//                case "chrome":
//                    WebDriverManager.chromedriver().setup();
//                    driver = new ChromeDriver();
//                    break;
//
//                case "chrome-headless":
////                    WebDriverManager.chromedriver().setup();
////                    driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
//                    ChromeOptions options = new ChromeOptions();
//                    options.addArguments("--headless"); // Enable headless mode
//                    //options.addArguments("start-maximized"); // maximize
//                    WebDriverManager.chromedriver().setup();
//                    driver = new ChromeDriver(options);
//                    break;
//
//                case "firefox":
//                    WebDriverManager.firefoxdriver().setup();
//                    driver = new FirefoxDriver();
//                    break;
//
//                case "firefox-headless":
////                    WebDriverManager.firefoxdriver().setup();
////                    driver = new FirefoxDriver(new FirefoxOptions().setHeadless(true));
//                    FirefoxOptions options2 = new FirefoxOptions();
//                    options2.addArguments("--headless"); // Enable headless mode
//                    //options.addArguments("start-maximized"); // maximize
//                    WebDriverManager.firefoxdriver().setup();
//                    driver = new FirefoxDriver(options2);
//                    break;
//
//                case "ie":
//                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
//                        throw new WebDriverException("Your operating system does not support the requested browser");
//                    }
//                    WebDriverManager.iedriver().setup();
//                    driver = new InternetExplorerDriver();
//                    break;
//
//                case "edge":
//                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
//                        throw new WebDriverException("Your operating system does not support the requested browser");
//                    }
//                    WebDriverManager.edgedriver().setup();
//                    driver = new EdgeDriver();
//                    break;
//
//                case "safari":
//                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
//                        throw new WebDriverException("Your operating system does not support the requested browser");
//                    }
//                    WebDriverManager.getInstance(SafariDriver.class).setup();
//                    driver = new SafariDriver();
//                    break;
//
//
//                /**
//                 * These added because of EC@2 Jenkins on Linux was not running the ones above because of graphical issues.
//                 */
//                case "chrome-linux":
//                    WebDriverManager.chromedriver().setup();
//                    chromeOptions = new ChromeOptions();
//                    chromeOptions.addArguments("--headless");
//                    chromeOptions.addArguments("--no-sandbox");
//                    chromeOptions.addArguments("--disable-dev-shm-usage");
//                    driver = new ChromeDriver(chromeOptions);
//                    break;
//
////                case "chrome-linux":
////                    WebDriverManager.chromedriver().setup();
////                    chromeOptions = new ChromeOptions();
////                    chromeOptions.addArguments("--headless"); // run without UI
////                    chromeOptions.addArguments("--no-sandbox"); // required on Linux
////                    chromeOptions.addArguments("--disable-dev-shm-usage"); // prevent crashes
////                    chromeOptions.addArguments("--disable-gpu"); // safer on headless servers
////                    chromeOptions.addArguments("--window-size=1920,1080"); // ensures full page rendering
////                    driver = new ChromeDriver(chromeOptions);
////                    break;
//
//                case "remote-chrome-linux":
//                    try {
//                        // assign your grid server address
//                        String gridAddress = "34.227.172.25";
//                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
//                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//                        chromeOptions = new ChromeOptions();
//                        chromeOptions.addArguments("--headless");
//                        chromeOptions.addArguments("--no-sandbox");
//                        chromeOptions.addArguments("--disable-dev-shm-usage");
//                        desiredCapabilities.merge(chromeOptions);
//                        driver = new RemoteWebDriver(url, desiredCapabilities);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//                case "firefox-linux":
//                    WebDriverManager.firefoxdriver().setup();
//                    firefoxOptions = new FirefoxOptions();
//                    firefoxOptions.addArguments("--headless");
//                    firefoxOptions.addArguments("--disable-gpu");
//                    firefoxOptions.addArguments("--no-sandbox");
//                    driver = new FirefoxDriver(firefoxOptions);
//                    break;
//
//                case "remote-firefox-linux":
//                    try {
//                        // assign your grid server address
//                        String gridAddress = "34.227.172.25";
//                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
//                        desiredCapabilities = new DesiredCapabilities();
//                        desiredCapabilities.setBrowserName("firefox");
//                        FirefoxOptions firefoxOptions = new FirefoxOptions();
//                        firefoxOptions.addArguments("--headless");
//                        firefoxOptions.addArguments("--disable-gpu");
//                        firefoxOptions.addArguments("--no-sandbox");
//                        desiredCapabilities.merge(firefoxOptions);
//                        driver = new RemoteWebDriver(url, desiredCapabilities);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//
//        return driver;
//    }
//
//    public static void closeDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }
//}


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
    private static ChromeOptions chromeOptions;
    private static FirefoxOptions firefoxOptions;

    private static DesiredCapabilities desiredCapabilities;

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
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--headless");
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(options);
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                        break;

                    case "firefox-headless":
                        FirefoxOptions options2 = new FirefoxOptions();
                        options2.addArguments("--headless");
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver(options2);
                        break;

                    case "ie":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("Your operating system does not support IE");
                        }
                        WebDriverManager.iedriver().setup();
                        driver = new InternetExplorerDriver();
                        break;

                    case "edge":
                        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                            throw new WebDriverException("Your operating system does not support Edge");
                        }
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                        break;

                    case "safari":
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            throw new WebDriverException("Your operating system does not support Safari");
                        }
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driver = new SafariDriver();
                        break;

                    case "chrome-linux":
//                        WebDriverManager.chromedriver().setup();
//                        chromeOptions = new ChromeOptions();
//                        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
//                        driver = new ChromeDriver(chromeOptions);
//                        break;

                        WebDriverManager.chromedriver().setup();
                        chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--window-size=1920,1080");
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        driver = new ChromeDriver(chromeOptions);
                        break;

                    case "remote-chrome-linux":
                        driver = initRemoteDriverWithOptions("34.227.172.25", new ChromeOptions());
                        break;

                    case "firefox-linux":
                        WebDriverManager.firefoxdriver().setup();
                        firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = new FirefoxDriver(firefoxOptions);
                        break;

                    case "remote-firefox-linux":
                        FirefoxOptions ffOptions = new FirefoxOptions();
                        ffOptions.addArguments("--headless", "--disable-gpu", "--no-sandbox");
                        driver = initRemoteDriverWithOptions("34.227.172.25", ffOptions);
                        break;

                    default:
//                        // Fallback: Jenkins-safe headless Chrome
//                        System.out.println("⚠ Unknown browser: " + browser + " → Falling back to headless Chrome.");
//                        WebDriverManager.chromedriver().setup();
//                        ChromeOptions defaultOptions = new ChromeOptions();
//                        defaultOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
//                        driver = new ChromeDriver(defaultOptions);
//                        break;

                        System.out.println("⚠ Unknown browser: " + browser + " → Falling back to headless Chrome.");
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions defaultOptions = new ChromeOptions();
                        defaultOptions.addArguments("--headless=new");
                        defaultOptions.addArguments("--no-sandbox");
                        defaultOptions.addArguments("--disable-dev-shm-usage");
                        defaultOptions.addArguments("--disable-gpu");
                        defaultOptions.addArguments("--window-size=1920,1080");
                        defaultOptions.addArguments("--remote-allow-origins=*");
                        driver = new ChromeDriver(defaultOptions);
                        break;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to initialize driver for browser: " + browser);
                e.printStackTrace();
                // Ensure we at least get a headless Chrome for Jenkins
                if (driver == null) {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions fallbackOptions = new ChromeOptions();
                    fallbackOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
                    driver = new ChromeDriver(fallbackOptions);
                }
            }
        }

        return driver;
    }

    private static WebDriver initRemoteDriver(String gridAddress, String browserName) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(browserName);
        return new RemoteWebDriver(url, desiredCapabilities);
    }

    private static WebDriver initRemoteDriverWithOptions(String gridAddress, ChromeOptions options) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("chrome");
        desiredCapabilities.merge(options);
        return new RemoteWebDriver(url, desiredCapabilities);
    }

    private static WebDriver initRemoteDriverWithOptions(String gridAddress, FirefoxOptions options) throws Exception {
        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("firefox");
        desiredCapabilities.merge(options);
        return new RemoteWebDriver(url, desiredCapabilities);
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
