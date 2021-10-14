package com.exam.portal.exams;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateExamController {
    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblDuration;

    @FXML
    private Button lblAddQuestion;

    @FXML
    private Button btnFinish;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfDuration;

    @FXML
    private DatePicker tfDate;

    @FXML
    void createExam(ActionEvent event) {

    }

}
