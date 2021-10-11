package com.exam.portal.database;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Team;

import java.sql.Connection;

public class TeamUtilsDb {
    private final Connection connection;

    public TeamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean createTeam(Team team){
        return true;
    }

    public boolean addStudent(BelongTo belongTo){
        return true;
    }
}
