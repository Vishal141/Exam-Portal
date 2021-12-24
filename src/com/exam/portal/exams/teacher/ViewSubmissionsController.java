package com.exam.portal.exams.teacher;

import com.exam.portal.models.Exam;
import com.exam.portal.models.StudentResponse;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teams.SelectTeamStudentController;
import com.exam.portal.teams.SelectTeamTeacherController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewSubmissionsController implements Initializable {
    public  static Exam exam;
    @FXML
    private TableView<StudentResponse> responseTable;

    @FXML
    private TableColumn<StudentResponse, String> nameColumn;

    @FXML
    private TableColumn<StudentResponse, String> emailColumn;

    @FXML
    private TableColumn<StudentResponse, String> contactColumn;

    @FXML
    private TableColumn<StudentResponse, String> marksColumn;

    private ArrayList<StudentResponse> list;
    private ObservableList<StudentResponse>responseList;
    @FXML
    private Label title;
    @FXML
    private TextField searchText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<StudentResponse,String>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        emailColumn.setCellValueFactory(new PropertyValueFactory<StudentResponse,String>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        contactColumn.setCellValueFactory(new PropertyValueFactory<StudentResponse,String>("contact"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        marksColumn.setCellValueFactory(new PropertyValueFactory<StudentResponse,String>("marksObtained"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        title.setText(exam.getTitle());

        fetchAnalysis();
    }
    public  void  fetchAnalysis(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            list=server.fetchExamDetails(exam.getExamId());
            if(list==null)
                list=new ArrayList<>();
            responseList.addAll(list);
            responseTable.setItems(responseList);
            searchStudent();
        });
}
private  void searchStudent(){
        searchText.textProperty().addListener((observableValue,s,t1)->{
            ArrayList<StudentResponse>newList=new ArrayList<>();
            for(StudentResponse studentResponse:list){
                if(studentResponse.getName().contains(t1)|| studentResponse.getEmail().contains(t1) || studentResponse.getContact().contains(t1))
                    newList.add(studentResponse);
            }
            responseList.clear();
            responseList.addAll(newList);
            responseTable.setItems(responseList);


        });
}



    @FXML
    private Button backBtn;

    @FXML
    void back(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scheduled/ScheduledExam.fxml")));
            stage.setTitle("Scheduled Exams");
            stage.setScene(new Scene(root, 700, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }
