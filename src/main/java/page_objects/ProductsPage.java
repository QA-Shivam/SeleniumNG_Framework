package page_objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WebElementsInteractions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends WebElementsInteractions {
    Logger logger =  LogManager.getLogger(ProductsPage.class);
    private final By productpagetitle = By.xpath("//span[contains(text(),'Products')]");
    private final By productname = By.id("item_4_title_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }
    public String getProductPageTitle() {
        String title = getText(productpagetitle);
        logger.info("Product Page Title: " + title);
        return title;
    }
    public String getProductName() {
        String productName = getText(productname);
        logger.info("Product Name: " + productName);
        return productName;
    }
}
