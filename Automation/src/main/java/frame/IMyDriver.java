package frame;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface IMyDriver {

	public WebDriver getDriver();
	public WebElement findElement(By by);
	public void click(By by);
	public void sendKeys(By by, CharSequence... keysToSend);
	public boolean checkElementExist(By by, int timeout);
	public void quitDriver();
}
