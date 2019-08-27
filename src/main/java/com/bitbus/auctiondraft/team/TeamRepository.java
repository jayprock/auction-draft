package com.bitbus.auctiondraft.team;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    Optional<Team> findByAbbreviation(String abbreviation);

}
