package com.exam.portal.exams.student;

import com.exam.portal.models.Exam;
import com.exam.portal.models.Option;
import com.exam.portal.models.Question;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

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
    VBox vBox;

    public static Exam exam;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exam = new Exam();
        exam.setExamId("Exam#en4f68");
        fetchExam();
        testTitle.setText(exam.getTitle());
    }

    private void fetchExam(){
        System.out.println(exam.getExamId());
        Server server = ServerHandler.getInstance();
        exam = server.getExamById(exam.getExamId());
        if(exam==null){
            exam = new Exam();
            exam.setQuestions(new ArrayList<>());
        }
    }




    private Image decodeImage(String bytes){
        byte[] bytes1 = Base64.getDecoder().decode(bytes);
        Image image = new Image(new ByteArrayInputStream(bytes1));
        return image;
    }

}
