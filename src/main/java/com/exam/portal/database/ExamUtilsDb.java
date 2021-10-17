package com.exam.portal.database;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Option;
import com.exam.portal.entities.Question;

import javax.sql.rowset.serial.SerialBlob;

import java.io.IOException;
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

    public ArrayList<Exam> getExamsScheduledBy(String teacherId){
        PreparedStatement preparedStatement=null;
        ResultSet rs;
        String query = "SELECT * FROM Exams WHERE Creator_Id=?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,teacherId);
            rs = preparedStatement.executeQuery();
            ArrayList<Exam> exams = new ArrayList<>();
            while(rs.next()){
                Exam exam = new Exam();
                exam.setExamId(rs.getString(1));
                exam.setTeamId(rs.getString(2));
                exam.setCreatorId(rs.getString(3));
                exam.setTitle(rs.getString(4));
                exam.setExamDate(rs.getDate(5));
                exam.setTime(rs.getTime(6));
                exam.setDuration(rs.getInt(7)+"");

                exams.add(exam);
            }
            return exams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Exam getExamById(String examId){
        try {
            String query = "SELECT * FROM Exams WHERE Exam_Id=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                System.out.println(examId);
                Exam exam = new Exam();
                exam.setExamId(rs.getString(1));
                exam.setTeamId(rs.getString(2));
                exam.setCreatorId(rs.getString(3));
                exam.setTitle(rs.getString(4));
                exam.setExamDate(rs.getDate(5));
                exam.setTime(rs.getTime(6));
                exam.setDuration(rs.getInt(7)+"");

                exam.setQuestions(getExamQuestions(examId));

                return exam;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Question> getExamQuestions(String examId){
        try {
            String query = "SELECT * FROM Questions WHERE Exam_Id=? ORDER  BY Q_Id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Question> questions = new ArrayList<>();
            while(rs.next()){
                Question question = new Question();
                question.setExamId(rs.getString(1));
                question.setQuestionId(rs.getString(1));
                question.setIsImage(rs.getBoolean(4));
                if(question.isImage())
                    question.setQuestion(getImage(rs.getString(3)));
                else
                    question.setQuestion(rs.getString(3));
                question.setPoint(rs.getInt(5));
                question.setNegPoint(rs.getInt(6));
                question.setAnsIndices(rs.getString(7));
                question.setOptions(getOptionsOf(examId,question.getQuestionId()));

                questions.add(question);
            }

            return questions;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Option> getOptionsOf(String examId,String questionId){
        try {
            String query = "SELECT * FROM Options WHERE Exam_Id=? AND Q_Id=? ORDER BY O_Id";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,examId);
            preparedStatement.setString(2,questionId);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Option> options = new ArrayList<>();
            while (rs.next()){
                Option option = new Option();
                option.setExamId(rs.getString(1));
                option.setQuestionId(rs.getString(2));
                option.setIndex(rs.getString(3));
                option.setIsImage(rs.getBoolean(4));
                if(option.isImage())
                    option.setFile(getImage(rs.getString(5)));
                else
                    option.setText(rs.getString(5));

                options.add(option);
            }

            return options;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getImage(String fileId){
        try {
            String query = "SELECT data FROM files WHERE file_Id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fileId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return encodeImageToString(rs.getBlob(1));
            }
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
    private String encodeImageToString(Blob blob){
        try {
            return Base64.getEncoder().encodeToString(blob.getBinaryStream().readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
