package com.bitbus.auctiondraft.scraper.yahoo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

import com.bitbus.auctiondraft.AuctionDraftApplication;
import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.cost.actual.AverageAuctionValue;
import com.bitbus.auctiondraft.cost.actual.AverageAuctionValueRepository;
import com.bitbus.auctiondraft.league.Platform;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
@Profile(Profiles.SELENIUM)
@Slf4j
public class YahooAuctionValueMain {

    @Autowired
    private YahooAuctionValueScraper scraper;

    @Autowired
    private YahooAuctionValueTransformer transformer;

    @Autowired
    private AverageAuctionValueRepository avgAuctionValueRepo;

    public static void main(String[] args) {
        new SpringApplicationBuilder(YahooAuctionValueMain.class) //
                .profiles(Profiles.SELENIUM) //
                .web(WebApplicationType.NONE) //
                .build() //
                .run(args) //
                .getBean(YahooAuctionValueMain.class) //
                .performETL();
    }

    public void performETL() {
        log.info("Calling Yahoo auction value scraper");
        List<YahooAuctionPlayer> yahooAuctionPlayers = scraper.scrape();

        log.info("Converting Yahoo auction players into AverageAuctionValue entities");
        List<AverageAuctionValue> auctionValues = yahooAuctionPlayers.stream() //
                .map(yahooAuctionPlayer -> transformer.transform(yahooAuctionPlayer)) //
                .collect(Collectors.toList());

        log.info("Deleting all existing auction values");
        long deleted = avgAuctionValueRepo.deleteByPlatform(Platform.YAHOO);
        log.debug("Deleted {} auction values", deleted);

        log.info("Saving the new Auction Values");
        int saveCount = avgAuctionValueRepo.saveAll(auctionValues).size();

        log.info("Done performing Auction Value ETL. Save {} auction values", saveCount);


    }

}
