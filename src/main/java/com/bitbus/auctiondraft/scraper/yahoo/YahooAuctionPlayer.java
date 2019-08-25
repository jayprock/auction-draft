package com.bitbus.auctiondraft.scraper.yahoo;

import lombok.Data;

@Data
public class YahooAuctionPlayer {

    private final String name;
    private final String details;
    private final String projectedValue;
    private final String actualValue;
    private final String percentDrafted;

}
