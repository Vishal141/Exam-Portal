package com.exam.portal.exams.teacher.createexam;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Question;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CreateExamController implements Initializable {
    @FXML
    JFXComboBox<String> selectedTeam;
    @FXML
    TextField tfTitle;
    @FXML
    TextField tfDuration;
    @FXML
    JFXDatePicker datePicker;
    @FXML
    JFXTimePicker timePicker;
    @FXML
    JFXListView<Label> questionList;

    private String date,time;

    public ObservableList<String> observableList;
    public ArrayList<Team> teams;

    private static Exam exam;

    private Stack<Question> stack;

    private volatile boolean isThreadRunning;
    private volatile int questionCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timePicker.set24HourView(true);
        observableList = FXCollections.observableArrayList();
        setTeams();
        setObservableList();
        selectedTeam.getItems().addAll(observableList);
        exam = new Exam();
        exam.setExamId(generateRandomId());
        stack = new Stack<>();
        isThreadRunning = false;
        questionCount = 0;
        date = "";
        time = "";
    }

    public void setObservableList(){
        for(Team team:teams){
            observableList.add(team.getName());
        }
    }

    public void setTeams(){
        Server server = ServerHandler.getInstance();
        teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
    }

    @FXML
    public void createExam(ActionEvent event) {
        boolean flag;
        setDateAndTime();
        if(!checkCombobox())
            return;
        if(tfTitle.getText().equals("") || tfDuration.getText().equals("") || date.equals("") || time.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Please select a team.");
            alert.showAndWait();
            flag = false;
        }else{
            flag = true;
            exam.setTitle(tfTitle.getText());
            exam.setExamDate(date);
            exam.setTime(time);
            exam.setDuration(tfDuration.getText());
            exam.setTeamId(teams.get(selectedTeam.getSelectionModel().getSelectedIndex()).getTeamId());
            exam.setCreatorId(TeacherController.teacher.getTeacherId());
        }

        if(flag){
            Server server = ServerHandler.getInstance();
            if(server.createExam(exam)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Successful");
                alert.setContentText("Exam successfully created.");
                alert.showAndWait();
                stopThread();
                Stage stage = (Stage) tfTitle.getScene().getWindow();
                stage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Failed");
                alert.setContentText("Exam creation failed");
                alert.showAndWait();
            }
        }
    }

    public boolean checkCombobox(){
        if(selectedTeam.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Please select a team.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void addQuestion(ActionEvent actionEvent) {
        //for running thread
        if(!isThreadRunning){
            runThread();
        }
        //for stopping running thread.
        if(isThreadRunning){
            Stage stage1 = (Stage) tfTitle.getScene().getWindow();
            stage1.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    stopThread();
                }
            });
        }

        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addQuestion.fxml")));
            stage.setTitle("Add Question");
            stage.setScene(new Scene(parent,800,800));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void undo(ActionEvent actionEvent) {
        Question question = exam.undoQuestion();
        if(question==null)
            return;
        questionCount--;
        questionList.getItems().remove(questionCount);
        stack.push(question);
    }

    public void redo(ActionEvent actionEvent) {
        if(!stack.isEmpty()){
            Question question = stack.pop();
            exam.addQuestion(question);
        }
    }

    public static void addQuestion(Question question){
        question.setExamId(exam.getExamId());
        exam.addQuestion(question);
    }

    public void runThread(){
        isThreadRunning = true;
        new Thread(()->{
            while (isThreadRunning){
                if(exam.getQuestionCount() != questionCount){
                    Question question = exam.getLastQuestion();
                    Label label = new Label();
                    label.setText(question.getQuestion());
                    questionList.getItems().add(label);
                    questionCount = exam.getQuestionCount();
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopThread(){
        isThreadRunning = false;
    }

    public String generateRandomId(){
        String id = UUID.randomUUID().toString();
        int start = Math.abs((Math.abs(new Random().nextInt(id.length()))%id.length()-15));
        id = "Exam#"+id.substring(start,start+6);
        id = id.replace('-',(char) ('a'+(new Random().nextInt())%26));
        return id;
    }

    public void setDateAndTime(){
        LocalDate localDate = datePicker.getValue();
        if(localDate==null)
            return;
        date = localDate.getYear()+"-"+localDate.getMonthValue()+"-"+localDate.getDayOfMonth();
        LocalTime localTime = timePicker.getValue();
        if(localTime==null)
            return;
        time = localTime.getHour()+":"+localTime.getMinute()+":00";
    }
}
