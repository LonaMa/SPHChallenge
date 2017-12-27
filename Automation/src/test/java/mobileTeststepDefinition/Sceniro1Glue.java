package mobileTeststepDefinition;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import frame.MobileDriver;


public class Sceniro1Glue {

	MobileDriver mobile = new MobileDriver();
	private static final Logger LOGGERERR = LogManager.getLogger();
	private static final int DEFAULTTIMEOUT = 20;
	private AndroidDriver<WebElement> driver;
	private final By titleBar = By.id("navbar");
	private final By btnNavIcon = By.className("android.widget.Button");
	private final By barLogin = By.id("block-dfp-lb1");
	private final By iconLogin = By.id("google_ads_iframe_/5908/st/lb1/homepage_0__container__");
	
	private final By txtLoginIDNPassword = By.className("android.widget.EditText");//By.id("j_username");
	private final By txtPassword = By.id("j_password");
	private final By btnSignIn = By.className("android.widget.Button");
	
	
	private final By layoutAdPopup = By.xpath("//*[@class='android.widget.RelativeLayout' and @content-desc='StartApp Ad']");
	
	private final By imgTopstory = By.className("android.widget.Image");
	
	
	private String topStoryContent;
	private String linkResource;
	private Boolean topStoryImgExist = false;
	private Boolean topStoryVideoExist = false;
	private String strVideoId;
	
	@Before
	public void initConnection() {
			try {
				mobile.init("ce12160c19e8a42905", "Android", "7.0", "tvn.breaking.news.straitstimes.com", ".MainActivity", "");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGERERR.error("Cannot init connection to the phone");
			}
			driver = mobile.getDriver();
			mobile.sleep(15);

	}
	@Given("^User is on the Login Page$")
	public void user_is_on_the_Login_Page() {
		
		if(mobile.checkElementExist(layoutAdPopup, DEFAULTTIMEOUT)){
			int xTitle = mobile.getElementCoordinate(layoutAdPopup).get("leftX") + 10;
			int yTitle = mobile.getElementCoordinate(layoutAdPopup).get("upperY") + 15;
			mobile.type(xTitle, yTitle);
		}
		mobile.sleep(5);
		//mobile.type(btnNavIcon);
		mobile.click(btnNavIcon);
		mobile.sleep(5);
		
		int xLoginIcon = mobile.getElementCoordinate(barLogin).get("rightX") - 100;
		int yLoginIcon = mobile.getElementCoordinate(barLogin).get("lowerY") - 100;
		mobile.type(xLoginIcon, yLoginIcon);
		mobile.sleep(5);
		
		int xLoginItem = mobile.getElementCoordinate(iconLogin).get("leftX") + 100;
		int yLoginItem = mobile.getElementCoordinate(iconLogin).get("lowerY") + 120;
		mobile.type(xLoginItem, yLoginItem);
		mobile.sleep(15);
	}

	@When("^User enter \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_enter_and(String arg1, String arg2) {

		mobile.checkElementExist(txtPassword, 30);
		mobile.sendKeys(driver.findElements(txtLoginIDNPassword).get(0), arg1);
		mobile.sendKeys(driver.findElements(txtLoginIDNPassword).get(1), arg2);
		mobile.hideKeyboard();
	}

	@When("^User click the login button$")
	public void user_click_the_login_button() {
		mobile.sleep(5);
		mobile.click(btnSignIn);
	}

	@Then("^User should login successfully$")
	public void user_should_login_successfully() {

		mobile.sleep(5);
		//mobile.type(btnNavIcon);
		mobile.click(btnNavIcon);
		mobile.sleep(5);
		
		int xLoginIcon = mobile.getElementCoordinate(barLogin).get("rightX") - 100;
		int yLoginIcon = mobile.getElementCoordinate(barLogin).get("lowerY") - 100;
		mobile.type(xLoginIcon, yLoginIcon);
		mobile.sleep(5);
		
		int xLoginItem = mobile.getElementCoordinate(iconLogin).get("leftX") + 100;
		int yLoginItem = mobile.getElementCoordinate(iconLogin).get("lowerY") + 120;
		mobile.type(xLoginItem, yLoginItem);
		mobile.sleep(15);
		
		Assert.assertFalse(mobile.checkElementExist(btnSignIn, 30));
	}

	@Then("^main article should has a picture/video$")
	public void main_article_should_has_a_picture_video() {
		driver.pressKeyCode(AndroidKeyCode.BACK);
		mobile.sleep(10);
		if(mobile.checkElementExistByScroll(imgTopstory, 3, DEFAULTTIMEOUT)) {
			linkResource = mobile.findElement(imgTopstory).getAttribute("name");
		}
		
		By topStoryContentIcon = By.xpath("//*[@content-desc='TOP STORIES']/../following-sibling::android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View");
		topStoryContent = mobile.findElement(topStoryContentIcon).getAttribute("name");
	}
	

	@When("^User click on the main article$")
	public void user_click_on_the_main_article() {	    
		mobile.click(imgTopstory);
	}

	@Then("^the page has been navigated to the main article$")
	public void the_page_has_been_navigated_to_the_main_article() {
		mobile.sleep(10);
		By topStoryContentItem = By.xpath("//*[@content-desc='" + topStoryContent + "']");
		Assert.assertTrue(mobile.checkElementExist(topStoryContentItem , DEFAULTTIMEOUT));
	}

	@Then("^the picture/video is present in the article\\.$")
	public void the_picture_video_is_present_in_the_article() {
		
		By sourceImg = By.xpath("//android.widget.Image[@content-desc='" + linkResource + "']");
		Assert.assertTrue(mobile.checkElementExistByScroll(sourceImg, 5, DEFAULTTIMEOUT));
	}
	
	@After
	public void afterScenario() {
	  mobile.quitDriver();
	}
}
