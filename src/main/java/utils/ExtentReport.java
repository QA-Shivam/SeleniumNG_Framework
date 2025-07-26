package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReport {
    public static ExtentReports reports;
    public static String reportPath;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
    public static ExtentReports getReportObject()
    {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
        reportPath = "./reports/Swag_Labs_" + dtf.format(LocalDateTime.now()) + ".html";
        //System.out.println(reportPath);
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("Swag Labs");
        sparkReporter.config().setDocumentTitle("Swag Labs Test Results");
        sparkReporter.config().setJs("document.getElementsByClassName('col-sm-12 col-md-4')[0].style.setProperty('min-inline-size','-webkit-fill-available');");


        reports = new ExtentReports();
        reports.attachReporter(sparkReporter);

        reports.setSystemInfo("Tester : ", "Shivam");
        return  reports;

    }

}
