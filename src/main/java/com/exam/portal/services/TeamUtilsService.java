package com.exam.portal.services;

import com.exam.portal.database.TeamUtilsDb;
import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Massage;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;
import com.exam.portal.interfaces.TeamUtils;

import java.util.ArrayList;

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

    @Override
    public ArrayList<Team> findStudentTeamsById(String Id) {
        return teamUtilsDb.findTeamsById(Id,"BelongTo","Student_Id");
    }

    @Override
    public ArrayList<Team> findTeacherTeamsById(String Id) {
        return teamUtilsDb.findTeamsById(Id,"Admin_Of","Admin_Id");
    }

    @Override
    public boolean makeAdmin(String teamId, String teacherId) {
        return teamUtilsDb.makeAdminOf(teacherId,teamId);
    }

    @Override
    public Team findTeamById(String id) {
        return teamUtilsDb.findTeamById(id);
    }
    @Override
    public boolean addMassage(Massage newMassage){return teamUtilsDb.addMassage(newMassage);}
    @Override
    public  ArrayList<String> getTeamMassage(String teamId){return teamUtilsDb.getTeamMassage(teamId);}


}
