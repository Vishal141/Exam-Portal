package com.exam.portal.exams.student;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.*;
import com.exam.portal.proctoring.ProcessesDetails;
import com.exam.portal.proctoring.WebcamRecorder;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;

import com.exam.portal.student.StudentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.ResourceBundle;

public class QuestionPaper implements Initializable {
    private static final int SUBJECTIVE=2;
    @FXML
    Label testTitle;
    @FXML
    Label studentName;
    @FXML
    Label timer;
    @FXML
    JFXButton submitBtn;
    @FXML
    JFXListView<Node> questionList;

    public static Exam exam; //it is set from scheduled exam.
    private int currQuestionIndex; //index of questions which is currently showing.
    private volatile boolean runTimer;

    private WebcamRecorder recorder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testTitle.setText(exam.getTitle());
        questionList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setListenerToQuestionList();
        fetchExam();
        currQuestionIndex = 0;

        if(ScheduledExam.fromTeacher){     //if current user is teacher than disabling submit button .
            studentName.setVisible(false);
            submitBtn.setVisible(false);
            submitBtn.setDisable(true);
            timer.setVisible(false);
        }else {
            studentName.setText(StudentController.student.getName());

            //setting screen to full screen
            Stage stage = (Stage) timer.getScene().getWindow();
            stage.setFullScreen(true);
            stage.setResizable(false);

            //initializing webcam recorder.
            recorder = new WebcamRecorder(Integer.parseInt(exam.getDuration()));
        }
    }

    //fetching all the questions of selected exam and display first of them.
    private void fetchExam(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            exam = server.getExamById(exam.getExamId());
            if(exam==null){
                exam = new Exam();
                exam.setQuestions(new ArrayList<>());
            }else if(exam.getQuestions().size()>0){
                setQuestion(exam.getQuestions().get(0));

                if(!ScheduledExam.fromTeacher){
                    startTimer();
                    recorder.startRecording();          //start capturing student's images.
                    checkCheatStatus();            //monitoring captured image status and number of applications opened.
                }
            }
        });
    }

    //starting timer
    private void startTimer(){
        runTimer = true;
        Task<Void> task = new Task<>(){
            @Override
            protected Void call() throws Exception {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = exam.getExamDate()+" "+exam.getTime();
                Date date = format.parse(time);

                long millisLeft = date.getTime()+(Long.getLong(exam.getDuration()))*60*1000;  //getting total milliseconds left in exam.
                millisLeft -= System.currentTimeMillis();
                int currHours = (int) (millisLeft/(60*60*1000));    //converting millis left to hours,minutes and second left.
                millisLeft = millisLeft - currHours*(60*60*1000);
                int currMinutes = (int) (millisLeft/(60*1000));
                int currSeconds = (int) (millisLeft - currMinutes*60*1000);

                if(currMinutes==0 && currHours>0){          //if current minutes or seconds are 0 than updating them.
                    currMinutes = 59;
                    currHours--;
                }
                if(currSeconds==0 && currMinutes>0){
                    currMinutes--;
                    currSeconds = 59;
                }

                while (currSeconds>0 && runTimer){
                    int finalCurrentHours = currHours;
                    int finalCurrMinutes = currMinutes;
                    int finalCurrSeconds = currSeconds;
                    Platform.runLater(()->{
                        timer.setText("Time Left : "+finalCurrentHours+":"+finalCurrMinutes +":"+ finalCurrSeconds);
                    });

                    currSeconds--;
                    if(currMinutes==0 && currHours>0){
                        currMinutes = 59;
                        currHours--;
                    }
                    if(currSeconds==0 && currMinutes>0){
                        currMinutes--;
                        currSeconds = 59;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(runTimer)
                    submitBtn.fire();
                return null;
            }
        };

        new Thread(task).start();
    }

    //function which check student's cheat status in certain intervals
    private void checkCheatStatus(){
        ProcessesDetails processesDetails = new ProcessesDetails();
        new Thread(()->{
           try {
               int cheatCount = 0;    //showing warning to student 3 times if cheating found then after close exam without submitting.
               while (runTimer){
                   if(recorder.getCheatStatus()){
                       if(cheatCount==3 || processesDetails.getProcessesCount()!=1){  //checking that cheatCount cross the limit or
                           Platform.runLater(()->{                                    //any other application has opened.
                               runTimer = false;
                               recorder.finish();
                               backToExams();
                           });
                       }else{
                           String msg = "It has been detected that you are cheating in the exam.\nDo not repeat it again otherwise " +
                                   "you will be automatically logged out from exam.";
                           Platform.runLater(()->{
                               showAlert(Alert.AlertType.WARNING,"Cheating found",msg);
                           });
                       }
                       cheatCount++;
                   }
                   Thread.sleep(5000);
               }
           }catch (Exception e){
               e.printStackTrace();
           }
        }).start();
    }

    //display a specific question in the list.
    private void setQuestion(Question question){
       // System.out.println(question.getIsImage());
        questionList.getItems().add(getVbox(question.getQuestion(),decodeImage(question.getFile()),question.getIsImage()));
        if(question.getQuestionType()==SUBJECTIVE){   //if question is subjective.
            JFXTextArea textArea = new JFXTextArea();  //adding textarea for writing answer.
            textArea.setPrefHeight(40);
            textArea.setPrefWidth(500);
            textArea.setText(question.getResponse());
            textArea.setPromptText("type here ");
            questionList.getItems().add(textArea);
        }
        int optionIndex = 1;
        for(Option option:question.getOptions()){
            questionList.getItems().add(getVbox(option.getText(),decodeImage(option.getFile()),option.isImage()));
            boolean isSelected = option.getSelected();
            if(isSelected)
                questionList.getItems().get(optionIndex).setStyle("-fx-background-color: #304ffe");
            optionIndex++;
        }
    }

    //it will returns a VBox which will add into questionList as a question or option.
    private VBox getVbox(String text,Image image,boolean isImage){
        VBox vBox = new VBox();
        Text textQ = new Text();
        textQ.setText(text);
        textQ.setFont(Font.font("System",FontWeight.BOLD,FontPosture.ITALIC,14));
        if(isImage){
            ImageView imageView = new ImageView();
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
            vBox.getChildren().add(textQ);
            vBox.getChildren().add(imageView);
            vBox.setVgrow(imageView, Priority.ALWAYS);
        }else{
           // System.out.println(text);
            vBox.getChildren().add(textQ);
            vBox.setVgrow(textQ,Priority.ALWAYS);
        }

        return vBox;
    }

    //it will returns a decoded image object from base64 encoded string.
    private Image decodeImage(String bytes){
        if(bytes==null || bytes.equals(""))
            return null;
        byte[] bytes1 = Base64.getDecoder().decode(bytes);
        Image image = new Image(new ByteArrayInputStream(bytes1));
        return image;
    }

    //adding on click listener to questionList.
    private void setListenerToQuestionList(){
        questionList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int index = questionList.getSelectionModel().getSelectedIndex();
                if(index==0) //because first element of list is question itself.
                    return;
                //making selected option as selected.
                Question question = exam.getQuestions().get(currQuestionIndex);
                if(question.getQuestionType()==SUBJECTIVE)
                    return;
                boolean isSelected = question.getOptions().get(index-1).setSelected();
                if(isSelected){
                    questionList.getItems().get(index).setStyle("-fx-background-color: #304ffe");
                }else {
                    questionList.getItems().get(index).setStyle("-fx-background-color: #fff");
                }
            }
        });
    }

    //shows the prev question of current.
    public void prevQuestion(ActionEvent event){
        //if current question is subjective than saving response in textarea.
        Question question = exam.getQuestions().get(currQuestionIndex);
        if(question.getQuestionType()==SUBJECTIVE){
            JFXTextArea textArea = (JFXTextArea) questionList.getItems().get(1);
            question.setResponse(textArea.getText());
        }

        if(currQuestionIndex>0){
            currQuestionIndex--;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    //shows the next question of current.
    public void nextQuestion(ActionEvent event){
        //if current question is subjective than saving response in textarea.
        Question question = exam.getQuestions().get(currQuestionIndex);
        if(question.getQuestionType()==SUBJECTIVE){
            JFXTextArea textArea = (JFXTextArea) questionList.getItems().get(1);
            question.setResponse(textArea.getText());
        }

        if(currQuestionIndex<exam.getQuestionCount()-1){
            currQuestionIndex++;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    //submit and end the test.
    public void submitTest(ActionEvent event){
        runTimer = false;
        recorder.finish();     //stop capturing student's images.

        //if current question is subjective than saving response in textarea.
        Question question = exam.getQuestions().get(currQuestionIndex);
        if(question.getQuestionType()==SUBJECTIVE){
            JFXTextArea textArea = (JFXTextArea) questionList.getItems().get(1);
            question.setResponse(textArea.getText());
        }

        if(ScheduledExam.fromTeacher)
            return;

        //Creating exam response of student and sending it to server.
        ExamResponse response = new ExamResponse();
        response.setExamId(exam.getExamId());
        response.setStudentId(StudentController.student.getStudentId());
        ArrayList<QuestionResponse> questionResponses = new ArrayList<>();

        for(Question q:exam.getQuestions()){
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setQId(q.getQuestionId());
            if(q.getQuestionType()==SUBJECTIVE){
                questionResponse.setResponseType("text");
                questionResponse.setResponse(q.getResponse());
            }
            else{
                questionResponse.setResponseType("options");
                StringBuilder options = new StringBuilder();
                for(Option option:q.getOptions()){
                    if(option.getSelected())
                        options.append(option.getIndex()).append("*");
                }
                options.deleteCharAt(options.lastIndexOf("*"));
                questionResponse.setResponse(options.toString());
            }
            questionResponses.add(questionResponse);
        }
        response.setMarks(calculateMarks());
        response.setResponses(questionResponses);   //adding all responses in exam response.

        //sending response to server.
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            if(server.sendExamResponse(response)){
                String message = "Your submissions have been submitted successfully.";
                showAlert(Alert.AlertType.INFORMATION,"Successful",message);
            }else{
                String message = "You have already submitted the exam.";
                showAlert(Alert.AlertType.ERROR,"Error",message);
            }
            backToExams();
        });
    }

    //calculating marks obtained by student.
    double calculateMarks(){
        double marks = 0.0;
        for(Question question:exam.getQuestions()){
            if(question.getQuestionType()==SUBJECTIVE){
                if(question.getAnswer().equals(question.getResponse()))
                    marks += question.getPoint();
                else
                    marks -= question.getNegPoint();
            }else{
                boolean flag=true;  //checking that all the options selected by student are correct or not.
                for(Option option:question.getOptions()){
                    if (option.getSelected() ^ option.getCorrect()) {       //checking that whether selected option is correct of not.
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    marks += question.getPoint();
                else
                    marks -= question.getNegPoint();
            }
        }

        return marks;
    }

    //showing alert after submission of exam.
    private void showAlert(Alert.AlertType type,String title,String message){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void backToExams(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../scheduled/ScheduledExam.fxml"));
            Stage stage = (Stage) testTitle.getScene().getWindow();
            stage.setTitle("Exams");
            stage.setScene(new Scene(root,600,600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
