package frame;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
public class MobileDriver implements IMyDriver{

	private static final Logger LOGGERERR = LogManager.getLogger();
	private AndroidDriver<WebElement> driver;
	//private final static String Appium_Home = "C:\\Program Files (x86)\\Appium";
	private final static String Appium_Home = "C:\\Users\\nanma\\AppData\\Local\\Programs\\appium-desktop";
	private final static String Appium_log_level = "debug";//info, debug, warn, error
	
	
	private final int DEFAULTSCROLL = 10;
	int retry = 0;

	public void init(String deviceName, String platform, String platformVersion, String appPackage, String appActivity, String appWaitActivity) throws Exception {
		//start appium serive
		
		////work around for 4.1.2//////
	//	String rmUnlockCmd = "C:/Users/nanma/AppData/Local/Android/sdk/platform-tools/adb.exe -s ce12160c19e8a42905 uninstall io.appium.unlock";
	//	Runtime.getRuntime().exec(rmUnlockCmd);
	//	String rmSettingCmd = "C:/Users/nanma/AppData/Local/Android/sdk/platform-tools/adb.exe -s ce12160c19e8a42905 uninstall io.appium.settings";
	//	Runtime.getRuntime().exec(rmSettingCmd);
		
		////////////////////
		
	//	run_Appium(Appium_Home, "127.0.0.1", "4723", "--no-reset", Appium_log_level);
				
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
        //	capabilities.setCapability("appWaitActivity", appWaitActivity);//com.google.android.gms.ads.AdActivity
        }

        this.sleep(1);
        LOGGERERR.info("Start Connection ..." + "\n");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

       
        LOGGERERR.info("Connected." + "\n");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}



	public AndroidDriver<WebElement> getDriver() {
		return driver;
	}
	
	public void type(By by, int x, int y) {
		TouchAction tAction= new TouchAction(driver);
		tAction.tap(driver.findElement(by), x, y);
		LOGGERERR.info("Type element:" + by.toString() + ", x:" + x + ", y:" + y);	
	}
	
	public void type(int x, int y) {
		TouchAction tAction= new TouchAction(driver);
		tAction.tap(x, y);
		LOGGERERR.info("Type on x:" + x + ", y:" + y);
	}
	
	public void type(By by) {
		WebElement ele = null;
		
		TouchAction tAction= new TouchAction(driver);
		if(checkElementExist(by, 20)){
			ele = driver.findElement(by);
		} else {
			if(retry <= 3){
				type(by);
				retry ++;
				LOGGERERR.warn("Retry to find the element:" + by.toString());
			}
		}
		int x = getElementCoordinate(ele).get("middleX");
		int y = getElementCoordinate(ele).get("middleY");
		tAction.tap(x, y);
		LOGGERERR.info("Type on element:"+ by.toString() +" x:" + x + ", y:" + y);
		this.sleep(3);
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
		WebElement ele = null;
		if(checkElementExist(by, 20)){
			ele = driver.findElement(by);
		} else {
			if(retry <= 3){
				type(by);
				retry ++;
				LOGGERERR.warn("Retry to find the element:" + by.toString());
			}
		}
		ele.click();
	}
	
	public void click(By by, int scrollTimes) {
		this.findElementThoughScorll(by, scrollTimes).click();
	}
	
	public void click(WebElement element) {
		element.click();
	}


	@Override
	public void sendKeys(By by, CharSequence... keysToSend) {
		this.findElementThoughScorll(by, DEFAULTSCROLL).sendKeys(keysToSend);;
		
	}
	
	public void sendKeys(WebElement element, CharSequence... keysToSend) {
		element.sendKeys(keysToSend);;
		
	}
	
	public void hideKeyboard()
	{
		driver.hideKeyboard();
	}


	public boolean checkElementExistByScroll(By by, int scrollTime, int timeout) {
		if(findElementThoughScorll(by, scrollTime, timeout) == null)
			return false;
		else
			return true;
	}
	
	public boolean checkElementExist(By by, int timeout) {
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

	public void quitDriver() {
		 driver.quit();
	}
	
	public void touchMoveUp()
	{
		int scrHeight = driver.manage().window().getSize().getHeight(); //To get the mobile screen height
		int scrWidth = driver.manage().window().getSize().getWidth();
        TouchAction action = new TouchAction(driver);
        action.press(scrWidth/2, scrHeight/2).waitAction(Duration.ofSeconds(2)).moveTo(scrWidth/2, scrHeight/4).release().perform();
	}
	
	 public Map<String, Integer> getElementCoordinate(By by)
	    {
		 LOGGERERR.info("Ready to Find element coordinate:" + by.toString());
		  return getElementCoordinate(driver.findElement(by));
	    }
	 
	 public Map<String, Integer> getElementCoordinate(WebElement element)
	    {
	    	Map<String, Integer> coordinate = new HashMap<String, Integer>() ;

	    	int leftX = element.getLocation().getX();
	    	int rightX = leftX + element.getSize().getWidth();
	    	int middleX = (rightX + leftX) / 2;
	    	int upperY = element.getLocation().getY();
	    	int lowerY = upperY + element.getSize().getHeight();
	    	int middleY = (upperY + lowerY) / 2;
	    	
	    	coordinate.put("leftX", leftX);
	    	coordinate.put("rightX", rightX);
	    	coordinate.put("middleX", middleX);
	    	coordinate.put("upperY", upperY);
	    	coordinate.put("lowerY", lowerY);
	    	coordinate.put("middleY", middleY);
	    	
	    	return coordinate;
	    }
	 
	 protected WebElement findViewElementChild(WebElement byFatherOfListView, int index)
		{

			WebElement findOne = null;
			List<WebElement> selections = null;
			int count = index;
			
			if (byFatherOfListView != null)
			{
				selections = byFatherOfListView.findElements(By.className("android.webkit.WebView"));
			}
			else
			{
				selections = driver.findElements(By.className("android.webkit.WebView"));
			}
			
			return selections.get(index);
		}
	 
	 
		public void sleep(int timeout) {
			try {
				Thread.sleep(timeout *1000);
			} catch (InterruptedException e) {
				LOGGERERR.info("sleep was interrupted");
			}
		}
}

