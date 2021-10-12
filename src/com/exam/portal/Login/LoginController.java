package com.exam.portal.Login;

import com.exam.portal.models.Student;
import com.exam.portal.models.Teacher;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
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
        UserEmailId.requestFocus();
    }
    @FXML
    void signIn(ActionEvent event) {
        if(UserEmailId.getText().equals("") || enteredPassword.getText().equals("")){
            showWarning("All fields are mandatory.", Alert.AlertType.WARNING);
        }else{
            Server server = ServerHandler.getInstance();
            if(isStudent.isSelected()){
                Student student = new Student();
                student.setEmail(UserEmailId.getText());
                student.setPassword(getHash(enteredPassword.getText()));
                if(server.login(student)){
                    System.out.println("successful");
                }else{
                    showWarning("Invalid Credentials", Alert.AlertType.ERROR);
                }
            }else{
                Teacher teacher = new Teacher();
                teacher.setEmail(UserEmailId.getText());
                teacher.setPassword(getHash(enteredPassword.getText()));
                if(server.login(teacher)){
                    TeacherController.teacher = server.getTeacher(UserEmailId.getText());
                    gotoDashboard("../teacher/TeacherDashboard.fxml");
                }else{
                    showWarning("Invalid Credentials", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    void signUp(ActionEvent event){
        try {
            Stage stage = (Stage) signupBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
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
    void makePerson(ActionEvent event) {
        if(pName.getText().equals("") || pNumber.getText().equals("") || pMail.getText().equals("") || pPassword.getText().equals("")){
            showWarning("All fields are mandatory.", Alert.AlertType.WARNING);
        }else{
            Server server = ServerHandler.getInstance();
            if(isStudent.isSelected()){
                System.out.println("student");
                Student student = new Student();
                student.setStudentId(generateId("S"));
                student.setName(pName.getText());
                student.setEmail(pMail.getText());
                student.setContactNo(pNumber.getText());
                student.setPassword(getHash(pPassword.getText()));

                if(server.register(student)){
                    System.out.println("successful");
                }else{
                    showWarning("Email is already registered.", Alert.AlertType.ERROR);
                }
            }else{
                Teacher teacher = new Teacher();
                teacher.setTeacherId(generateId("T"));
                teacher.setName(pName.getText());
                teacher.setEmail(pMail.getText());
                teacher.setContactNo(pNumber.getText());
                teacher.setPassword(getHash(pPassword.getText()));

                if(server.register(teacher)){
                    TeacherController.teacher = server.getTeacher(teacher.getEmail());
                    gotoDashboard("../teacher/TeacherDashboard.fxml");
                }else {
                    showWarning("Email is already registered.", Alert.AlertType.ERROR);
                }
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

    public void backToLogin(ActionEvent actionEvent) {
        try{
            Stage stage = (Stage) doSignUp.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            stage.setTitle("Login");
            stage.setScene(new Scene(root,700,500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void gotoDashboard(String path){
        try {
            Stage stage = (Stage) isStudent.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root,500,600));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
