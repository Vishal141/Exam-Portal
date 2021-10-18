package com.exam.portal.exams.scheduled;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExamItem {
    @FXML
    Label title;
    @FXML
    Label date;
    @FXML
    Label teamName;

    private Exam exam;
    private Team team;

    public void setTitle(String text){
        title.setText(text);
    }

    public void setDate(String text){
        date.setText(text);
    }

    public void setTeamName(String text){
        teamName.setText(text);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
