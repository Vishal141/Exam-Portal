package com.exam.portal.database;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TeamUtilsDb {
    private final Connection connection;

    public TeamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean createTeam(Team team){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Team values(?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, team.getTeamId());
            preparedStatement.setString(2, team.getName());
            preparedStatement.setString(3, team.getCreatorId());
            java.sql.Date sqlDate=new java.sql.Date(team.getDateCreated().getTime());
            preparedStatement.setDate(4,sqlDate );
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addStudent(BelongTo belongTo){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO BelongTo values(?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, belongTo.getStudentId());
            preparedStatement.setString(2, belongTo.getTeamId());
            java.sql.Date sqlDate=new java.sql.Date(belongTo.getDate().getTime());
            preparedStatement.setDate(3,sqlDate );
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
