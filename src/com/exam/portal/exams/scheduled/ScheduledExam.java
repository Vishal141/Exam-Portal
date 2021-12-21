package com.exam.portal.exams.scheduled;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.exam.portal.teacher.TeacherController;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ScheduledExam implements Initializable {
    private static final String QUESTION_PAPER_PATH = "../student/questionPaper.fxml";
    private static final String SUBMISSION_PATH = "../student/submissions.fxml";

    @FXML
    VBox vBox;
    @FXML
    JFXButton activeBtn;

    //variable used for determining that it is student or teacher.
    public static boolean fromTeacher;

    private ArrayList<Exam> exams;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchExams();
    }

    //fetching only details of exams (without questions).
    private void fetchExams(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            if(fromTeacher)
                exams = server.getExamScheduledBy(TeacherController.teacher.getTeacherId());
            else
                exams = server.getExamScheduledFor(StudentController.student.getStudentId());
            if(exams == null)
                exams = new ArrayList<>();
            activeBtn.fire();          //setting active exams initially.
        });
    }

    //displaying all exams.
    private void addExamsToVBox(ArrayList<Exam> examArrayList,String path){
        ExamItem.stage = (Stage)vBox.getScene().getWindow();
        Server server = ServerHandler.getInstance();
        try{
           for(Exam exam:examArrayList) {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("ExamItem.fxml"));
               Node node = loader.load();
               ExamItem examItem = loader.getController();
               examItem.setTitle(exam.getTitle());
               examItem.setDate(exam.getExamDate());
               examItem.setTime(exam.getTime());
               examItem.setExam(exam);
               Team team = server.getTeamById(exam.getTeamId());
               examItem.setTeamName(team.getName());
               examItem.setTeam(team);
               vBox.getChildren().add(node);
               examItem.addEventListener(path);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all the exams.
    public void activeExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> activeExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-examDate.getTime();
                long milli = Long.parseLong(exam.getDuration())*60*1000;      //duration from minutes to milliseconds.
                if(diff>=0 && diff<milli)
                    activeExam.add(exam);
            }
            addExamsToVBox(activeExam,QUESTION_PAPER_PATH);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all exams which has not been finished yet.
    public void upcomingExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> upcomingExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-examDate.getTime();
                if(diff<0)
                    upcomingExam.add(exam);
            }
            if(fromTeacher)
                addExamsToVBox(upcomingExam,QUESTION_PAPER_PATH);
            else
                addExamsToVBox(upcomingExam,"upcoming");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display all finished exams.
    public void archivedExams(ActionEvent actionEvent) {
        vBox.getChildren().clear();
        try{
            ArrayList<Exam> archivedExam = new ArrayList<>();
            for(Exam exam:exams){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = System.currentTimeMillis()-(examDate.getTime()+ (long) (Integer.parseInt(exam.getDuration())) *60*1000);
                if(diff>0)
                    archivedExam.add(exam);
            }
            if(fromTeacher)
                addExamsToVBox(archivedExam,QUESTION_PAPER_PATH);
            else
                addExamsToVBox(archivedExam,SUBMISSION_PATH);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //compare dates.
    private boolean after(Date examDate,String time,int duration){
        Date date = new Date();
        if(date.after(examDate))
            return false;
        if(date.before(examDate))
            return true;
        int h = Integer.parseInt(time.substring(0,1));
        int m = Integer.parseInt(time.substring(3,4));
        h += duration/60;
        duration = duration%60;
        m += duration;
        h += m/60;
        m = m%60;
        if(date.getHours()>h)
            return true;
        if(date.getDate()<h)
            return false;
        return date.getMinutes()>m;
    }

    private long getTimeDiff(long millis){
        return System.currentTimeMillis()-millis;
    }
}
