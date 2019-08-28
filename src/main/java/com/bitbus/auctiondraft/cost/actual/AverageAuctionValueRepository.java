package com.bitbus.auctiondraft.cost.actual;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitbus.auctiondraft.league.Platform;

public interface AverageAuctionValueRepository extends JpaRepository<AverageAuctionValue, Long> {

    long deleteByPlatform(Platform platform);

    List<AverageAuctionValue> findByPlatform(Platform platform);

}
