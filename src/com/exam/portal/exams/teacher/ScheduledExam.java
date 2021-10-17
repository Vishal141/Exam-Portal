package com.exam.portal.exams.teacher;

import com.exam.portal.models.Exam;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ScheduledExam implements Initializable {
    @FXML
    VBox vBox;

    private ArrayList<Exam> exams;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchExams();
        addExamsToVBox(exams);
    }

    private void fetchExams(){
        Server server = ServerHandler.getInstance();
        exams = server.getExamScheduledBy(TeacherController.teacher.getTeacherId());
        if(exams == null)
            exams = new ArrayList<>();
    }

    private void addExamsToVBox(ArrayList<Exam> examArrayList){
        try{
           for(Exam exam:examArrayList) {
               FXMLLoader loader = FXMLLoader.load(getClass().getResource("ExamItem.fxml"));
               Node node = loader.load();
               ExamItem examItem = loader.getController();
               examItem.setTitle(exam.getTitle());
               examItem.setDate(exam.getExamDate());
               vBox.getChildren().add(node);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void allExams(ActionEvent actionEvent) {
        vBox.getChildren().removeAll();
        addExamsToVBox(exams);
    }

    public void upcomingExams(ActionEvent actionEvent) {
        vBox.getChildren().removeAll();
        try{
            ArrayList<Exam> upcomingExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date examDate = dateFormat.parse(exam.getExamDate());
                if(after(examDate,exam.getTime()))
                    upcomingExam.add(exam);
            }
            addExamsToVBox(upcomingExam);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void archivedExams(ActionEvent actionEvent) {
        vBox.getChildren().removeAll();
        try{
            ArrayList<Exam> archivedExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date examDate = dateFormat.parse(exam.getExamDate());
                if(!after(examDate,exam.getTime()))
                    archivedExam.add(exam);
            }
            addExamsToVBox(archivedExam);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean after(Date examDate,String time){
        Date date = new Date();
        if(date.after(examDate))
            return true;
        if(date.before(examDate))
            return false;
        int h = Integer.parseInt(time.substring(0,1));
        int m = Integer.parseInt(time.substring(3,4));
        if(date.getHours()>h)
            return true;
        if(date.getDate()<h)
            return false;
        return date.getMinutes()>m;
    }
}
