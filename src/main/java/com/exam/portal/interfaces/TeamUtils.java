package com.exam.portal.interfaces;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Massage;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;

import java.util.ArrayList;

public interface TeamUtils {
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
    public ArrayList<Team> findStudentTeamsById(String Id);
    public ArrayList<Team> findTeacherTeamsById(String Id);
    public ArrayList<Student> getStudentsByTeamId(String Id);
    public boolean makeAdmin(String teamId,String teacherId);
    public Team findTeamById(String id);
    public boolean addMassage(Massage newMassage);
    public ArrayList<String> getTeamMassage(String teamId);
}
