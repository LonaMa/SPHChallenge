package frame;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
public class MobileDriver implements IMyDriver{

	private static final Logger LOGGERERR = LogManager.getLogger();
	private AndroidDriver<WebElement> driver;
	
	private final int DEFAULTSCROLL = 10;

	public void init(String deviceName, String platform, String platformVersion, String appPackage, String appActivity, String appWaitActivity) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", platform);
        capabilities.setCapability("platformVersion", platformVersion);
 
        if(appPackage == "Browser")
        {
        	capabilities.setCapability("browserName", appActivity);//Chrome, IE
        }
        else
        {
            capabilities.setCapability("appPackage", appPackage); 
        	capabilities.setCapability("appActivity", appActivity);//.activities.PeopleActivity
        	capabilities.setCapability("appWaitActivity", appWaitActivity);//com.google.android.gms.ads.AdActivity
        }

        Thread.sleep(1000);
        LOGGERERR.info("Start Connection ..." + "\n");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

       
        LOGGERERR.info("Connected." + "\n");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}



	public WebDriver getDriver() {
		return driver;
	}

	public WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
	public WebElement findElementThoughScorll(By by, int topScrollTimes)
	{
		return findElementThoughScorll(by, topScrollTimes, 30);
	}
	
	public WebElement findElementThoughScorll(By by, int topScrollTimes, int timeout)
	{
		WebElement ele = null;
		WebDriverWait wait = new WebDriverWait(driver,timeout);
		int scrollTime = 0;
		do{
			try
			{
				Thread.sleep(100);
				ele = driver.findElement(by);
			}
			catch(Exception e)
			{
				try{
					ele = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				}
				catch(Exception ex)
				{
					ele = null;
					touchMoveUp();
					scrollTime ++;
					LOGGERERR.info("Cannot find element:" + by.toString() + "\n" + "Scroll screen to find the element. Times:"+scrollTime);
				}
			}

		}while(ele == null && scrollTime < topScrollTimes);
		
		return ele;
	}

	public void click(By by) {
		this.click(by, DEFAULTSCROLL);
	}
	
	public void click(By by, int scrollTimes) {
		this.findElementThoughScorll(by, scrollTimes).click();
	}


	@Override
	public void sendKeys(By by, CharSequence... keysToSend) {
		this.findElementThoughScorll(by, DEFAULTSCROLL);
		
	}


	@Override
	public boolean checkElementExist(By by, int timeout) {
		return checkElementExist(by, 1, timeout);
	}
	
	public boolean checkElementExist(By by, int scrollTime, int timeout) {
		if(findElementThoughScorll(by, scrollTime, timeout) == null)
			return false;
		else
			return true;
	}

	public void quitDriver() {
		 driver.quit();
	}
	
	public void touchMoveUp()
	{
		int scrHeight = driver.manage().window().getSize().getHeight(); //To get the mobile screen height
		int scrWidth = driver.manage().window().getSize().getWidth();
        TouchAction action = new TouchAction(driver);
        action.press(scrWidth/2, scrHeight/2).waitAction(Duration.ofSeconds(2)).moveTo(scrWidth/2, scrHeight/3).release().perform();
	}
}
