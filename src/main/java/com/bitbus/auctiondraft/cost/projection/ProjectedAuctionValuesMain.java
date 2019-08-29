package com.bitbus.auctiondraft.cost.projection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.bitbus.auctiondraft.AuctionDraftApplication;
import com.bitbus.auctiondraft.Profiles;
import com.bitbus.auctiondraft.cost.actual.AverageAuctionValue;
import com.bitbus.auctiondraft.cost.actual.AverageAuctionValueRepository;
import com.bitbus.auctiondraft.league.League;
import com.bitbus.auctiondraft.league.LeagueRepository;
import com.bitbus.auctiondraft.player.ExclusionReason;
import com.bitbus.auctiondraft.player.Position;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
@Slf4j
public class ProjectedAuctionValuesMain {

    private static final long LEAGUE_ID = 1;
    private static final BigDecimal MAX_COST_INCREASE = BigDecimal.TEN;

    @Autowired
    private LeagueRepository leagueRepo;

    @Autowired
    private ProjectedAuctionValueRepository projAuctionValueRepo;

    @Autowired
    private AverageAuctionValueRepository avgAuctionValueRepo;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProjectedAuctionValuesMain.class) //
                .profiles(Profiles.DEV) //
                .web(WebApplicationType.NONE) //
                .build() //
                .run(args) //
                .getBean(ProjectedAuctionValuesMain.class) //
                .doWork();
    }

    private void doWork() {
        log.info("Looking up League...");
        League league = leagueRepo.findById(LEAGUE_ID).get();

        log.info("Fetching all the Yahoo average auction values");
        List<AverageAuctionValue> yahooAverageAVs = avgAuctionValueRepo.findByPlatform(league.getPlatform());
        Collections.sort(yahooAverageAVs, (av1, av2) -> av2.getAverageCost().compareTo(av1.getAverageCost()));
        log.info("Found {} Yahoo average auction values", yahooAverageAVs.size());

        int maxDraftablePlayers = league.getNumTeams() * league.getRosterSize();
        log.info("Determining the {} draftable players", maxDraftablePlayers);
        List<AverageAuctionValue> draftablePlayers = new ArrayList<>();
        int kCount = 0;
        int defCount = 0;
        for (AverageAuctionValue av : yahooAverageAVs) {
            ExclusionReason exclusionReason = av.getPlayer().getExclusionReason();
            if (exclusionReason != null) {
                continue;
            }
            Position position = av.getPlayer().getPosition();
            if (position == Position.K) {
                if (kCount < league.getNumTeams()) {
                    draftablePlayers.add(av);
                    kCount++;
                }
            } else if (position == Position.DEF) {
                if (defCount < league.getNumTeams()) {
                    draftablePlayers.add(av);
                    defCount++;
                }
            } else if (draftablePlayers.size() < (maxDraftablePlayers - (league.getNumTeams() - kCount)
                    - (league.getNumTeams() - defCount))) {
                draftablePlayers.add(av);
            }
            if (draftablePlayers.size() == maxDraftablePlayers) {
                break;
            }
        }
        log.info("Found {} draftable players", draftablePlayers.size());

        log.info("Performing checks against the draftable players");
        assertEnoughByPosition(draftablePlayers, Position.QB, league.getNumTeams());
        assertEnoughByPosition(draftablePlayers, Position.RB, league.getNumTeams() * 3);
        assertEnoughByPosition(draftablePlayers, Position.WR, league.getNumTeams() * 3);
        assertEnoughByPosition(draftablePlayers, Position.TE, league.getNumTeams());
        assertEnoughByPosition(draftablePlayers, Position.K, league.getNumTeams());
        assertEnoughByPosition(draftablePlayers, Position.DEF, league.getNumTeams());

        log.info("Evaluating the total money allocated to these draftable players");
        BigDecimal actualTotalMoney = draftablePlayers.stream() //
                .map(av -> av.getAverageCost()) //
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalMoney = new BigDecimal(league.getAuctionBudget() * league.getNumTeams());
        log.info("Actual total money is {}, expected total money is {}", actualTotalMoney, expectedTotalMoney);

        log.info("Scaling costs...");
        BigDecimal scaleFactor = expectedTotalMoney.divide(actualTotalMoney, 4, RoundingMode.HALF_UP);
        log.info("Scale Factor: " + scaleFactor);

        List<ProjectedAuctionValue> projAVs = new ArrayList<>();
        int totalProjectedCost = 0;
        for (AverageAuctionValue draftablePlayer : draftablePlayers) {
            int projectedCost;
            if (draftablePlayer.getPlayer().getPosition().isPositional()) {
                projectedCost = draftablePlayer.getAverageCost().multiply(scaleFactor) //
                        .min(draftablePlayer.getAverageCost().add(MAX_COST_INCREASE)) //
                        .setScale(0, RoundingMode.HALF_UP) //
                        .intValue();
            } else {
                projectedCost = 1;
            }
            log.info("{} original cost of {} is updated to {}", draftablePlayer.getPlayer().getName(),
                    draftablePlayer.getAverageCost(), projectedCost);
            ProjectedAuctionValue projAV = new ProjectedAuctionValue();
            projAV.setLeague(league);
            projAV.setPlayer(draftablePlayer.getPlayer());
            projAV.setCost(projectedCost);
            projAVs.add(projAV);
            totalProjectedCost += projectedCost;
        }
        log.info("New actual money cost is {}", totalProjectedCost);

        log.info("Saving projected Auction Values");
        int saveCount = projAuctionValueRepo.saveAll(projAVs).size();
        log.info("Saved {} projected auction values", saveCount);

    }

    private void assertEnoughByPosition(List<AverageAuctionValue> avs, Position position, int minRequired) {
        long count = avs.stream().filter(av -> av.getPlayer().getPosition() == position).count();
        if (count < minRequired) {
            throw new RuntimeException("Not enough players of position " + position + ", only found " + count);
        }
    }

}
