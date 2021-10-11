package com.exam.portal.database;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ExamUtilsDb {
    private final Connection connection;

    public ExamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean createExam(Exam exam){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Exam values(?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,exam.getExamId());
            preparedStatement.setString(2, exam.getTeamId());
            preparedStatement.setString(3,exam.getCreatorId());
            java.sql.Date sqlDate=new java.sql.Date(exam.getExamDate().getTime());
            preparedStatement.setDate(4,sqlDate );
            preparedStatement.setInt(5,exam.getDuration() );

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addQuestions(String examId, ArrayList<Question> questions){
        return true;
    }
}
