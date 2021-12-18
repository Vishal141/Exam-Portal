package com.exam.portal.database;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Massage;
import com.exam.portal.entities.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class TeamUtilsDb {
    private final Connection connection;

    public TeamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    //creates a new team in the database and returns true if successfully created otherwise returns false.
    public boolean createTeam(Team team){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Teams values(?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, team.getTeamId());
            preparedStatement.setString(2, team.getName());
            preparedStatement.setString(3, team.getCreatorId());
            preparedStatement.setDate(4,team.getDateCreated());
            preparedStatement.execute();
            return makeAdminOf(team.getCreatorId(),team.getTeamId());    //making creator admin as well.
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //makes teacher with teacherId a admin of team with teamId.
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

    //adding student in the team , belongTo contains details of student and teams both.
    public boolean addStudent(BelongTo belongTo){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO BelongTo values(?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, belongTo.getStudentId());
            preparedStatement.setString(2, belongTo.getTeamId());
            preparedStatement.setDate(3,belongTo.getDate());
            preparedStatement.execute();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //returns all the teams in which student with Id included or teacher with Id is a admin.
    //relationName is the name of table , for student's team it is belongTo and for teacher's team it is adminOf.
    public ArrayList<Team> findTeamsById(String Id,String relationName,String relationId){
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        ArrayList<Team> teams;
        String query = "SELECT * FROM TEAMS WHERE TEAMS.Team_Id IN (SELECT Team_Id FROM "+relationName+" WHERE "+relationId+"=?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Id);
            rs = preparedStatement.executeQuery();
            teams = new ArrayList<>();
            while(rs.next()){
                Team team = new Team();
                team.setTeamId(rs.getString(1));
                team.setCreatorId(rs.getString(3));
                team.setName(rs.getString(2));
                team.setDateCreated(rs.getDate(4));
                teams.add(team);
            }
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //return a team having id same as teamId.
    public Team findTeamById(String teamId){
        PreparedStatement preparedStatement=null;
        try {
            String query = "SELECT * FROM Teams WHERE Team_Id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Team team = new Team();
                team.setTeamId(teamId);
                team.setCreatorId(rs.getString(3));
                team.setName(rs.getString(2));
                team.setDateCreated(rs.getDate(4));

                return team;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // join a team with team id for student

    public boolean joinTeamWithId(String teamId,String studentId){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO BelongTo values(?,?,?)  where Team_Id=teamId ";
        try{
            Team requiredTeam=findTeamById(teamId);
            if(requiredTeam!=null){
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, studentId);
                preparedStatement.setString(2, teamId);
                preparedStatement.setDate(3, (java.sql.Date) new Date());
                preparedStatement.execute();
                return true;

            }
            }catch (Exception e){
            e.printStackTrace();
        }

       return false;
    }
  public boolean addMassage(Massage newMassage){
      PreparedStatement preparedStatement=null;
      String query = "INSERT INTO Massages values(?,?,?,?,?)";
      try{
          preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, newMassage.getMassageId());
          preparedStatement.setString(2, newMassage.getTeamId());
          preparedStatement.setString(3, newMassage.getSenderId());
          preparedStatement.setString(4, newMassage.getMassage());
          preparedStatement.setDate(5,   newMassage.getDate());
          preparedStatement.execute();
          return true;
      }catch(Exception e){
          e.printStackTrace();
      }

      return false;
  }
  public  ArrayList<String> getTeamMassage(String teamId){
        PreparedStatement preparedStatement=null;
        PreparedStatement preparedStatement1=null;
        ArrayList<String> massages=new ArrayList<>();
      try{
            String query="SELECT * FROM MASSAGES WHERE TEAM_ID=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teamId);
            ResultSet rs=preparedStatement.executeQuery();
            // rsname=null;
            String queryname="SELECT NAME FROM ? WHERE ?_Id=?";
            if(rs.next()){
               String massage=rs.getString(4);
               String storeId=rs.getString(3);
               if(storeId!=null && storeId.charAt(0)=='T'){
                   preparedStatement1=connection.prepareStatement(queryname);
                   preparedStatement1.setString(1,"teacher");
                   preparedStatement1.setString(2,"Teacher");
                   preparedStatement1.setString(3,storeId);
                   ResultSet rsname= preparedStatement1.executeQuery();
                   if(rsname.next())
                       massage+="By "+rsname.getString(1);

               }
                if(storeId!=null && storeId.charAt(0)=='S'){
                    preparedStatement1=connection.prepareStatement(queryname);
                    preparedStatement1.setString(1,"student");
                    preparedStatement1.setString(2,"Student");
                    preparedStatement1.setString(3,storeId);
                    ResultSet rsname= preparedStatement1.executeQuery();
                    if(rsname.next())
                        massage+="By "+rsname.getString(1);

                }
                massage+=rs.getString(5);
                massages.add(massage);
            }
            return massages;

        }catch (Exception e){
            e.printStackTrace();}
        return massages;
  }

}
