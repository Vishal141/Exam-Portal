package com.exam.portal.student;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.Student;
import com.exam.portal.models.Teacher;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    public static Student student;

    @FXML
    Label lblName;
    @FXML
    Hyperlink lblMail;
    @FXML
    Label lblPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(lblMail != null){
            lblMail.setText(student.getEmail());
            lblName.setText(student.getName());
            lblPhone.setText(student.getContactNo());
        }else{
            newName.setText(student.getName());
            newMail.setText(student.getEmail());
            newNo.setText(student.getContactNo());
        }
    }

    public void editDetails(ActionEvent actionEvent) {
        String path = "edit.fxml";
        changeStage(path,"Edit Details",500,500);
    }

    public void teamsClicked(ActionEvent actionEvent) {
    }

    public void examsClicked(ActionEvent actionEvent) {
        String path = "../exams/scheduled/ScheduledExam.fxml";
        ScheduledExam.fromTeacher = false;
        changeStage(path,"Scheduled Exams",600,600);
    }

    public void coursesClicked(ActionEvent actionEvent) {
    }

    public void complainClicked(ActionEvent actionEvent) {
    }

    public void gotoJoinTeam(ActionEvent actionEvent) {

    }

    public void changeStage(String path,String title,int width,int height){
        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Edit Detail Section
    @FXML
    TextField newName;
    @FXML
    TextField newMail;
    @FXML
    TextField newNo;
    @FXML
    TextField Pass;

    @FXML
    void EditSubmitted(ActionEvent event) {
        if(newMail.getText().equals("") || newName.getText().equals("") || newNo.getText().equals("") || Pass.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("All fields are mandatory.");
            alert.showAndWait();
        }else{
            Student student1 = new Student();
            student1.setStudentId(student.getStudentId());
            student1.setName(newName.getText());
            student1.setEmail(newMail.getText());
            student1.setContactNo(newNo.getText());student1.setPassword(getHash(Pass.getText()));

            Server server = ServerHandler.getInstance();
            if(server.updateStudent(student1)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Update Successful.");
                alert.showAndWait();
                Stage stage = (Stage) newName.getScene().getWindow();
                stage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Update Failed");
                alert.setContentText("Check you password or email.");
                alert.showAndWait();
            }
        }
    }

    private String getHash(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            String hash=number.toString();
            while (hash.length()<32){
                hash = "v"+hash;
            }
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
