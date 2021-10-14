package com.exam.portal.exams;

import com.exam.portal.models.Option;
import com.exam.portal.models.Question;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class AddQuestionController implements Initializable {
    @FXML
    JFXTextArea questionText;
    @FXML
    JFXToggleButton isImage;
    @FXML
    JFXListView<Label> options;
    @FXML
    JFXTextField answers;
    @FXML
    JFXTextField point;
    @FXML
    JFXTextField negPoint;
    @FXML
    JFXButton chooseFileBtn;

    private static Question question;

    private Stack<Option> stack;
    private File file;

    private volatile boolean isThreadRunning;
    private volatile int optionCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stack = new Stack<>();
        question = new Question();
        file = null;
        isThreadRunning = false;
        optionCount =0;
        runThread();
    }

    public void addOption(ActionEvent actionEvent) {
        try{
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("addOption.fxml"));
            stage.setTitle("Add Option");
            stage.setScene(new Scene(parent,650,450));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void undoOption(ActionEvent actionEvent) {
        Option option = question.undoOption();
        if(option != null)
            stack.push(option);
    }

    public void redoOption(ActionEvent actionEvent) {
        if(!stack.isEmpty()){
            Option option = stack.pop();
            question.addOption(option);
        }
    }

    public void Done(ActionEvent actionEvent) {
        boolean flag;
        if(point.getText().equals("") || negPoint.getText().equals("") ||
                answers.getText().equals("") || question.getOptionCount()==0){
            flag = false;
            showAlert("point and answer filling is mandatory.");
        }else{
            if(isImage.isSelected()){
                if(file==null){
                    flag = false;
                    showAlert("Question image is not selected.");
                }else{
                    flag = true;
                    question.setImage(true);
                    question.setFile(file);
                }
            }else{
                if(questionText.getText().equals("")){
                    flag = false;
                    showAlert("Question text is not entered.");
                }else{
                    flag = true;
                    question.setImage(false);
                    question.setQuestion(questionText.getText());
                }
            }
        }

        if(flag){

            stopThread();
            Stage stage = (Stage) questionText.getScene().getWindow();
            stage.close();
        }
    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        file = chooser.showOpenDialog(questionText.getScene().getWindow());
        if(file!=null){
            chooseFileBtn.setText(file.getAbsolutePath());
        }
    }

    public static void addOption(Option option){
        question.addOption(option);
    }

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }

    //thread which update option list.
    private void runThread(){
        isThreadRunning = true;
        Platform.runLater(()->{
            while(isThreadRunning){
                if(question.getOptionCount() != optionCount){
                    Option option = question.getLatestOptionAdded();
                    Label label = new Label();
                    if(option.isImage())
                        label.setText(option.getFile().getAbsolutePath());
                    else
                        label.setText(option.getText());
                    options.getItems().add(label);
                    optionCount = question.getOptionCount();
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void stopThread(){
        isThreadRunning = false;
    }
}
