package com.exam.portal.controllers;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;
import com.exam.portal.interfaces.TeamUtils;
import com.exam.portal.services.TeamUtilsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam-portal/team")
public class TeamUtilsController {
    private final TeamUtils teamUtils;

    public TeamUtilsController(){
        teamUtils = new TeamUtilsService();
    }

    @RequestMapping("/create-team")
    public String createTeam(@RequestBody Team team){
        if(teamUtils.createTeam(team))
            return "SUCCESSFUL";
        return "FAILED";
    }

    @RequestMapping("/add-student")
    public String addStudent(@RequestBody BelongTo belongTo){
        if(teamUtils.addStudent(belongTo))
            return "SUCCESSFUL";
        return "FAILED";
    }
}
