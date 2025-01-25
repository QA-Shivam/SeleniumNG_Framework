package page_objects;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebElementsInteractions;

import static base.BaseTest.ExtentReport;

public class LoginPage extends WebElementsInteractions {

    private final By usernametextfield = By.id("user-name");
    private final By passwordtextfiled = By.id("password");
    private final By loginButton = By.id("login-button");
    Logger logger = LogManager.getLogger(LoginPage.class);


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage userLogin(String username, String password) {
        logger.info("Logging in with username: " + username + " and password: " + password);
        sendText(usernametextfield, username);
        ExtentReport.get().log(Status.INFO, "Entered username: " + username);
        sendText(passwordtextfiled, password);
        ExtentReport.get().log(Status.INFO, "Entered password: " + password);
        click(loginButton);
        ExtentReport.get().log(Status.INFO, "Clicked on login button");
        ExtentReport.get().log(Status.PASS, "Logged in with username: " + username + " and password: " + password);
        return new ProductsPage(driver);
    }
}
