package com.bitbus.auctiondraft.player;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitbus.auctiondraft.team.Team;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByNameAndTeamAndPosition(String name, Team team, Position position);

}
