package com.bitbus.auctiondraft.scraper.yahoo;

import java.util.ArrayList;
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
public class YahooAuctionValueScraper implements DataScraper<List<YahooAuctionPlayer>> {

    @Autowired
    private WebDriver driver;

    @Override
    public List<YahooAuctionPlayer> scrape() {
        log.info("Beginning the Yahoo Auction Value scrape");
        driver.get("https://football.fantasysports.yahoo.com/f1/draftanalysis");

        log.debug("Navigating to the Auction Values");
        driver.findElement(By.linkText("Auction Drafts")).click();

        List<YahooAuctionPlayer> players = new ArrayList<>();
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
            boolean anyPlayerOnPageDrafted = false;
            for (WebElement row : tableRowElements) {
                rowNum++;
                log.trace("Scraping player data from row {}", rowNum);
                List<WebElement> columnElements = row.findElements(By.tagName("td"));
                WebElement playerNameEl = columnElements.get(0).findElement(By.className("name"));
                String playerName = playerNameEl.getText();
                String playerDetails = playerNameEl.findElement(By.xpath("following-sibling::span")).getText();
                String projectedValue = columnElements.get(1).findElement(By.tagName("div")).getText();
                String actualValue = columnElements.get(2).findElement(By.tagName("div")).getText();
                String percentDrafted = columnElements.get(3).findElement(By.tagName("div")).getText();
                YahooAuctionPlayer player =
                        new YahooAuctionPlayer(playerName, playerDetails, projectedValue, actualValue, percentDrafted);
                players.add(player);
                log.info("Found player: {}", player);
                anyPlayerOnPageDrafted |= !percentDrafted.equals("0%");
            }

            log.debug("Checking if there are more pages");
            List<WebElement> next50Elements = driver.findElements(By.linkText("Next 50"));
            done = !anyPlayerOnPageDrafted || next50Elements.size() == 0;
            log.debug("Done getting player data? {}", done);
            if (!done) {
                log.debug("Not done getting player data, moving to the next page");
                next50Elements.get(0).click();
            }
        }

        log.info("Done scraping Yahoo Auction Value Data, found {} players", players.size());
        return players;
    }

}
