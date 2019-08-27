package com.bitbus.auctiondraft.team;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepo;

    @Test
    public void testFindByAbbreviation() {
        Optional<Team> teamOpt = teamRepo.findByAbbreviation("WAS");
        Team team = teamOpt.get();
        Assert.assertEquals("Redskins", team.getName());
        Assert.assertEquals("Washington", team.getCity());
    }
}
