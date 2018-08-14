package com.dragobrat.test;

import java.net.MalformedURLException;

import org.testng.annotations.Test;

import com.dragobrat.common.WebDriverUtility;
import com.dragobrat.pages.StartPage;

public class VerifyGetPressureTest extends WebDriverUtility {

	@Test(invocationCount = 1)
	public void verifyPressureWithinLimits() throws MalformedURLException, InterruptedException {
		start();
		
		StartPage startPage = new StartPage(driver);
		startPage.getPressureByCity("Драгобрат", "Суббота");

		driver.quit();
	}

}
