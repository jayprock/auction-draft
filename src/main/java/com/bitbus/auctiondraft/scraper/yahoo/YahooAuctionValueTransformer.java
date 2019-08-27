package com.bitbus.auctiondraft.scraper.yahoo;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.cost.actual.AverageAuctionValue;
import com.bitbus.auctiondraft.league.Platform;
import com.bitbus.auctiondraft.player.Player;
import com.bitbus.auctiondraft.player.PlayerRepository;
import com.bitbus.auctiondraft.scraper.Transformer;

@Component
@Profile(Profiles.SELENIUM)
public class YahooAuctionValueTransformer implements Transformer<YahooAuctionPlayer, AverageAuctionValue> {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private YahooPlayerTransformer playerTransformer;

    @Override
    public AverageAuctionValue transform(YahooAuctionPlayer yahooAuctionPlayer) {
        AverageAuctionValue auctionValue = new AverageAuctionValue();
        auctionValue.setPlatform(Platform.YAHOO);
        String auctionValueDigits = yahooAuctionPlayer.getActualValue().replace("$", "").replace("-", "0");
        auctionValue.setAverageCost(new BigDecimal(auctionValueDigits));
        Player player = playerTransformer.transform(yahooAuctionPlayer);
        player = playerRepo.findByNameAndTeamAndPosition(player.getName(), player.getTeam(), player.getPosition());
        auctionValue.setPlayer(player);
        return auctionValue;
    }

}
