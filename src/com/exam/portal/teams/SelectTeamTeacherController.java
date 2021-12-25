package com.exam.portal.teams;

import com.exam.portal.exams.scheduled.ScheduledExam;
import com.exam.portal.models.Message;
import com.exam.portal.models.MessageUpdate;
import com.exam.portal.models.Team;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.teacher.TeacherController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

public class SelectTeamTeacherController implements Initializable {
    public static Team team;
    public static Stage stage;

    @FXML
    Label lblTeamName;

    @FXML
    Button btnShowStudents;

    @FXML
    Button btnAddStudent;

    @FXML
    Button btnScheduledExam;

    @FXML
    Button btnGoBack;

    @FXML
    TextField tfMassage;

    @FXML
    Button btnSend;
    @FXML
    private ListView<Label> messageList;

    private ArrayList<Message> returnedMassages;
    private volatile int messageCount;
    private volatile boolean runThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTeamName.setText(team.getName());
        messageCount = 0;
        runThread = false;
        fetchMassages();
    }

    //fetching all messages from server.
    private void fetchMassages(){
        Platform.runLater(()->{
            Server server = ServerHandler.getInstance();
            returnedMassages = server.getMassages(team.getTeamId());
            if(returnedMassages==null)
                returnedMassages=new ArrayList<>();
            setMassages(returnedMassages);
            messageCount = returnedMassages.size();
            loadCurrentMassages();
        });
    }

    //fetching all the messages after certain interval.
    private void loadCurrentMassages(){
        runThread = true;
        new Thread(() -> {
            while(runThread) {
                try{
                    MessageUpdate update=new MessageUpdate(team.getTeamId(),messageCount);
                    Server server = ServerHandler.getInstance();
                    update = server.checkMessageUpdate(update);
                    if(update.isUpdate()){                     //checking for new messages.
                        messageCount = update.getPrevCount();
                        MessageUpdate finalUpdate = update;
                        Platform.runLater(()->{
                            setMassages(finalUpdate.getMessages());
                        });
                    }
                    Thread.sleep(5000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        //for stopping thread after closing messages stage.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                runThread = false;
            }
        });
    }

    private  void setMassages(ArrayList<Message> messages){
        for( Message message:messages){
            Label label=new Label();
            String msg = message.getSenderName() + " : " + message.getMessage();
            label.setText(msg);
            label.autosize();
            label.setMaxWidth(500);
            // Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC,14);
            // label.setFont(font);
            label.setStyle("-fx-background-color: DarkSlateGrey");
            label.setStyle("-fx-border-color: black");
            messageList.getItems().add(label);

        }
    }

    @FXML
    void send(ActionEvent event) {
        if(tfMassage.getText().equals("")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("warning");
            alert.setContentText("massage can't empty");
            alert.showAndWait();
        }else{
            Message newMassage=new Message();
            newMassage.setMessageId(generateId());
            newMassage.setTeamId(team.getTeamId());
            newMassage.setSenderId(TeacherController.teacher.getTeacherId());
            newMassage.setSenderName(TeacherController.teacher.getName());
            newMassage.setMessage(tfMassage.getText());
            newMassage.setDate(new Timestamp(new Date().getTime()));

            Server server = ServerHandler.getInstance();
            if(server.sendMassage(newMassage))
            {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Status");
                alert.setContentText("massage sent successfully");
                alert.showAndWait();
            }
            else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Status");
                alert.setContentText("Some problem occurred");
                alert.showAndWait();
            }
        }
    }

    private String generateId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

    @FXML
    void addStudent(ActionEvent event) {
        String path = "addStudent.fxml";
        changeStage(path,"Add Student",600,650);
    }

    @FXML
    void goBack(ActionEvent event) {
        String path = "teams.fxml";
        changeStage(path,"Teams",700,600);
    }

    @FXML
    void scheduledExam(ActionEvent event) {
        String path = "../exams/scheduled/ScheduledExam.fxml";
        ScheduledExam.fromTeacher = true;
        changeStage(path,"Scheduled Exams",700,600);
    }

    @FXML
    void showAllStudents(ActionEvent event) {
        String path ="showAllStudents.fxml";
        changeStage(path,"students",700,600);
    }

    public void changeStage(String path,String title,int width,int height){
        try{
            runThread = false;
            Stage stage = (Stage) lblTeamName.getScene().getWindow();
            ShowAllStudents.fromTeacher = true;
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            stage.setTitle(title);
            stage.setScene(new Scene(parent,width,height));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
