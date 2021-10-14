package com.exam.portal.teams;

import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class teamsController implements Initializable {
    @FXML
    JFXListView<Label> teamList;

    @FXML
    Label lblCandidateName;

    @FXML
    Label lblCandidateMail;

    @FXML
    Button btnManageTeams;

    @FXML
    Button btnJoinWId;

    @FXML
    Button btnCreateNewTeam;

    @FXML
    Button btnBack;

    private ArrayList<Team> teams;

    int r,g,b;
    double op;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblCandidateMail.setText(TeacherController.teacher.getEmail());
        lblCandidateName.setText(TeacherController.teacher.getName());
        teamList.setExpanded(true);
        fetchTeams();
        if(teams==null){
            teams = new ArrayList<>();
            Team t1 = new Team();
            t1.setName("Softablitz");
            Team t2 = new Team();
            t2.setName("Webster");
            teams.add(t1);
            teams.add(t2);
        }
        setListView();

        teamList.setOnMouseClicked(this::teamListItemClicked);
    }

    public void createNewTeam(ActionEvent actionEvent) {
        String path = "CreateTeam.fxml";
        changeStage(path,"Create Team",400,500);
    }

    public void goBack(ActionEvent actionEvent) {
        String path = "../teacher/TeacherDashboard.fxml";
        changeStage(path,"Dashboard",700,500);
    }

    public void changeStage(String path,String title,int width,int height){
        try {
            Stage stage;
            if(title.equals("Create Team"))
                stage = new Stage();
            else
                stage = (Stage) btnBack.getScene().getWindow();
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //fetching all teams associated with teacher from server
    private void fetchTeams(){
        Server server = ServerHandler.getInstance();
        teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
    }

    private void setListView(){
        for(Team team:teams){
            Label label = new Label();
            label.setText(team.getName());
            Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
            label.setFont(font);
            label.setTextFill(Color.WHITE);
            teamList.getItems().add(label);
        }
    }

    private void generateRandomColor(){
        Random random = new Random();
        int c = random.nextInt();
        r = c&255;
        g = (c>>>8)&255;
        b = (c>>>16)&255;
        op = (c>>>24)/255.0;
    }

    private void teamListItemClicked(MouseEvent mouseEvent) {

    }
}
