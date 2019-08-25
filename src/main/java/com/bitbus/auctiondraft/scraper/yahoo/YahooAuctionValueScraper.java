package com.bitbus.auctiondraft.scraper.yahoo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.scraper.DataScraper;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile(Profiles.SELENIUM)
@Slf4j
public class YahooAuctionValueScraper implements DataScraper {

    @Autowired
    private WebDriver driver;

    @Override
    public void scrape() {
        log.info("Beginning the Yahoo Auction Value scrape");
        driver.get("https://football.fantasysports.yahoo.com/f1/draftanalysis");

        log.debug("Navigating to the Auction Values");
        driver.findElement(By.linkText("Auction Drafts")).click();

        int page = 0;
        boolean done = false;
        while (!done) {
            page++;
            log.debug("Beginning player scrape on page {}", page);

            List<WebElement> tableRowElements = driver //
                    .findElement(By.id("draftanalysistable")) //
                    .findElement(By.tagName("tbody")) //
                    .findElements(By.tagName("tr"));
            int rowNum = 0;
            for (WebElement row : tableRowElements) {
                rowNum++;
                log.trace("Scraping player data from row {}", rowNum);

                String playerName = row.findElement(By.className("name")).getText();
                log.info("Found player: {}", playerName);
            }

            log.debug("Checking if there are more pages");
            List<WebElement> next50Elements = driver.findElements(By.linkText("Next 50"));
            done = next50Elements.size() == 0;
            log.debug("Done getting player data? {}", done);
            if (!done) {
                log.debug("Not done getting player data, moving to the next page");
                next50Elements.get(0).click();
            }

        }

    }

}
