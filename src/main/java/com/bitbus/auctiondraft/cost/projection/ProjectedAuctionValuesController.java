package com.bitbus.auctiondraft.cost.projection;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        List<ProjectedAuctionValue> avs = projAVRepo.findByLeague(new League(leagueId));
        Collections.sort(avs, (av1, av2) -> Integer.valueOf(av2.getCost()).compareTo(av1.getCost()));
        return avs;
    }

    @PutMapping
    void updateProjections(@RequestBody List<ProjectedAuctionValue> projections) {
        projAVRepo.saveAll(projections);
    }
}
