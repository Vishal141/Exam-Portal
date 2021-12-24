package com.exam.portal.login;

import com.exam.portal.models.Student;
import com.exam.portal.models.Teacher;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.exam.portal.teacher.TeacherController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;
import java.util.UUID;

public class LoginController implements Initializable {
    @FXML
    RadioButton isStudent;
    @FXML
    PasswordField enteredPassword;
    @FXML
    TextField UserEmailId;
    @FXML
    Button loginBtn;
    @FXML
    Button signupBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML  //call login method of server interface which sends request to server.
    void signIn(ActionEvent event) {
        if(UserEmailId.getText().equals("") || enteredPassword.getText().equals("")){
            showWarning("All fields are mandatory.", Alert.AlertType.WARNING);   //showing alert if all fields are not filled.
        }else{
            Server server = ServerHandler.getInstance();
            if(isStudent.isSelected()){                    //checking that user is student or teacher.
                Student student = new Student();
                student.setEmail(UserEmailId.getText());
                student.setPassword(getHash(enteredPassword.getText()));

                Platform.runLater(()->{   //making login asynchronous
                    if(server.login(student)){
                        StudentController.student = server.getStudent(UserEmailId.getText());
                        gotoDashboard("../student/studentDashboard.fxml");    //if login is successful than go student dashboard.
                    }else{
                        showWarning("Invalid Credentials", Alert.AlertType.ERROR);    //showing alert if login failed.
                    }
                });
            }else{
                Teacher teacher = new Teacher();
                teacher.setEmail(UserEmailId.getText());
                teacher.setPassword(getHash(enteredPassword.getText()));

                Platform.runLater(()->{
                    if(server.login(teacher)){
                        TeacherController.teacher = server.getTeacher(UserEmailId.getText());
                        gotoDashboard("../teacher/teacherDashboard.fxml");
                    }else{
                        showWarning("Invalid Credentials", Alert.AlertType.ERROR);
                    }
                });
            }
        }
    }

    @FXML
    void register(ActionEvent event){    //change current stage to signUp stage.
        try {
            Stage stage = (Stage) signupBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("signUp.fxml"));
            stage.setTitle("Create New Account");
            stage.setScene(new Scene(root,700,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //sign up start here
    @FXML
    TextField pName;
    @FXML
    TextField pNumber;
    @FXML
    TextField pMail;
    @FXML
    PasswordField pPassword;
    @FXML
    Button doSignUp;

    @FXML
    void signUp(ActionEvent event) {   //call register method of server interface which send signUp request to server.
        if(pName.getText().equals("") || pNumber.getText().equals("") || pMail.getText().equals("") || pPassword.getText().equals("")){
            showWarning("All fields are mandatory.", Alert.AlertType.WARNING);
        }else{
            Server server = ServerHandler.getInstance();
            if(isStudent.isSelected()){
                Student student = new Student();
                student.setStudentId(generateId("S"));
                student.setName(pName.getText());
                student.setEmail(pMail.getText());
                student.setContactNo(pNumber.getText());
                student.setPassword(getHash(pPassword.getText()));

                Platform.runLater(()->{
                    if(server.register(student)){
                        StudentController.student = student;
                        gotoDashboard("../student/studentDashboard.fxml");  //goto dashboard if signUp request is successful.
                    }else{
                        showWarning("Email is already registered.", Alert.AlertType.ERROR);
                    }
                });
            }else{
                Teacher teacher = new Teacher();
                teacher.setTeacherId(generateId("T"));
                teacher.setName(pName.getText());
                teacher.setEmail(pMail.getText());
                teacher.setContactNo(pNumber.getText());
                teacher.setPassword(getHash(pPassword.getText()));

                Platform.runLater(()->{
                    if(server.register(teacher)){
                        TeacherController.teacher = server.getTeacher(teacher.getEmail());
                        gotoDashboard("../teacher/teacherDashboard.fxml");
                    }else {
                        showWarning("Email is already registered.", Alert.AlertType.ERROR);
                    }
                });
            }
        }
    }

    //function for showing alert.
    private void showWarning(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }

    //function for hashing password.
    private String getHash(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            String hash=number.toString();
            while (hash.length()<32){
                hash = "v"+hash;
            }
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //function that generate unique id for teacher and student.
    private String generateId(String prefix){
        String uniqueId = UUID.randomUUID().toString();
        return prefix+uniqueId;
    }

    //closing current stage and set it as login stage.
    public void backToLogin(ActionEvent actionEvent) {
        try{
            Stage stage = (Stage) doSignUp.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setTitle("Login");
            stage.setScene(new Scene(root,700,500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //changing current stage to dashboard according to given path.
    public void gotoDashboard(String path){
        try {
            Stage stage = (Stage) isStudent.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root,700,500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
