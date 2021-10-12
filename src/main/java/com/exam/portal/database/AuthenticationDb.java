package com.exam.portal.database;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

import java.sql.*;
import java.util.ArrayList;

public class AuthenticationDb {
    private final Connection connection;

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
            return false;
        }
    }

    public boolean register(Student student) {
        PreparedStatement preparedStatement = null;
        // connection=null;
        String query = "INSERT INTO Student values(?,?,?,?,?)";
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
        String query="SELECT PASSWORD FROM STUDENT WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,student.getEmail());
            rs=preparedStatement.executeQuery();
            if(rs.next()){
                String checkPass=rs.getString(1);
                return checkPass.equals(student.getPassword());
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
        String query="SELECT PASSWORD FROM TEACHER WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,teacher.getEmail());
            rs=preparedStatement.executeQuery();
            if(rs.next()){
                String checkPass=rs.getString(1);
                return checkPass.equals(teacher.getPassword());
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Teacher findTeacherByEmail(String email){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT * FROM TEACHER WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            if (rs.next()){
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getString(1));
                teacher.setName(rs.getString(2));
                teacher.setEmail(rs.getString(3));
                teacher.setContactNo(rs.getString(4));
                teacher.setPassword("");
                return teacher;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Student findStudentByEmail(String email){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        String query="SELECT * FROM Student WHERE EMAIL=?";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            rs=preparedStatement.executeQuery();
            if (rs.next()){
                Student student = new Student();
                student.setStudentId(rs.getString(1));
                student.setName(rs.getString(2));
                student.setEmail(rs.getString(3));
                student.setContactNo(rs.getString(4));
                student.setPassword("");
                return student;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public ArrayList<Student> searchStudent(String prefix){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        ArrayList<Student> students;
        String query="SELECT * FROM Student WHERE Student_Id=?% OR Name=?% OR Email=?%";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,prefix);
            preparedStatement.setString(2,prefix);
            preparedStatement.setString(3,prefix);
            rs=preparedStatement.executeQuery();
            students = new ArrayList<>();
            while (rs.next()){
                Student student = new Student();
                student.setStudentId(rs.getString(1));
                student.setName(rs.getString(2));
                student.setEmail(rs.getString(3));
                student.setContactNo(rs.getString(4));
                student.setPassword("");
                students.add(student);
            }
            return students;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Teacher> searchTeacher(String prefix){
        ResultSet rs;
        PreparedStatement preparedStatement=null;
        ArrayList<Teacher> teachers;
        String query="SELECT * FROM Teacher WHERE Teacher_Id=?% OR Name=?% OR Email=?%";
        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,prefix);
            preparedStatement.setString(2,prefix);
            preparedStatement.setString(3,prefix);
            rs=preparedStatement.executeQuery();
            teachers= new ArrayList<>();
            while (rs.next()){
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getString(1));
                teacher.setName(rs.getString(2));
                teacher.setEmail(rs.getString(3));
                teacher.setContactNo(rs.getString(4));
                teacher.setPassword("");
                teachers.add(teacher);
            }
            return teachers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}