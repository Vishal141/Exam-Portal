package com.exam.portal.teams;

import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

public class CreateTeam {
    @FXML
    TextField txtFieldTeamName;

    @FXML
    Button createTeamBtn;

    @FXML
    void createTeam(ActionEvent event) {
        if(txtFieldTeamName.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.setContentText("Team name is mandatory.");
            alert.showAndWait();
        }else{
            Team team = new Team();
            team.setName(txtFieldTeamName.getText());
            team.setTeamId(generateId());
            team.setCreatorId(TeacherController.teacher.getTeacherId());
            team.setDateCreated(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            Server server = ServerHandler.getInstance();
            if(server.createTeam(team)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Successful");
                alert.setContentText("Team successfully created.");
                alert.showAndWait();
                Stage stage = (Stage) createTeamBtn.getScene().getWindow();
                stage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Failed");
                alert.setContentText("Team creation Failed.");
                alert.showAndWait();
            }
        }
    }

    private String generateId(){
        String id = UUID.randomUUID().toString();
        Random random = new Random();
        int start = random.nextInt(id.length()-10);
        id = "Team#"+id.substring(start,start+4);
        id = id.replace('-', (char) (97+random.nextInt()%26));
        return id;
    }

}
