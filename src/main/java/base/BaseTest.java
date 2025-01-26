package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import config.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;

import static base.BasePage.convertSSToBase64;
import static base.BasePage.getScreenshot;
import static utils.ExtentReport.getReportObject;

public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    public static ThreadLocal<ExtentTest> ExtentReport = new ThreadLocal<>();
    public static ExtentReports reports = getReportObject();
    protected WebDriver driver;

    @Parameters({"browser", "version", "os"})
    @BeforeMethod
    public void beforeTest(String browser, @Optional String version, @Optional String os, ITestResult iTestResult) throws InterruptedException {
        ConfigLoader config = new ConfigLoader();
        String applicationurl = config.getProperty("url");
        String configBrowser = System.getProperty("browser", config.getProperty("browser"));
        String runMode = System.getProperty("runmode", config.getProperty("runmode"));
        String platform = System.getProperty("platform", config.getProperty("platform"));
        String lambdaTestURL = config.getProperty("lambdaTestURL");
        String seleniumGridURL = config.getProperty("seleniumGridURL");
        String applicationName = config.getProperty("applicationname");

        // Determine browser to use (priority: Maven -> TestNG -> Config)
        browser = (browser != null && !browser.isEmpty()) ? browser.toLowerCase() : configBrowser;

        // Validate inputs
        if (!browser.matches("chrome|firefox|edge|ie")) {
            throw new IllegalArgumentException("Invalid browser specified: " + browser);
        }
        if (!runMode.matches("local|remote")) {
            throw new IllegalArgumentException("Invalid runmode specified: " + runMode);
        }
        if (runMode.equals("remote") && !platform.matches("seleniumgrid|lambdatest|githubactions")) {
            throw new IllegalArgumentException("Invalid platform specified: " + platform);
        }

        // Initialize WebDriver
        if (runMode.equals("remote")) {
            String remoteURL = platform.equalsIgnoreCase("lambdatest") ? lambdaTestURL : seleniumGridURL;
            if (remoteURL == null || remoteURL.isEmpty()) {
                throw new IllegalArgumentException("Remote URL is not specified for platform: " + platform);
            }
            try {
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    if (platform.equalsIgnoreCase("lambdatest")) {
                        options.setCapability(CapabilityType.BROWSER_NAME, "chrome");
                        options.setCapability("browserVersion", version);
                        options.setCapability("platformName", os);
                        options.setCapability("lt:options", new HashMap<String, Object>() {{
                            put("build", applicationName);
                            put("name", applicationName);
                            put("selenium_version", "4.0.0");
                            put("w3c", true);
                        }});
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }
                    if (platform.equalsIgnoreCase("githubactions")) {
                        options.addArguments("--headless");
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-gpu");
                        driver = new ChromeDriver(options);
                    }
                    if (platform.equalsIgnoreCase("seleniumgrid")) {
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }
                } else if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions options = new FirefoxOptions();
                    if (platform.equalsIgnoreCase("lambdatest")) {
                        options.setCapability(CapabilityType.BROWSER_NAME, "firefox");
                        options.setCapability("browserVersion", version);
                        options.setCapability("platformName", os);
                        options.setCapability("lt:options", new HashMap<String, Object>() {{
                            put("build", applicationName);
                            put("name", applicationName);
                            put("w3c", true);
                        }});
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }
                    if (platform.equalsIgnoreCase("githubactions")) {
                        options.addArguments("--headless");
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-gpu");
                        driver = new FirefoxDriver(options);
                    }
                    if (platform.equalsIgnoreCase("seleniumgrid")) {
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }
                } else if (browser.equalsIgnoreCase("safari")) {
                    SafariOptions options = new SafariOptions();

                    if (platform.equalsIgnoreCase("lambdatest")) {
                        options.setCapability(CapabilityType.BROWSER_NAME, "safari");
                        options.setCapability("browserVersion", version);
                        options.setCapability("platformName", os);
                        options.setCapability("lt:options", new HashMap<String, Object>() {{
                            put("build", applicationName);
                            put("name", applicationName);
                            put("w3c", true);
                        }});
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }
                    if (platform.equalsIgnoreCase("seleniumgrid")) {
                        driver = new RemoteWebDriver(new URL(remoteURL), options);
                    }

                } else {
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize remote WebDriver: " + e.getMessage(), e);
            }
        } else {
            driver = browser.equals("firefox") ? new FirefoxDriver() : new ChromeDriver();
        }

        // Log test details
        logger.info("Application Name: " + applicationName);
        logger.info("Browser: " + browser);
        logger.info("Browser Version: " + version);
        logger.info("OS: " + os);
        logger.info("Run Mode: " + runMode);
        logger.info("Platform: " + platform);

        // Browser setup
        driver.get(applicationurl);
        driver.manage().window().maximize();

        // ExtentTest setup with detailed test name
        String category = "Browser- " + browser + " | Browser Version- " + version + " | Operating System: " + os + " | Run Mode: " + runMode + " | Platform: " + platform;
        ExtentTest test = reports.createTest(iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription()).assignCategory(category);
        ExtentReport.set(test);

        // Add additional metadata to the Extent Report
        ExtentReport.get().info("Test Start Time: " + LocalDateTime.now());

    }


    @AfterMethod
    public void afterTest(ITestResult iTestResult) {
        if (iTestResult.getStatus() == ITestResult.FAILURE) {
            ExtentReport.get().log(Status.FAIL, "Test Failed Due to: " + iTestResult.getThrowable());
            String screenshotPath = getScreenshot(iTestResult.getMethod().getMethodName() + ".jpg", driver);
            ExtentReport.get().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromBase64String(convertSSToBase64(screenshotPath)).build());

        } else if (iTestResult.getStatus() == ITestResult.SUCCESS) {
            ExtentReport.get().log(Status.PASS, MarkupHelper.createLabel(iTestResult.getMethod().getMethodName(), ExtentColor.GREEN));
            String screenshotPath = getScreenshot(iTestResult.getMethod().getMethodName() + ".jpg", driver);
            ExtentReport.get().pass("Test Pass", MediaEntityBuilder.createScreenCaptureFromBase64String(convertSSToBase64(screenshotPath)).build());
        } else if (iTestResult.getStatus() == ITestResult.SKIP) {
            ExtentReport.get().log(Status.SKIP, MarkupHelper.createLabel(iTestResult.getMethod().getMethodName(), ExtentColor.ORANGE));
            String screenshotPath = getScreenshot(iTestResult.getMethod().getMethodName() + ".jpg", driver);
            ExtentReport.get().skip("Test Skipped", MediaEntityBuilder.createScreenCaptureFromBase64String(convertSSToBase64(screenshotPath)).build());
        }
        ExtentReport.get().info("Test End Time: " + LocalDateTime.now());
        driver.quit();
    }

    @AfterClass
    public void afterClass() {
        reports.flush();
    }

}
