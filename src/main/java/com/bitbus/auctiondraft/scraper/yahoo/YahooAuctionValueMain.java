package com.bitbus.auctiondraft.scraper.yahoo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.bitbus.auctiondraft.AuctionDraftApplication;
import com.bitbus.auctiondraft.Profiles;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
public class YahooAuctionValueMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(YahooAuctionValueMain.class) //
                .profiles(Profiles.SELENIUM, Profiles.DEV) //
                .web(WebApplicationType.NONE) //
                .build() //
                .run(args) //
                .getBean(YahooAuctionValueScraper.class) //
                .scrape();
    }

}
