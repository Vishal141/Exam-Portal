package com.exam.portal.exams.student;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.*;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;

import com.exam.portal.student.StudentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
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

    //it is set from scheduled exam.
    public static Exam exam;
    //index of questions which is currently showing.
    private int currQuestionIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testTitle.setText(exam.getTitle());
        questionList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setListenerToQuestionList();
        fetchExam();
        currQuestionIndex = 0;

        if(ScheduledExam.fromTeacher){
            submitBtn.setVisible(false);
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
            }
        });
    }

    //display a specific question in the list.
    private void setQuestion(Question question){
       // System.out.println(question.getIsImage());
        questionList.getItems().add(getVbox(question.getQuestion(),decodeImage(question.getFile()),question.getIsImage()));
        if(question.getQuestionType()==SUBJECTIVE){
            JFXTextArea textArea = new JFXTextArea();
            textArea.setPrefHeight(40);
            textArea.setPrefWidth(500);
            textArea.setText(question.getResponse());
            textArea.setPromptText("type here ");
            questionList.getItems().add(textArea);
        }
        for(Option option:question.getOptions()){
            questionList.getItems().add(getVbox(option.getText(),decodeImage(option.getFile()),option.isImage()));
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
    public void setListenerToQuestionList(){
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
                boolean isSelected = question.getOptions().get(index-1).setIsSelected();
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
        //if current question is subjective than saving response in textarea.
        Question question = exam.getQuestions().get(currQuestionIndex);
        if(question.getQuestionType()==SUBJECTIVE){
            JFXTextArea textArea = (JFXTextArea) questionList.getItems().get(1);
            question.setResponse(textArea.getText());
        }

        //Creating exam response of student and sending it to server.
        ExamResponse response = new ExamResponse();
        response.setExamId(exam.getExamId());
        response.setStudentId(StudentController.student.getStudentId());

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
                    if(option.getIsSelected())
                        options.append(option.getIndex()).append("*");
                }
                options.delete(options.lastIndexOf("*"),options.lastIndexOf("*"));
                questionResponse.setResponse(options.toString());
            }
        }

    }

}
