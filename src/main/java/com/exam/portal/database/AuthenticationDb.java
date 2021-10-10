package com.exam.portal.database;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

import java.sql.Connection;

public class AuthenticationDb {
    private Connection connection;

    public AuthenticationDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean register(Teacher teacher){
        return true;
    }

    public boolean register(Student student){
        return true;
    }

    public boolean login(Student student){
        return true;
    }

    public boolean login(Teacher teacher){
        return true;
    }

    private String getHashValue(String name,String email){
        return "";
    }
}
