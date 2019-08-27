package com.bitbus.auctiondraft.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Team {
    @Id
    @Column(name = "team_id")
    private int id;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String abbreviation;

    public Team() {}

    public Team(int id) {
        this.id = id;
    }

}
