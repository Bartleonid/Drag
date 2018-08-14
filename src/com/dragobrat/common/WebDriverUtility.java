package com.dragobrat.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class WebDriverUtility {

	protected WebDriver driver;

	public void start() throws MalformedURLException {

		final ChromeOptions options = new ChromeOptions();
		// options.setBinary("/usr/bin/google-chrome-stable");
		options.addArguments("--no-sandbox");
//		options.addArguments("--headless");
		options.addArguments("--no-default-browser-check"); /* #Overrides default choices */
		options.addArguments("--no-first-run");
		options.addArguments("--disable-default-apps");
		options.addArguments("start-maximized");
		options.addArguments("disable-infobars");
		options.addArguments("window-size=1920x1080");

		options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		options.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);

		driver = new ChromeDriver(options);
		driver.get("https://sinoptik.ua/");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}
	
	public final static int TIMEOUT = 300;

	protected void compareElementsByText(WebDriver driver, By locator, String expectedElement)
			throws InterruptedException {
		waitForPageLoaded(driver);
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedElement));
			String actualElement = driver.findElement(locator).getText();
			System.out.println("actualElement   " + actualElement);
			System.out.println("expectedElement   " + expectedElement);
			assertEquals(actualElement, expectedElement);
		} catch (Exception e) {
			Assert.fail("Title not found: " + expectedElement);
		}
	}
	
	protected String getText(WebDriver driver, By locator)
			throws InterruptedException {
		waitForPageLoaded(driver);
		String text = "";
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			text = driver.findElement(locator).getText();
			System.out.println("Pressure:   " + text);
		} catch (Exception e) {
			Assert.fail("Locator not found: " + locator);
		}
		return text;
	}

	

	protected static void waitForPageLoaded(WebDriver driver) throws InterruptedException {
		Thread.sleep(2000);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		for (int i = 0; i < tabs.size(); i++) {
			driver.switchTo().window(tabs.get(i));
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, TIMEOUT);
			try {
				wait.until(expectation);
			} catch (Throwable error) {
			}
		}
	}

	protected void click(WebDriver driver, By by) throws InterruptedException {
		WebElement element = waitForElement(driver, by, TIMEOUT);
		waitForPageLoaded(driver);
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			wait.until(ExpectedConditions.elementToBeClickable(by));
			action.click(element).build().perform();
			System.out.println("element is clicked  " + by);
			waitForPageLoaded(driver);
		} catch (Exception e) {
			Assert.fail("element not found: " + by);
			System.out.println("element not found");
		}
	}

	protected void pressEnter(WebDriver driver, By by) throws InterruptedException {
		WebElement element = waitForElement(driver, by, TIMEOUT);
		waitForPageLoaded(driver);
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			wait.until(ExpectedConditions.elementToBeClickable(by));
			action.sendKeys(Keys.ENTER);
			action.build().perform();
			System.out.println("element is clicked  " + by);
			waitForPageLoaded(driver);
		} catch (Exception e) {
			Assert.fail("element not found: " + by);
			System.out.println("element not found");
		}
	}

	protected void type(WebDriver driver, By by, Object text) throws InterruptedException {
		waitForElement(driver, by, TIMEOUT);
		WebElement element = driver.findElement(by);
		text = String.valueOf(text);
		// String str = text.toString();
		// CharSequence ch = str;
		System.out.println(
				"Filling a text '" + text + "' into the field '" + by + "' on page '" + driver.getCurrentUrl() + "'");
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		wait.until(ExpectedConditions.elementToBeClickable(by));
		element.clear();
		action.sendKeys(element, " ").build().perform();
		element.clear();
		element.sendKeys((String) text);
	}
	
	public static WebElement waitForElement(WebDriver driver, final By by, int timeout) {
		org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(driver, timeout);

		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

		ExpectedCondition<WebElement> expectation = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(by);
			}
		};

		try {
			return wait.until(expectation);
		} catch (Throwable error) {
			return null;
		}
	}
	
	public void isTestElementPresent(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			driver.findElement(locator);
		} catch (NoSuchElementException e) {
			Assert.assertFalse(false, "element not found");
		}
	}

}
