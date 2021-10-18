package com.exam.portal.teams;

import com.exam.portal.models.BelongTo;
import com.exam.portal.models.Student;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable {
    @FXML
    JFXComboBox<String> selectedTeam;
    @FXML
    JFXTextField searchText;
    @FXML
    JFXListView<Label> searchResultList;

    private ArrayList<Team> teams;
    private ObservableList<String> observableList;
    private ArrayList<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableList = FXCollections.observableArrayList();
        fetchTeams();
        setList();
    }

    private void setList() {
        selectedTeam.getItems().addAll(observableList);
    }

    private void fetchTeams() {
        Server server = ServerHandler.getInstance();
        teams = server.getTeachersTeams(TeacherController.teacher.getTeacherId());
        if(teams==null)
            teams = new ArrayList<>();
        for(Team team:teams){
            observableList.add(team.getName());
        }
    }

    public void search(ActionEvent actionEvent){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(searchText.getText().equals("")){
                    showAlert("search item is empty.","Warning", Alert.AlertType.WARNING);
                }else{
                    Server server = ServerHandler.getInstance();
                    students = server.searchStudent(searchText.getText());
                    if(students==null)
                        students = new ArrayList<>();
                    for(Student student:students){
                        Label label = new Label();
                        label.setText(student.getName());
                        Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
                        label.setFont(font);
                        label.setTextFill(Color.BLACK);
                        searchResultList.getItems().add(label);
                    }
                }
            }
        });
    }

    public void addStudent(ActionEvent actionEvent){
        if(searchResultList.getSelectionModel().isEmpty() || selectedTeam.getSelectionModel().isEmpty()){
            showAlert("Please select student from list or team from team list.","Warning", Alert.AlertType.WARNING);
        }else{
            Server server = ServerHandler.getInstance();
            Student student = students.get(searchResultList.getSelectionModel().getSelectedIndex());
            Team team = teams.get(selectedTeam.getSelectionModel().getSelectedIndex());
            BelongTo belongTo = new BelongTo();
            belongTo.setStudentId(student.getStudentId());
            belongTo.setTeamId(team.getTeamId());
            //getting date
            LocalDate date = LocalDate.now();
            String sDate = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
            belongTo.setDate(sDate);

            if(server.addStudent(belongTo)){
                showAlert("Student successfully added to team","Successful", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) searchText.getScene().getWindow();
                stage.close();
            }else{
                String message = "Not able to add student in this team.\nMay be student already exist int this team.";
                showAlert(message,"Warning", Alert.AlertType.WARNING);
            }
        }
    }

    public void showAlert(String message,String title, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
