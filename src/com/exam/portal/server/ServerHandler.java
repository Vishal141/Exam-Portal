package com.exam.portal.server;

import com.exam.portal.models.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ServerHandler implements Server{
    private static final String AUTHENTICATION_URL = "/authentication";
    private static final String TEAM_URL = "/team-utilities";
    private static final  String EXAM_URL = "/exam-utilities";

    private static final String SUCCESSFUL = "SUCCESSFUL";

    private static Gson gson;

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
            HttpURLConnection connection = ServerConfig.getConnection(url);
            String json = gson.toJson(student);
            assert connection != null;
            writeJson(connection,json);

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
            HttpURLConnection connection = ServerConfig.getConnection(url);
            String json = gson.toJson(teacher);
            assert connection != null;
            writeJson(connection,json);

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
            String url = AUTHENTICATION_URL + "/student/singUp";
            HttpURLConnection connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(student);
            writeJson(connection,json);

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
            String url = AUTHENTICATION_URL + "/teacher/singUp";
            HttpURLConnection connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(teacher);
            writeJson(connection,json);

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
            HttpURLConnection connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(team);
            writeJson(connection,json);

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
            HttpURLConnection connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(belongTo);
            writeJson(connection,json);

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
            HttpURLConnection connection = ServerConfig.getConnection(url);
            assert connection != null;
            String json = gson.toJson(exam);
            writeJson(connection,json);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            return response.equals(SUCCESSFUL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    //method for writing object in request body
    private void writeJson(HttpURLConnection connection,String json){
        try {
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
