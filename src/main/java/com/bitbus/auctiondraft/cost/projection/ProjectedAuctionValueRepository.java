package com.bitbus.auctiondraft.cost.projection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitbus.auctiondraft.league.League;

public interface ProjectedAuctionValueRepository extends JpaRepository<ProjectedAuctionValue, Long> {

    List<ProjectedAuctionValue> findByLeague(League league);
}
