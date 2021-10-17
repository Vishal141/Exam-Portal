package com.exam.portal.exams.student;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class QuestionItem {
    @FXML
    VBox vBox;
    @FXML
    Text text;
    @FXML
    ImageView imageView;

    public void setText(String t){
        vBox.getChildren().remove(imageView);
        text.setText(t);
    }

    public void setImageView(Image image){
        //vBox.getChildren().remove(text);
        imageView.autosize();
        imageView.setImage(image);
    }
}
