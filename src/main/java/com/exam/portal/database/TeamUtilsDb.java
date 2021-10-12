package com.exam.portal.database;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Teacher;
import com.exam.portal.entities.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
            preparedStatement.execute();
            return makeAdminOf(team.getCreatorId(),team.getTeamId());
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean makeAdminOf(String teacherId,String teamId){
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO ADMIN_OF VALUES(?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            preparedStatement.setString(2,teacherId);
            preparedStatement.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
            preparedStatement.execute();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Team> findTeamsById(String Id,String relationName,String relationId){
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        ArrayList<Team> teams;
        String query = "SELECT * FROM TEAMS WHERE TEAMS.Team_Id IN (SELECT Team_Id FROM ? WHERE ?=?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,relationName);
            preparedStatement.setString(2,relationId);
            preparedStatement.setString(3,Id);
            rs = preparedStatement.executeQuery();
            teams = new ArrayList<>();
            while(rs.next()){
                Team team = new Team();
                team.setTeamId(rs.getString(1));
                team.setCreatorId(rs.getString(2));
                team.setName(rs.getString(3));
                team.setDateCreated(rs.getDate(4));
                teams.add(team);
            }
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
