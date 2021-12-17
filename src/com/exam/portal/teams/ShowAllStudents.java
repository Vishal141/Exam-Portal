package com.exam.portal.teams;

import com.exam.portal.models.Student;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.exam.portal.teacher.TeacherController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowAllStudents implements Initializable {

    @FXML
    private TableView<Student> table;

    @FXML
    private TableColumn<Student, String> name;

    @FXML
    private TableColumn<Student, String> mnumber;

    @FXML
    private TableColumn<Student, String> email;

    ArrayList<Student> student;

    private void fetchStudents(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            student = server.getStudentsByTeamId(SelectTeamTeacherController.team.getTeamId());
            if(student == null)
                student = new ArrayList<>();
            //addExamsToVBox(exams);
        });
    }

    ObservableList<Student> list= FXCollections.observableArrayList(student);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Student , String>("name"));
        mnumber.setCellValueFactory(new PropertyValueFactory<Student , String>("mnumber"));
        email.setCellValueFactory(new PropertyValueFactory<Student , String>("email"));
    }
}
