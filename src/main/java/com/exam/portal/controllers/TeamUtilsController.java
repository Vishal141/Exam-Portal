package com.exam.portal.controllers;

import com.exam.portal.entities.BelongTo;

import com.exam.portal.entities.Team;
import com.exam.portal.interfaces.TeamUtils;
import com.exam.portal.services.TeamUtilsService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exam-portal/team-utilities")
public class TeamUtilsController {
    private static final String SUCCESSFUL = "SUCCESSFUL";
    private static final String FAILED = "FAILED";

    private final TeamUtils teamUtils;

    public TeamUtilsController(){
        teamUtils = new TeamUtilsService();
    }

    @RequestMapping("/create-team")
    public String createTeam(@RequestBody Team team){
        if(teamUtils.createTeam(team))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/add-student")
    public String addStudent(@RequestBody BelongTo belongTo){
        if(teamUtils.addStudent(belongTo))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/get/all/student?id={Id}")
    public ArrayList<Team> getStudentTeams(@PathVariable String Id){
        return teamUtils.findStudentTeamsById(Id);
    }

    @RequestMapping("/get/all/teacher?id={Id}")
    public ArrayList<Team> getTeacherTeams(@PathVariable String Id){
        return teamUtils.findTeacherTeamsById(Id);
    }

    @RequestMapping("/teacher/make-admin?team-id={teamId}&teacher-id={teacherId}")
    public String makeAdmin(@PathVariable String teacherId, @PathVariable String teamId){
        if(teamUtils.makeAdmin(teamId,teacherId))
            return SUCCESSFUL;
        return FAILED;
    }
}
