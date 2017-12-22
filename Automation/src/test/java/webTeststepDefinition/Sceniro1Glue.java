package webTeststepDefinition;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.*;
import frame.MyWebDriver;


public class Sceniro1Glue {

	MyWebDriver web = new MyWebDriver();
	private static final Logger LOGGERERR = LogManager.getLogger();
	private static final int DEFAULTTIMEOUT = 30;
	private static final int SHORTWAIT = 2;
	private final By iframePopup = By.xpath("//iframe[contains(@id,'expand_iframe')]");
	private final By videoAdPopup = By.xpath("//div[contains(@id,'innity_adslot')]/a[contains(@id,'btn_close')]");
	private final By btnPopClose = By.id("close-button");
	private final By linkLogin = By.xpath("//li/a[text()='Login']");
	private final By txtUsername = By.id("j_username");
	private final By txtPassword = By.id("j_password");
	private final By btnSignIn = By.xpath("//button[text()='Sign in!']");
	private final By linkLogout = By.xpath("//div[@class='block-user-menu']//a[text()='Logout']");
	private final By linkTopStory = By.xpath("//div[contains(@class,'main-featured-story stt-top1')]//div[contains(@class,'views-row-first')]/a");
	private final By topStoryImg = By.xpath("//div[contains(@class,'main-featured-story stt-top1')]//div[@class='content']/div/img");
	private final By topStoryImgParent = By.xpath("//div[contains(@class,'main-featured-story stt-top1')]//div[@class='content']/..");
	private final By topStoryVideo = By.xpath("//div[contains(@class,'main-featured-story stt-top1')]//div[@class='content']/../h2/a");
	
	private String linkTopStoryContent;
	private String linkResource;
	private Boolean topStoryImgExist = false;
	private Boolean topStoryVideoExist = false;
	private String strVideoId;
	private String videoTitle;
	
	
	@Given("^User is on the Login Page$")
	public void user_is_on_the_Login_Page() {
		web.navigateTo("http://www.straitstimes.com");
		
		
		if(web.checkElementExist(iframePopup, DEFAULTTIMEOUT)) {
			web.switchFrame(web.findElement(iframePopup));
			web.click(btnPopClose);
		}
		if(web.checkElementExist(videoAdPopup, DEFAULTTIMEOUT)) {
			web.click(videoAdPopup);
			web.sleep(SHORTWAIT);
		}
		
		web.click(linkLogin);
		
		boolean findLoginInBtn = web.checkElementExist(btnSignIn, DEFAULTTIMEOUT);
		assertTrue(findLoginInBtn);
	}

	@When("^User enter \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_enter_and(String arg1, String arg2) {
	    web.sendKeys(txtUsername, arg1);
	    web.sendKeys(txtPassword, arg2);
	}

	@When("^User click the login button$")
	public void user_click_the_login_button() {
	    web.click(btnSignIn);
	}

	@Then("^User should login successfully$")
	public void user_should_login_successfully() {
//		if(web.checkElementExist(iframePopup, DEFAULTTIMEOUT)) {
//			web.switchFrame(web.webObject(iframePopup));
//			web.click(btnPopClose);
//		}
		web.sleep(SHORTWAIT);
		boolean findLogout = web.checkElementExist(linkLogout, DEFAULTTIMEOUT);
		assertTrue(findLogout);	
	}

	@Then("^main article should has a picture/video$")
	public void main_article_should_has_a_picture_video() {
		
		if(web.checkElementExist(topStoryImg, DEFAULTTIMEOUT)) {
			if(web.findElement(topStoryImgParent).getAttribute("class").contains("video")) {
				topStoryVideoExist = true;
				String href = web.findElement(topStoryVideo).getAttribute("href");
				videoTitle = web.findElement(topStoryVideo).getText();
				strVideoId = href.substring(href.lastIndexOf("/")+1);
				LOGGERERR.info("Get Video id is:" + strVideoId);
			} else {
				String srcOrg = web.findElement(topStoryImg).getAttribute("src");
				LOGGERERR.info("Find org img resource is:" + srcOrg);
				linkResource = srcOrg.substring(srcOrg.indexOf("public/")+7, srcOrg.indexOf("?"));
				LOGGERERR.info("Find img resource is:" + linkResource);
				topStoryImgExist = true;
			}
		}
		
		assertTrue(topStoryImgExist || topStoryVideoExist);	
	}
	

	@When("^User click on the main article$")
	public void user_click_on_the_main_article() {	    
	    linkTopStoryContent = web.findElement(linkTopStory).getAttribute("href");
	    web.click(linkTopStory);
	}

	@Then("^the page has been navigated to the main article$")
	public void the_page_has_been_navigated_to_the_main_article() {
	    boolean findUrlCorrect = web.getCurrentUrl().contains(linkTopStoryContent);
	    Assert.assertTrue("Go to the " + findUrlCorrect + " url.", findUrlCorrect);
	}

	@Then("^the picture/video is present in the article\\.$")
	public void the_picture_video_is_present_in_the_article() {
		Boolean checkExist = false;
		By objectEle = null;
		
		if(topStoryImgExist) {
			objectEle = By.xpath("//img[contains(@srcset,'" + linkResource + "')]");
			checkExist = web.checkElementExist(objectEle, DEFAULTTIMEOUT);

			Assert.assertTrue(checkExist);
			
			Boolean imgPresent = web.checkImgPresent(objectEle);
		    Assert.assertTrue("Picture in the article present is:"+ imgPresent, imgPresent );
		}
		
		if(topStoryVideoExist) {
			
			By videoFrame = By.xpath("//iframe[contains(@src,'" + strVideoId + "') or contains(@title,'" + videoTitle + "')]");
			web.switchFrame(web.findElement(videoFrame));
			By video = By.xpath("//*[text()='"+videoTitle+"']/../../../..//video");
			LOGGERERR.info(web.findElement(video).getAttribute("data-video-id"));
			if(web.findElement(video).getAttribute("data-video-id")!=null){
				objectEle = By.xpath("//video[@data-video-id='" + strVideoId + "']");
			} else {
				objectEle = video;
			}
			checkExist = web.checkElementExist(objectEle, DEFAULTTIMEOUT);

			Assert.assertTrue("Video exist in article is:" + checkExist, checkExist);
		}
	    
	    
	}
	
	@After
	public void afterScenario() {
	  web.quitDriver();
	}
}
