package com.exam.portal.exams.teacher.createexam;

import com.exam.portal.models.Option;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class AddOptionController implements Initializable {
    @FXML
    JFXTextArea optionText;
    @FXML
    JFXToggleButton isImage;
    @FXML
    JFXButton chooseFileBtn;

    private Option option;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        option = new Option();
        file = null;
    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        file = chooser.showOpenDialog(isImage.getScene().getWindow());
        if(file != null){
            chooseFileBtn.setText(file.getAbsolutePath());
        }
    }

    public void Done(ActionEvent actionEvent) {
        boolean flag = true;
        if(isImage.isSelected()){
            if(file==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Warning");
                alert.setContentText("You haven't choose any file.");
                alert.showAndWait();
                flag = false;
            }else{
                flag = true;
                option.setIsImage(true);
                option.setText(file.getAbsolutePath());
                option.setFile(encodeImageToBase64Binary(file));
            }
        }else{
            if(optionText.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Warning");
                alert.setContentText("please enter option text.");
                alert.showAndWait();
                flag = false;
            }else{
                flag = true;
                option.setIsImage(false);
                option.setText(optionText.getText());
            }
        }

        if(flag){
            AddQuestionController.addOption(option);
            Stage stage = (Stage) isImage.getScene().getWindow();
            stage.close();
        }
    }

    public String encodeImageToBase64Binary(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            String encodedFile = Base64.getEncoder().encodeToString(fis.readAllBytes());
            return encodedFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
