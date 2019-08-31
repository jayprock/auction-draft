package com.bitbus.auctiondraft.cost.projection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitbus.auctiondraft.league.League;

@RequestMapping("api/auction-values/projections")
@RestController
public class ProjectedAuctionValuesController {

    @Autowired
    private ProjectedAuctionValueRepository projAVRepo;

    @GetMapping("{leagueId}")
    List<ProjectedAuctionValue> getProjectionsForLeague(@PathVariable long leagueId) {
        return projAVRepo.findByLeague(new League(leagueId));
    }

}
