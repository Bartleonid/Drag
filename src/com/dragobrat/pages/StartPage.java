package com.dragobrat.pages;

import java.util.ArrayList;
import java.util.Iterator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.dragobrat.common.WebDriverUtility;

public class StartPage extends WebDriverUtility {

	WebDriver driver;

	public StartPage(WebDriver driver) {
		this.driver = driver;
	}

	By searchField = By.xpath("//input[@id='search_city']");
	
	public void getPressureByCity(String city, String day) throws InterruptedException {
		type(driver, searchField, city);
		pressEnter(driver, searchField);
		By titleStrong = By.xpath("//strong[contains(text(), 'в " + city + "')]");
		isTestElementPresent(driver, titleStrong);
		By dayElem = By.xpath("//*[@class='day-link'][contains(text(), '"+ day +"')]");
		click(driver, dayElem);
		String baseLocatorPressure = "//*[@class='infoDayweek'][contains(text(), '"+ day +"')]/../../..//*[@class='weatherDetails']//tr[5]";
		
		By pNight = By.xpath(baseLocatorPressure + "/td[1]");
		By pMorning = By.xpath(baseLocatorPressure + "/td[2]");
		By pDay = By.xpath(baseLocatorPressure + "/td[3]");
		By pEevening = By.xpath(baseLocatorPressure + "/td[4]");

		ArrayList<Integer> pressureList = new ArrayList<>();
		pressureList.add(Integer.valueOf(getText(driver, pNight)));
		pressureList.add(Integer.valueOf(getText(driver, pMorning)));
		pressureList.add(Integer.valueOf(getText(driver, pDay)));
		pressureList.add(Integer.valueOf(getText(driver, pEevening)));

		Iterator<Integer> iter = pressureList.iterator();
		while (iter.hasNext()) {
			Integer pressure = iter.next().intValue();
			if (pressure >= 600 && pressure <= 700)
				System.out.println("Pressure OK");
			else if (pressure < 600)
				Assert.fail("Pressure is Less than 600");
			else if (pressure > 700)
				Assert.fail("Pressure is More than 700");
			else
				Assert.fail("Pressure values ares incorrect");
		}

	}

}
