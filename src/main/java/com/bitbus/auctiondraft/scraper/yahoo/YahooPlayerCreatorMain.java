package com.bitbus.auctiondraft.scraper.yahoo;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

import com.bitbus.auctiondraft.AuctionDraftApplication;
import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.player.Player;
import com.bitbus.auctiondraft.player.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
@Profile(Profiles.SELENIUM)
@Slf4j
public class YahooPlayerCreatorMain {

    @Autowired
    private YahooAuctionValueScraper scraper;

    @Autowired
    private YahooPlayerTransformer transformer;

    @Autowired
    private PlayerRepository playerRepo;


    public static void main(String[] args) {
        new SpringApplicationBuilder(YahooPlayerCreatorMain.class) //
                .profiles(Profiles.SELENIUM) //
                .web(WebApplicationType.NONE) //
                .build() //
                .run(args) //
                .getBean(YahooPlayerCreatorMain.class) //
                .performETL();

    }

    @Transactional
    public void performETL() {
        log.info("Calling the Yahoo player scraper");
        List<YahooAuctionPlayer> yahooPlayers = scraper.scrape();

        log.info("Converting the Yahoo players into Player entities");
        List<Player> players = yahooPlayers.stream() //
                .map(yahooPlayer -> transformer.transform(yahooPlayer)) //
                .collect(Collectors.toList());

        log.info("Deleting all existing players");
        playerRepo.deleteAll();

        log.info("Saving the new players");
        int saveCount = playerRepo.saveAll(players).size();

        log.info("Done performing player ETL. Saved {} players", saveCount);
    }

}
