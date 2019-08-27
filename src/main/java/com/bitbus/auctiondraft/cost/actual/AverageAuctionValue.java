package com.bitbus.auctiondraft.cost.actual;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bitbus.auctiondraft.league.Platform;
import com.bitbus.auctiondraft.player.Player;

import lombok.Data;

@Entity
@Table(name = "average_auction_value",
        indexes = {@Index(name = "uidx_player_platform", columnList = "player_id, platform", unique = true)})
@Data
public class AverageAuctionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "average_auction_value_id")
    private long id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal averageCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

}
