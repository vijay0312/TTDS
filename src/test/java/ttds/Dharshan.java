package ttds;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Dharshan {
	ExtentReports extent;
	ExtentTest logger;
	static WebDriver driver;
	
	@BeforeTest
	public void startReport() {
		extent = new ExtentReports (System.getProperty("user.dir")+"/test-output/TTDSExtentReport.html", true);
		extent
			.addSystemInfo("Host Name", "TTDS Online")
			.addSystemInfo("User Name", "VIJAY V S");
		extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
	}
	@Test
	public void page() throws InterruptedException {
		logger = extent.startTest("page");
		System.setProperty("webdriver.chrome.driver", "D:\\TESTING\\Automation\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://tirupatibalaji.ap.gov.in/#/login");
		
		Thread.sleep(5000);
		String Title = driver.getTitle();
		Assert.assertEquals(Title, "Tirumala Tirupati Devasthanams(Official Booking Portal)");
		logger.log(LogStatus.PASS, "Test passed");
	}
	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if(result.getStatus() == ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, "Test Case Passed is "+ result.getName());;
			logger.log(LogStatus.PASS, "Test Case Passed is "+ result.getThrowable());
			String screenshotPath = Dharshan.getScreenshot(driver, result.getName());
			logger.log(LogStatus.PASS, logger.addScreenCapture(screenshotPath));
		}
		else if(result.getStatus() == ITestResult.SKIP){
			logger.log(LogStatus.SKIP, "Test Case Skipped is "+result.getName());
			}
		extent.endTest(logger);
	}
	public static String getScreenshot(WebDriver driver2, String name) throws IOException {
		 String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
	             //after execution, you could see a folder "FailedTestsScreenshots" under src folder
			String destination = System.getProperty("user.dir") + "/Screenshots/"+"done"+dateName+".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
	             //Returns the captured file path
			return destination;
		// TODO Auto-generated method stub
		
	}

	@AfterTest
	public void endReport(){
	//writing everything to document
	//flush() - to write or update test information to your report.
	               extent.flush();
	               //Call close() at the very end of your session to clear all resources.
	               //If any of your test ended abruptly causing any side-affects (not all logs sent to ExtentReports, information missing), this method will ensure that the test is still appended to the report with a warning message.
	               //You should call close() only once, at the very end (in @AfterSuite for example) as it closes the underlying stream.
	               //Once this method is called, calling any Extent method will throw an error.
	               //close() - To close all the operation
	               extent.close();
	               driver.close();
	   }
}
