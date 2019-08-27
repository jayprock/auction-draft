package com.bitbus.auctiondraft.scraper;

public interface Transformer<T, V> {

    V transform(T t);
}
