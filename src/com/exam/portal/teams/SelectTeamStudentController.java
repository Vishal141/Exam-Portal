package com.exam.portal.teams;

import com.exam.portal.models.Massage;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.exam.portal.teacher.TeacherController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;


import static com.exam.portal.student.StudentController.student;

public class SelectTeamStudentController implements Initializable {
    public static Team team;
    @FXML
    private Label lblTeamName;
    @FXML
    private ListView<Label> listViewofMassages;

    @FXML
    private Button btnShowStudents;

    @FXML
    private Button btnSheduleExam;

    @FXML
    private Button btnGoBcak;

    @FXML
    private TextField tfMassage;

    @FXML
    private Button btnSend;

    @FXML
    void checkExam(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTeamName.setText(team.getName());
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
    private ArrayList<String> returnedMassages;
    private void fetchMassages(){
        Platform.runLater(()->{
            Server server = new ServerHandler().getInstance();
            returnedMassages = server.getMassages(team.getTeamId());
            if(returnedMassages==null)
                returnedMassages=new ArrayList<>();
            setMassages();
        });
    }
    private  void setMassages(){
        listViewofMassages.getItems().clear();
        for( String massage:returnedMassages){
            Label label=new Label();
            label.setText(massage);
            label.autosize();
            label.setMaxWidth(500);
            Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
            label.setFont(font);
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
            Massage newMassage = new Massage();
            newMassage.setMassage(generateId());
            newMassage.setTeamId(team.getTeamId());
            newMassage.setSenderId(student.getStudentId());
            newMassage.setMassage(tfMassage.getText());
            newMassage.setDate(String.valueOf(new Date()));

            Server server = ServerHandler.getInstance();
        }
    }
    @FXML
    void showAllStudents(ActionEvent event) {

    }
    private String generateId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

}



