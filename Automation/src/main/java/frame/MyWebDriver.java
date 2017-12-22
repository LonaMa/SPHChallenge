package frame;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MyWebDriver implements IMyDriver{

	private static final Logger LOGGERERR = LogManager.getLogger();
	private WebDriver driver;
	private String driverPath = System.getProperty("user.dir") + "/src/test/resources/Drivers/chromedriver.exe";

	public MyWebDriver() {
		ChromeOptions options = new ChromeOptions();		

		options.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver", driverPath);
		
		driver = new ChromeDriver(options);
		driver.manage().deleteAllCookies();
	}
	public WebDriver getDriver() {
		return driver;
	}
	
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
	public void navigateTo(String url) {
		driver.get(url);
	}
	
	public void click(By by) {
		driver.findElement(by).click();
		LOGGERERR.info("Click the element:" + by.toString());
	}
	
	public void sendKeys(By by, CharSequence... keysToSend) {
		driver.findElement(by).sendKeys(keysToSend);
		LOGGERERR.info("SenkKeys to:" + by.toString() +", value is:" + keysToSend.toString());
	}
	
	public boolean checkElementExist(By by, int timeout) {
		//WebElement 
		FluentWait<WebDriver> wait = new WebDriverWait(driver, timeout).pollingEvery(500, TimeUnit.MILLISECONDS);
        try {
        	WebElement ele = (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(by));
        	LOGGERERR.info("Find the element exist:" + by.toString());
        } catch(Exception e) {
        	LOGGERERR.warn("Cannot find the element:" + by.toString());
        	return false;
        }
        return true;
	}
	
	public void switchFrame(WebElement obj) {
		driver.switchTo().frame(obj);
	}
	
	public String getCurrentUrl() {
		String url = driver.getCurrentUrl();
		LOGGERERR.info("Current URL is:" + url);
		return url;
	}
	
	public Boolean checkImgPresent(By by) {
		WebElement imageFile = this.findElement(by);
		return (Boolean) ((JavascriptExecutor)driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", imageFile);
	}
	
	public void quitDriver() {
		driver.quit();
	}
}
