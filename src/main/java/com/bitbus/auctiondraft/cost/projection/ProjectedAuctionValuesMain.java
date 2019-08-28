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
import com.bitbus.auctiondraft.league.Platform;
import com.bitbus.auctiondraft.player.ExclusionReason;
import com.bitbus.auctiondraft.player.Position;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackageClasses = AuctionDraftApplication.class)
@Slf4j
public class ProjectedAuctionValuesMain {

    private static final int LEAGUE_SIZE = 12;
    private static final int AUCTION_BUDGET = 200;
    private static final int ROSTER_SIZE = 15;
    private static final Platform LEAGUE_PLATFORM = Platform.YAHOO;

    private static final int MIN_QBS = 12;
    private static final int MIN_RBS = 18;
    private static final int MIN_WRS = 18;
    private static final int MIN_TES = 12;
    private static final int MIN_DEF = 12;
    private static final int MIN_KS = 12;

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
        log.info("Fetching all the Yahoo average auction values");
        List<AverageAuctionValue> yahooAverageAVs = avgAuctionValueRepo.findByPlatform(LEAGUE_PLATFORM);

        Collections.sort(yahooAverageAVs, (av1, av2) -> av2.getAverageCost().compareTo(av1.getAverageCost()));

        int maxDraftablePlayers = LEAGUE_SIZE * ROSTER_SIZE;
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
                if (kCount < MIN_KS) {
                    draftablePlayers.add(av);
                    kCount++;
                }
            } else if (position == Position.DEF) {
                if (defCount < MIN_DEF) {
                    draftablePlayers.add(av);
                    defCount++;
                }
            } else if (draftablePlayers.size() < (maxDraftablePlayers - MIN_KS - MIN_DEF)) {
                draftablePlayers.add(av);
            }
            if (draftablePlayers.size() == maxDraftablePlayers) {
                break;
            }
        }

        log.info("Performing checks against the draftable players");
        assertEnoughByPosition(draftablePlayers, Position.QB, MIN_QBS);
        assertEnoughByPosition(draftablePlayers, Position.RB, MIN_RBS);
        assertEnoughByPosition(draftablePlayers, Position.WR, MIN_WRS);
        assertEnoughByPosition(draftablePlayers, Position.TE, MIN_TES);
        assertEnoughByPosition(draftablePlayers, Position.K, MIN_KS);
        assertEnoughByPosition(draftablePlayers, Position.DEF, MIN_DEF);

        log.info("Evaluating the total money allocated to these draftable players");
        BigDecimal actualTotalMoney = draftablePlayers.stream() //
                .map(av -> av.getAverageCost()) //
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalMoney = new BigDecimal(AUCTION_BUDGET).multiply(new BigDecimal(LEAGUE_SIZE));
        log.info("Actual total money is {}, expected total money is {}", actualTotalMoney, expectedTotalMoney);

        log.info("Scaling costs...");
        BigDecimal scaleFactor = expectedTotalMoney.divide(actualTotalMoney, 4, RoundingMode.HALF_UP);
        log.info("Scale Factor: " + scaleFactor);

        BigDecimal newActualTotalMoney = BigDecimal.ZERO;
        for (AverageAuctionValue draftablePlayer : draftablePlayers) {
            BigDecimal scaledCost =
                    draftablePlayer.getAverageCost().multiply(scaleFactor).setScale(0, RoundingMode.HALF_UP);
            log.info("{} original cost of {} is updated to {}", draftablePlayer.getPlayer().getName(),
                    draftablePlayer.getAverageCost(), scaledCost);
            draftablePlayer.setAverageCost(scaledCost);
            newActualTotalMoney = newActualTotalMoney.add(scaledCost);
        }
        log.info("New actual money cost is {}", newActualTotalMoney);
    }

    private void assertEnoughByPosition(List<AverageAuctionValue> avs, Position position, int minRequired) {
        long count = avs.stream().filter(av -> av.getPlayer().getPosition() == position).count();
        if (count < minRequired) {
            throw new RuntimeException("Not enough players of position " + position + ", only found " + count);
        }
    }

}
