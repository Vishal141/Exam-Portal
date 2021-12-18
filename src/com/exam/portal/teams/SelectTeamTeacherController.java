package com.exam.portal.teams;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.Massage;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class SelectTeamTeacherController implements Initializable {
    public static Team team;

    @FXML
    Label lblTeamName;

    @FXML
    Button btnShowStudents;

    @FXML
    Button btnAddStudent;

    @FXML
    Button btnScheduledExam;

    @FXML
    Button btnGoBack;

    @FXML
    TextField tfMassage;

    @FXML
    Button btnSend;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTeamName.setText(team.getName());
    }

    @FXML
    void addStudent(ActionEvent event) {
        String path = "addStudent.fxml";
        changeStage(path,"Add Student",600,650);
    }

    @FXML
    void goBack(ActionEvent event) {
        String path = "teams.fxml";
        changeStage(path,"Teams",700,600);
    }



    @FXML
    void send(ActionEvent event) {
        if(tfMassage.getText().equals("")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("warning");
            alert.setContentText("massage can't empty");
            alert.showAndWait();
        }else{
            Massage newMassage=new Massage();
            newMassage.setMassage(generateId());
            newMassage.setTeamId(team.getTeamId());
            newMassage.setSenderId(TeacherController.teacher.getTeacherId());
            newMassage.setMassage(tfMassage.getText());
            newMassage.setDate(String.valueOf(new Date()));

            Server server = ServerHandler.getInstance();
            if(server.sendMassage(newMassage))
            {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Status");
                alert.setContentText("massage sent succesfully");
                alert.showAndWait();
            }
            else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Status");
                alert.setContentText("Some problem occurred");
                alert.showAndWait();
            }
        }
       }
    private String generateId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

    @FXML
    void scheduledExam(ActionEvent event) {
        String path = "../exams/scheduled/ScheduledExam.fxml";
        ScheduledExam.fromTeacher = true;
        changeStage(path,"Scheduled Exams",700,600);
    }

    @FXML
    void showAllStudents(ActionEvent event) {

    }

    public void changeStage(String path,String title,int width,int height){
        try{
            Stage stage = (Stage) lblTeamName.getScene().getWindow();
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
