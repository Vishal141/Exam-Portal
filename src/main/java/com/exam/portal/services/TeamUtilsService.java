package com.exam.portal.services;

import com.exam.portal.database.TeamUtilsDb;
import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;
import com.exam.portal.interfaces.TeamUtils;

public class TeamUtilsService implements TeamUtils {
    private final TeamUtilsDb teamUtilsDb;

    public TeamUtilsService(){
        teamUtilsDb = new TeamUtilsDb();
    }

    @Override
    public boolean createTeam(Team team) {
        return teamUtilsDb.createTeam(team);
    }

    @Override
    public boolean addStudent(BelongTo belongTo) {
        return teamUtilsDb.addStudent(belongTo);
    }
}
