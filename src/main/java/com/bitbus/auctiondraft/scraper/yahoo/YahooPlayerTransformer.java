package com.bitbus.auctiondraft.scraper.yahoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.player.Player;
import com.bitbus.auctiondraft.player.Position;
import com.bitbus.auctiondraft.scraper.Transformer;
import com.bitbus.auctiondraft.team.Team;
import com.bitbus.auctiondraft.team.TeamRepository;

@Component
@Profile(Profiles.SELENIUM)
public class YahooPlayerTransformer implements Transformer<YahooAuctionPlayer, Player> {

    @Autowired
    private TeamRepository teamRepo;

    @Override
    public Player transform(YahooAuctionPlayer yahooPlayer) {
        Player player = new Player();
        player.setName(yahooPlayer.getName());
        String[] playerDetails = yahooPlayer.getDetails().split(" - ");
        String teamAbbr = playerDetails[0].toUpperCase();
        Team team = teamRepo.findByAbbreviation(teamAbbr)
                .orElseThrow(() -> new RuntimeException("No team found for abbr: " + teamAbbr));
        player.setTeam(team);
        player.setPosition(Position.valueOf(playerDetails[1].toUpperCase()));
        return player;
    }

}
