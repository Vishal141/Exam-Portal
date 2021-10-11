package com.company.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private PasswordField enteredPassword;

    @FXML
    private TextField UserEmailId;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;
    @FXML
    void signIn(ActionEvent event) {
        System.out.println("u are signed in");

    }

    @FXML
    void signUp(ActionEvent event) {

    }
    //sign up start here
    @FXML
    private TextField pName;

    @FXML
    private TextField pNumber;

    @FXML
    private TextField pMail;

    @FXML
    private PasswordField pPassword;

    @FXML
    private Button doSignUp;

    @FXML
    void makePerson(ActionEvent event) {

    }
}
