package com.exam.portal.teams;

import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentTeamsController implements Initializable {
    @FXML
    Label studentName;
    @FXML
    JFXListView<Label> teamList;

    private Server server;
    private ArrayList<Team> teams;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        server = ServerHandler.getInstance();
        studentName.setText(StudentController.student.getName());
        fetchTeams();
        setListener();
    }

    //fetching student teams from server.
    private void fetchTeams(){
        Platform.runLater(()->{
            teams = server.getStudentsTeams(StudentController.student.getStudentId());
            if(teams==null)
                teams = new ArrayList<>();
            setList();
        });
    }

    //adding all teams in listview
    private void setList(){
        for(Team team:teams){
            Label label = new Label();         //creating custom label and adding in listview.
            label.setText(team.getName());
            Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
            label.setFont(font);
            label.setTextFill(Color.WHITE);
            teamList.getItems().add(label);
        }
    }

    //setting listener to list
    private void setListener(){
        teamList.setOnMouseClicked(mouseEvent -> {
            SelectTeamStudentController.currentTeam=teams.get(teamList.getSelectionModel().getSelectedIndex());  //setting team in controller.
            Stage stage = (Stage) teamList.getScene().getWindow();
            try{
                Parent root = FXMLLoader.load(getClass().getResource("selectTeamStudent.fxml"));
                stage.setScene(new Scene(root,700,700));
                stage.setTitle(SelectTeamStudentController.currentTeam.getName());
                stage.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    //opening another window for entering team id for joining new team.
    @FXML
    public void joinTeam(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../student/joinWithId.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Join Team");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../student/studentDashboard.fxml"));
            Stage stage = (Stage) teamList.getScene().getWindow();
            stage.setTitle("Exam Portal");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
