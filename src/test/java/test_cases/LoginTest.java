package test_cases;
import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import page_objects.LoginPage;
public class LoginTest extends BaseTest {
LoginPage loginPage;
private static final Logger logger= LogManager.getLogger(LoginTest.class);
    @Test
    public void loginTest() {
        logger.info("***************Starting Login Test***************");
        loginPage= new LoginPage(driver);
        loginPage.userLogin("standard_user", "secret_sauce");
    }
}