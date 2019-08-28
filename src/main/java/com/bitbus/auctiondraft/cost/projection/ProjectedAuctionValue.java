package com.bitbus.auctiondraft.cost.projection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bitbus.auctiondraft.league.League;
import com.bitbus.auctiondraft.player.Player;

import lombok.Data;

@Entity
@Table(name = "projected_auction_value",
        indexes = {@Index(name = "uidx_league_player", columnList = "league_id, player_id", unique = true)})
@Data
public class ProjectedAuctionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_value_id")
    private long id;

    @Column(nullable = false)
    private int cost;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

}
