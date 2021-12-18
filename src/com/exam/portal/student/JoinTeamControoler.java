package com.exam.portal.student;

import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.exam.portal.student.StudentController.student;

public class JoinTeamControoler {

    @FXML
    private JFXButton joinWithIdClickedBtn;

    @FXML
    private TextField joinWithId;
    @FXML
    void joinWithIdClicked(ActionEvent event) {
        if(joinWithId.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);     //checking all the fields are filled or not.
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Team ID mendatory");
            alert.showAndWait();
        }else{
            Server server = ServerHandler.getInstance();
            Platform.runLater(()->{
                if(server.joinTeamWithId(joinWithId.getText(), student.getStudentId())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Added Successfully");
                    alert.showAndWait();
                    //Stage stage = (Stage) newName.getScene().getWindow();
                   // stage.close();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);     //checking all the fields are filled or not.
                    alert.setHeaderText(null);
                    alert.setTitle("Warning");
                    alert.setContentText("Team ID is invalid");
                    alert.showAndWait();}
            });
        }}
    //create a new stage and show it.
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
}
