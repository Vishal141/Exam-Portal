package com.exam.portal.teams;

import com.exam.portal.models.Message;
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

import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;


import static com.exam.portal.student.StudentController.student;

public class SelectTeamStudentController implements Initializable {
    public static Team currentTeam;
    @FXML
    private Label lblTeamName;
    @FXML
    private ListView<Label> listViewofMassages;

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

    private ArrayList<Message> returnedMassages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTeamName.setText(currentTeam.getName());
        loadCurrentMassages();
    }
    private void loadCurrentMassages(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try{
                        //System.out.println("Thread is doing something");
                        fetchMassages();
                        Thread.sleep(5000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void fetchMassages(){
        Platform.runLater(()->{
            Server server = new ServerHandler().getInstance();
            returnedMassages = server.getMassages(currentTeam.getTeamId());
            if(returnedMassages==null)
                returnedMassages=new ArrayList<>();
            setMassages();
        });
    }

    private  void setMassages(){
        listViewofMassages.getItems().clear();
        for( Message message:returnedMassages){
            Label label=new Label();
            String msg = message.getSenderName() + " : " + message.getMessage();
            label.setText(msg);
            label.autosize();
            label.setMaxWidth(500);
            label.setStyle("-fx-background-color: DarkSlateGrey");
            label.setStyle("-fx-border-color: black");
            listViewofMassages.getItems().add(label);

        }
    }
    @FXML
    void send(ActionEvent event) {
        if (tfMassage.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("warning");
            alert.setContentText("massage can't empty");
            alert.showAndWait();
        } else {
            Message newMassage = new Message();
            newMassage.setMessage(generateId());
            newMassage.setTeamId(currentTeam.getTeamId());
            newMassage.setSenderId(student.getStudentId());
            newMassage.setMessage(tfMassage.getText());
            newMassage.setDate(new Timestamp(new Date().getTime()));

            Server server = ServerHandler.getInstance();
        }
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
    private String generateId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

    @FXML
    void checkExam(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }

}



