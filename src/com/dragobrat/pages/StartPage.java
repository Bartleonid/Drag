package com.dragobrat.pages;

import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.dragobrat.common.WebDriverUtility;

public class StartPage extends WebDriverUtility {

	WebDriver driver;

	public StartPage(WebDriver driver) {
		this.driver = driver;
	}

	By searchField = By.xpath("//input[@id='search_city']");
	String dragobrat = "Драгобрат";
	By sunday = By.xpath("//a[contains(text(), 'Воскресенье')]");
	String expectedDragobrat = "в Драгобрате";
	By titleStrong = By.xpath("//strong[contains(text(), '" + expectedDragobrat + "')]");

	public void findDragobrat() throws InterruptedException {
		type(driver, searchField, dragobrat);
		pressEnter(driver, searchField);
		compareElementsByText(driver, titleStrong, expectedDragobrat);
		click(driver, sunday);
	}

	By pNight = By.xpath("//div[@id='bd7c']//tbody/tr[5]/td[@class='p1 bR ']");
	By pMorning = By.xpath("//div[@id='bd7c']//tbody/tr[5]/td[@class='p2 bR ']");
	By pDay = By.xpath("//div[@id='bd7c']//tbody/tr[5]/td[@class='p3 bR ']");
	By pEevening = By.xpath("//div[@id='bd7c']//tbody/tr[5]/td[@class='p4  ']");

	public void getPressureValues() throws InterruptedException {

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
