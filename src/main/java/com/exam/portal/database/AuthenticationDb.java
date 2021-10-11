package com.exam.portal.database;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class AuthenticationDb {
    private Connection connection;

    public AuthenticationDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean register(Teacher teacher){
        PreparedStatement preparedStatement=null;
       // connection=null;
String query="INSERT INTO Teacher values(?,?,?,?,?)";
try{
    preparedStatement=connection.prepareStatement(query);
    preparedStatement.setString(1,teacher.getTeacherId());
    preparedStatement.setString(2,teacher.getName());
    preparedStatement.setString(3,teacher.getEmail());
    preparedStatement.setString(4,teacher.getContactNo());
    preparedStatement.setString(5,teacher.getPassword());
    preparedStatement.execute();
    return true;

}
catch (Exception e) {
    e.printStackTrace();
    return false;}
    }

    public boolean register(Student student) {
        PreparedStatement preparedStatement = null;
        // connection=null;
        String query = "INSERT INTO Teacher values(?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getStudentId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getContactNo());
            preparedStatement.setString(5, student.getPassword());
            preparedStatement.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 public boolean login(Student student){
 ResultSet rs;
 PreparedStatement preparedStatement=null;
 String query="SELECT PASSWORD FROM STUDENT WHERE STUDENTID=?";
 try{
     preparedStatement=connection.prepareStatement(query);
     preparedStatement.setString(1,student.getStudentId());
     rs=preparedStatement.executeQuery();
     if(rs.next()){
         String checkPass=rs.getString(1);
         if(checkPass.equals(student.getPassword()))return true;
         else
             return  false;

     }

 }catch (Exception e){
     e.printStackTrace();
     return false;
 }
return false;
 }

    public boolean login(Teacher teacher){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT PASSWORD FROM TEACHER WHERE TEACHERID=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,teacher.getPassword());
            rs=preparedStatement.executeQuery();
            if(rs.next()){
                String checkPass=rs.getString(1);
                if(checkPass.equals(teacher.getPassword()))return true;
                else
                    return  false;

            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }



}
