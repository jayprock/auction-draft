package com.bitbus.auctiondraft.league;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int numTeams;

    @Column(nullable = false)
    private int rosterSize;

    @Column(nullable = false)
    private int auctionBudget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidAggression aggressiveness;

}
