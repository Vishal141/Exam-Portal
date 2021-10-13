package com.exam.portal.teams;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class teamsController {
    @FXML
    private Label lblCondidateName;

    @FXML
    private Label lblCondidateMail;

    @FXML
    private Label lblCondidateID;

    @FXML
    private Button btnManageTeams;

    @FXML
    private Button btnJoinWId;

    @FXML
    private Button btnCreateNewTeam;

    @FXML
    private Button btnBack;

    @FXML
    private ListView<?, ?> teamsListView;

}
