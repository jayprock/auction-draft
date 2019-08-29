package com.bitbus.auctiondraft.player;

public enum Position {

    QB(true), WR(true), RB(true), TE(true), K(false), DEF(false);

    boolean positional;

    private Position(boolean positional) {
        this.positional = positional;
    }

    public boolean isPositional() {
        return positional;
    }
}
