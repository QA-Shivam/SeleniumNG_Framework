package utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WebElementsInteractions {
    protected WebDriver driver;

    public WebElementsInteractions(WebDriver driver) {
        this.driver = driver;
    }

    public void gotToApplication(String url) {
        driver.get(url);
    }

    public void click(By locator) {
        driver.findElement(locator).click();
    }

       public void clear(By locator) {
        driver.findElement(locator).clear();
    }
    public String getText(By locator) {
        return driver.findElement(locator).getText();
    }
    public void sendText(By locator, String text) {
        driver.findElement(locator).sendKeys(text);
    }
}
