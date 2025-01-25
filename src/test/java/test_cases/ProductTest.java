package test_cases;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import page_objects.LoginPage;
import page_objects.ProductsPage;

public class ProductTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    Logger logger = LogManager.getLogger(ProductTest.class);

    @Test
    public void productTest() {
        logger.info("***************Starting Product Test***************");
        loginPage = new LoginPage(driver);
        productsPage = loginPage.userLogin("standard_user", "secret_sauce");
        String productPageTitle = productsPage.getProductPageTitle();
        Assert.assertEquals(productPageTitle, "Products");
        ExtentReport.get().log(Status.PASS, "Product Page Title: " + productPageTitle);
        String productName = productsPage.getProductName();
        Assert.assertEquals(productName, "Sauce Labs Backpack");
        ExtentReport.get().log(Status.PASS, "Product Name: " + productName);
    }
}
