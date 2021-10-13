package com.exam.portal.teacher;

import com.exam.portal.models.Teacher;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    public static Teacher teacher;

    @FXML
     Label lblTName;

    @FXML
     Hyperlink lblTMail;

    @FXML
     Label lblTPhone;

    @FXML
     Label lblTId;

    @FXML
     Button btnTeams;

    @FXML
     Button btnExams;

    @FXML
     Button btnCourses;

    @FXML
     Button btnComplain; @FXML
     Button btnEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(lblTId != null){
            lblTName.setText(teacher.getName());
            lblTMail.setText(teacher.getEmail());
            lblTPhone.setText(teacher.getContactNo());
            lblTId.setText(teacher.getTeacherId());
        }else{
            newName.setText(teacher.getName());
            newId.setText(teacher.getTeacherId());
            newMail.setText(teacher.getEmail());
            newNo.setText(teacher.getContactNo());
        }
    }

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

   // Edit details
    @FXML
    TextField newName;

    @FXML
     TextField newMail;

    @FXML
     TextField newNo;

    @FXML
     TextField newId;

    @FXML
     PasswordField newPass;

    @FXML
     Button btnEditSubmit;
    @FXML
    void EditSubmitted(ActionEvent event) {
        if(newMail.getText().equals("") || newName.getText().equals("") || newNo.getText().equals("")){

        }else{
            Teacher teacher1 = new Teacher();
            teacher1.setTeacherId(teacher.getTeacherId());
            teacher1.setName(newName.getText());
            teacher1.setEmail(newMail.getText());
            teacher1.setContactNo(newNo.getText());
            teacher1.setPassword(newPass.getText());

            Server server = ServerHandler.getInstance();
            if(server.updateTeacher(teacher1)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Update Successful");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Update Failed");
                alert.setContentText("Check you password or email.");
                alert.showAndWait();
            }
        }
    }

    public void editDetails(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("Edit.fxml"));
            stage.setTitle("Edit Details");
            stage.setScene(new Scene(parent,500,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gotoCreateTeam(ActionEvent actionEvent) {
    }

    public void gotoConductExam(ActionEvent actionEvent) {
    }

    public void gotoAddStudent(ActionEvent actionEvent) {
    }
}
