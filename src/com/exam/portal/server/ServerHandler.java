package com.exam.portal.server;

import com.exam.portal.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler implements Server{
    private static final String AUTHENTICATION_URL = "/authentication";
    private static final String TEAM_URL = "/team-utilities";
    private static final  String EXAM_URL = "/exam-utilities";

    private static final String SUCCESSFUL = "SUCCESSFUL";

    private static Gson gson;
    private HttpURLConnection connection;

    private static ServerHandler serverHandler=null;

    private ServerHandler(){
        gson = new Gson();
    }

    public static ServerHandler getInstance(){
        if(serverHandler == null)
            serverHandler = new ServerHandler();
        return serverHandler;
    }

    @Override
    public boolean login(Student student) {
        try{
            String url = AUTHENTICATION_URL + "/student/signIn";
            connection = ServerConfig.getConnection(url);
            String json = gson.toJson(student);
            assert connection != null;
            writeJson(json);

            //reading server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();

            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean login(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/signIn";
            connection = ServerConfig.getConnection(url);
            String json = gson.toJson(teacher);
            assert connection != null;
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean register(Student student) {
        try {
            String url = AUTHENTICATION_URL + "/student/signUp";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(student);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean register(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/signUp";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(teacher);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean createTeam(Team team) {
        try {
            String url = TEAM_URL + "/create-team";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(team);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean addStudent(BelongTo belongTo) {
        try {
            String url = TEAM_URL + "/add-student";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(belongTo);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean createExam(Exam exam) {
        try {
            String url = EXAM_URL + "/create-exam";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(exam);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public Teacher getTeacher(String email) {
        try{
            String url = AUTHENTICATION_URL + "/teacher/get/"+email;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            Teacher teacher = gson.fromJson(response,Teacher.class);
            return teacher;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student getStudent(String email) {
        try{
            String url = AUTHENTICATION_URL + "/student/get?"+email;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            Student student = gson.fromJson(response,Student.class);
            return student;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Teacher> searchTeacher(String text) {
        try{
            String url = AUTHENTICATION_URL + "/teacher/search?q="+text;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Teacher> teachers = gson.fromJson(response,new TypeToken<List<Teacher>>(){}.getType());
            return teachers;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Student> searchStudent(String text) {
        try{
            String url = AUTHENTICATION_URL + "/student/search?q="+text;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Student> students = gson.fromJson(response,new TypeToken<List<Student>>(){}.getType());
            return students;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Team> getStudentsTeams(String Id) {
        try{
            String url = TEAM_URL + "/get/all/student?id="+Id;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Team> teams = gson.fromJson(response,new TypeToken<List<Team>>(){}.getType());
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Team> getTeachersTeams(String Id) {
        try{
            String url = TEAM_URL + "/get/all/teacher?id="+Id;
            connection = ServerConfig.getConnection(url);
            assert connection != null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            ArrayList<Team> teams = gson.fromJson(response,new TypeToken<List<Team>>(){}.getType());
            return teams;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        try {
            String url = AUTHENTICATION_URL + "/teacher/update";
            connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(teacher);
            writeJson(json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //method for writing object in request body
    private void writeJson(String json){
        try {
            System.out.println(json);
            connection.setFixedLengthStreamingMode(json.length());
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
