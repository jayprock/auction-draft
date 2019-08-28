package com.bitbus.auctiondraft.cost.actual;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.bitbus.auctiondraft.league.Platform;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql("./average-auction-value-data.sql")
public class AverageAuctionValueRepositoryTest {

    @Autowired
    private AverageAuctionValueRepository avgAuctionValueRepo;

    @Test
    public void testDeleteByPlatform() {
        long totalCount = avgAuctionValueRepo.count();
        assertEquals(303, totalCount);
        long deletedCount = avgAuctionValueRepo.deleteByPlatform(Platform.FANTRAX);
        assertEquals(2, deletedCount);
        totalCount = avgAuctionValueRepo.count();
        assertEquals(301, totalCount);
    }

    @Test
    public void testFindByPlatform() {
        List<AverageAuctionValue> auctionValues = avgAuctionValueRepo.findByPlatform(Platform.ESPN);
        assertEquals(1, auctionValues.size());
    }
}
