package com.bitbus.auctiondraft.player;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bitbus.auctiondraft.team.Team;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepo;

    @Test
    public void testFindByNameAndTeamAndPosition() {
        Player kylerMurray = playerRepo.findByNameAndTeamAndPosition("Kyler Murray", new Team(1), Position.QB);
        assertEquals("Kyler Murray", kylerMurray.getName());
    }
}
