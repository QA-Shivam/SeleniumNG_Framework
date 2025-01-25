package base;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.util.Base64;


public class BasePage {
    public static String getScreenshot(String img, WebDriver driver) {
        TakesScreenshot takesScreenshot=(TakesScreenshot) driver;
        File file = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String filePath = System.getProperty("user.dir") + "/screenshots/" + img ;
        try {
            FileUtils.copyFile(file, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }
    public static String  convertSSToBase64(String imgPath) {
        String encodedBase64 = null;
        try {
            File file = new File(imgPath);
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            encodedBase64 = new String(Base64.getEncoder().encode(fileContent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return encodedBase64;
    }
}
