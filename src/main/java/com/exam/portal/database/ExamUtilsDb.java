package com.exam.portal.database;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Option;
import com.exam.portal.entities.Question;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class ExamUtilsDb {
    private final Connection connection;

    public ExamUtilsDb(){
        connection = DatabaseConfig.getConnection();
    }

    public boolean createExam(Exam exam){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Exams VALUES (?,?,?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,exam.getExamId());
            preparedStatement.setString(2,exam.getTeamId());
            preparedStatement.setString(3,exam.getCreatorId());
            preparedStatement.setString(4,exam.getTitle());
            preparedStatement.setDate(5,exam.getExamDate());
            preparedStatement.setTime(6,exam.getTime());
            preparedStatement.setInt(7,Integer.parseInt(exam.getDuration()));
            preparedStatement.execute();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean addQuestions(ArrayList<Question> questions){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Questions VALUES(?,?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            for(Question question:questions){
                preparedStatement.setString(1, question.getExamId());
                preparedStatement.setString(2,question.getQuestionId());
                preparedStatement.setDouble(5,question.getPoint());
                preparedStatement.setDouble(6,question.getNegPoint());
                preparedStatement.setString(7,question.getAnsIndices());
                preparedStatement.setBoolean(4,question.isImage());
                if(question.isImage()){
                    byte[] bytes = decodeStringToImage(question.getFile());
                    preparedStatement.setString(3,uploadFile(bytes));
                }
                else
                    preparedStatement.setString(3,question.getQuestion());
                preparedStatement.execute();
                uploadOptions(question.getOptions());
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean uploadOptions(ArrayList<Option> options){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO Options VALUES(?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            for(Option option:options){
                preparedStatement.setString(1,option.getExamId());
                preparedStatement.setString(2,option.getQuestionId());
                preparedStatement.setString(3,option.getIndex());
                preparedStatement.setBoolean(4,option.isImage());
                if(option.isImage()){
                    byte[] bytes = decodeStringToImage(option.getFile());
                    preparedStatement.setString(5,uploadFile(bytes));
                }
                else
                    preparedStatement.setString(5,option.getText());
                preparedStatement.execute();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //uploading files.
    public String uploadFile(byte[] file){
        PreparedStatement preparedStatement=null;
        String query = "INSERT INTO files VALUES(?,?)";
        try{
            String id = generateId();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,id);
            Blob blob = new SerialBlob(file);
            preparedStatement.setBlob(2,blob);
            preparedStatement.execute();
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String generateId(){
        return UUID.randomUUID().toString();
    }

    //decoding string in base64 to byte array.
    private byte[] decodeStringToImage(String encodedFile){
        return Base64.getDecoder().decode(encodedFile);
    }
}
