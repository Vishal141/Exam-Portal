package com.exam.portal.exams.student;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Option;
import com.exam.portal.models.Question;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    @FXML
    Label testTitle;
    @FXML
    Label studentName;
    @FXML
    Label timer;
    @FXML
    JFXListView<VBox> questionList;

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
                boolean isSelected = exam.getQuestions().get(currQuestionIndex).getOptions().get(index-1).setSelected();
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
        if(currQuestionIndex>0){
            currQuestionIndex--;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    //shows the next question of current.
    public void nextQuestion(ActionEvent event){
        if(currQuestionIndex<exam.getQuestionCount()-1){
            currQuestionIndex++;
            questionList.getItems().clear();
            setQuestion(exam.getQuestions().get(currQuestionIndex));
        }
    }

    //submit and end the test.
    public void submitTest(ActionEvent event){

    }

}
