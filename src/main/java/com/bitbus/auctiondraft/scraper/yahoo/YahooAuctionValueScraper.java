package com.bitbus.auctiondraft.scraper.yahoo;

import org.openqa.selenium.WebDriver;
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
