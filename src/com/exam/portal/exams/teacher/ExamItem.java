package com.exam.portal.exams.teacher;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExamItem {
    @FXML
    Label title;
    @FXML
    Label date;

    public void setTitle(String text){
        title.setText(text);
    }

    public void setDate(String text){
        date.setText(text);
    }
}
