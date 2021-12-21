package com.exam.portal.teams;

import com.exam.portal.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Stack;

public class SelectTeamStudentController {
    public static Team currentTeam;
    @FXML
    Label lblTeamName;

    @FXML
    Button btnShowStudents;

    @FXML
    Button btnSheduleExam;

    @FXML
    Button btnGoBcak;

    @FXML
    TextField tfMassage;

    @FXML
    Button btnSend;

    @FXML
    void checkExam(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

    @FXML
    void send(ActionEvent event) {

    }

    @FXML
    void showAllStudents(ActionEvent event) {
        try{
            Stage stage = (Stage) btnSend.getScene().getWindow();
            ShowAllStudents.fromTeacher = false;
            Parent root = FXMLLoader.load(getClass().getResource("showAllStudent.fxml"));
            stage.setTitle("All students");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
