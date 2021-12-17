package com.exam.portal.teams;

import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

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

    //sending request to server for joining new team with team id.
    public void joinTeam(ActionEvent event) {
    }

}
