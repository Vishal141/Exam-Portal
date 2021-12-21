package com.exam.portal.exams.student;

import com.exam.portal.models.*;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class SubmissionsController implements Initializable {
    private static final String TEXT = "text";
    private static final String OPTIONS = "options";
    private static final int SUBJECTIVE = 2;

    @FXML
    Label testTitle;

    @FXML
    Label studentName;

    @FXML
    Label marks;

    @FXML
    JFXListView<Node> questionList;

    public static Exam exam;
    private ExamResponse response;
    private Server server;

    private int currQuestionIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testTitle.setText(exam.getTitle());
        studentName.setText(StudentController.student.getName());
        server = ServerHandler.getInstance();
        currQuestionIndex = 0;
        fetchExam();
        fetchResponse();
    }

    //fetching exam from sever.
    private void fetchExam(){
        Platform.runLater(()->{
            exam = server.getExamById(exam.getExamId());
            if(exam==null)
                exam = new Exam();
        });
    }
    
    //fetching student response
    private void fetchResponse(){
        Platform.runLater(()->{
            response = server.getStudentExamResponse(exam.getExamId(),StudentController.student.getStudentId());
            if(response==null)
                response = new ExamResponse();
            marks.setText(response.getMarks()+"/"+exam.getTotalMarks());
            setResponses();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        });
    }

    //setting student response in question
    private void setResponses(){
        int idx = 0;
        for(QuestionResponse questionResponse:response.getResponses()){
            Question question = exam.getQuestions().get(idx++);
            if(questionResponse.getResponseType().equals(TEXT))
                question.setResponse(questionResponse.getResponse());
            else if(questionResponse.getResponseType().equals(OPTIONS)){
                StringTokenizer tokenizer = new StringTokenizer(questionResponse.getResponse(),"*");
                while(tokenizer.hasMoreTokens()){
                    question.getOptions().get(Integer.parseInt(tokenizer.nextToken())).setSelected();
                }
            }
        }
    }

    @FXML
    void nextQuestion(ActionEvent event) {
        if(currQuestionIndex<exam.getQuestionCount()-1){
            currQuestionIndex++;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    @FXML
    void prevQuestion(ActionEvent event) {
        if(currQuestionIndex>0){
            currQuestionIndex--;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    //display a specific question in the list.
    private void setQuestion(Question question){
        // System.out.println(question.getIsImage());
        questionList.getItems().add(getVbox(question.getQuestion(),decodeImage(question.getFile()),question.getIsImage()));
        if(question.getQuestionType()==SUBJECTIVE){   //if question is subjective.
            JFXTextArea studentResponse = new JFXTextArea();  //adding textarea for student response
            studentResponse.setPrefHeight(40);
            studentResponse.setPrefWidth(500);
            studentResponse.setText(question.getResponse());
            studentResponse.setEditable(false);          //making textarea not editable
            questionList.getItems().add(studentResponse);

            JFXTextArea answer = new JFXTextArea();    //adding textarea for answer of question.
            answer.setPrefHeight(40);
            answer.setPrefWidth(500);
            answer.setEditable(false);
            answer.setText(question.getAnswer());
            questionList.getItems().add(answer);

            return;
        }
        int optionIndex = 1;
        StringBuilder answers= new StringBuilder();
        for(Option option:question.getOptions()){
            questionList.getItems().add(getVbox(option.getText(),decodeImage(option.getFile()),option.isImage()));
            boolean isSelected = option.getSelected();
            if(isSelected)
                questionList.getItems().get(optionIndex).setStyle("-fx-background-color: #304ffe");
            if(option.getCorrect())
                answers.append(optionIndex).append(",");
            optionIndex++;
        }
        answers.deleteCharAt(answers.lastIndexOf(","));

        Label label = new Label();    //adding label which shows the correct options.
        label.setPrefHeight(30);
        label.setPrefWidth(500);
        label.setText("Answers : "+ answers);
        questionList.getItems().add(label);
    }

    //it will returns a VBox which will add into questionList as a question or option.
    private VBox getVbox(String text, Image image, boolean isImage){
        VBox vBox = new VBox();
        Text TextQ = new Text();
        TextQ.setText(text);
        TextQ.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14));
        if(isImage){
            ImageView imageView = new ImageView();
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
            vBox.getChildren().add(TextQ);
            vBox.getChildren().add(imageView);
            vBox.setVgrow(imageView, Priority.ALWAYS);
        }else{
            vBox.getChildren().add(TextQ);
            vBox.setVgrow(TextQ,Priority.ALWAYS);
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
}