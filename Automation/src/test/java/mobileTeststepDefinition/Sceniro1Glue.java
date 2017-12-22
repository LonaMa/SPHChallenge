package mobileTeststepDefinition;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import frame.MobileDriver;


public class Sceniro1Glue {

	MobileDriver mobile = new MobileDriver();
	private static final Logger LOGGERERR = LogManager.getLogger();
	private static final int DEFAULTTIMEOUT = 20;
	private final By titleBar = By.id("navbar");
	private final By iconLogin = By.id("google_ads_iframe_/5908/st/lb1/homepage_0");

	
	@Before
	public void initConnection() {
			try {
				mobile.init("ce12160c19e8a42905", "Android", "7.0", "tvn.breaking.news.straitstimes.com", ".MainActivity", "");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGERERR.error("Cannot init connection to the phone");
			}

	}
	@Given("^User is on the Login Page$")
	public void user_is_on_the_Login_Page() {
		int xTitle = mobile.getElementCoordinate(titleBar).get("rightX") - 5;
		int yTitle = mobile.getElementCoordinate(titleBar).get("lowerY") - 5;
		mobile.type(titleBar, xTitle, yTitle);
		
		int xLoginIcon = mobile.getElementCoordinate(iconLogin).get("rightX") - 5;
		int yLoginIcon = mobile.getElementCoordinate(iconLogin).get("lowerY") - 5;
		mobile.type(iconLogin, xLoginIcon, yLoginIcon);
		
		int xLoginItem = mobile.getElementCoordinate(iconLogin).get("leftX") + 5;
		int yLoginItem = mobile.getElementCoordinate(iconLogin).get("lowerY") + 15;
		mobile.type(iconLogin, xLoginItem, yLoginItem);
	}

	@When("^User enter \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_enter_and(String arg1, String arg2) {

	}

	@When("^User click the login button$")
	public void user_click_the_login_button() {

	}

	@Then("^User should login successfully$")
	public void user_should_login_successfully() {

	}

	@Then("^main article should has a picture/video$")
	public void main_article_should_has_a_picture_video() {
		
		

	}
	

	@When("^User click on the main article$")
	public void user_click_on_the_main_article() {	    

	}

	@Then("^the page has been navigated to the main article$")
	public void the_page_has_been_navigated_to_the_main_article() {
	    
	}

	@Then("^the picture/video is present in the article\\.$")
	public void the_picture_video_is_present_in_the_article() {
		
	    
	}
	
//	@After
//	public void afterScenario() {
//	  web.quitDriver();
//	}
}
