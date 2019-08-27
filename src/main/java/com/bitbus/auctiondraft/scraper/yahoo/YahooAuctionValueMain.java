package com.bitbus.auctiondraft.scraper.yahoo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

import com.bitbus.auctiondraft.AuctionDraftApplication;
import com.bitbus.auctiondraft.Profiles;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
@Profile(Profiles.SELENIUM)
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
