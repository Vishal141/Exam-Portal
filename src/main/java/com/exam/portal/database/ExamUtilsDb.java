package com.exam.portal.database;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;

import java.sql.Connection;
import java.util.ArrayList;

public class ExamUtilsDb {
    private final Connection connection;

    public ExamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean createExam(Exam exam){
        return true;
    }

    public boolean addQuestions(String examId, ArrayList<Question> questions){
        return true;
    }
}
