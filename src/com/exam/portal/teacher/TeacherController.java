package com.exam.portal.teacher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private Label lblTName;

    @FXML
    private Hyperlink lblTMail;

    @FXML
    private Label lblTPhone;

    @FXML
    private Label lblTId;

    @FXML
    private Button btnTeams;

    @FXML
    private Button btnExams;

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnComplain; @FXML
    private Button btnEdit;

    @FXML
    void complainClicked(ActionEvent event) {
        System.out.println("complain");

    }

    @FXML
    void coursesClicked(ActionEvent event) {
        System.out.println("courses");
    }

    @FXML
    void examsClicked(ActionEvent event) {
        System.out.println("exam");
    }

    @FXML
    void teamsClicked(ActionEvent event) {
        System.out.println("teams");
    }

    @FXML
    private TextField newName;

    @FXML
    private TextField newMail;

    @FXML
    private TextField newNo;

    @FXML
    private TextField newId;

    @FXML
    private PasswordField newPass;

    @FXML
    private Button btnEditSubmit;
    @FXML
    void EditSubmited(ActionEvent event) {

    }
}
