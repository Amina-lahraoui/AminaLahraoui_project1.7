package ma.project;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * CView - Use of JavaFX UI
 */
public class CView {
    private Stage s;
    private CModel m;
    
    private TextField UnameField;
    private Button ConnectBtn;
    private Label StatusLabel;
    private Circle statusCircle;
    private TextArea ChatArea;
    private TextField MsgField;
    private Button SendBtn;
    private Button allUsersBtn;
    private Button DiscotBtn;
    
    private boolean connected = false;
    private String ServerIP;
    private int ServerPort;
    
    public CView(Stage s) {
        this.s = s;
        setupUI();
    }
    
    public void setM(CModel m) {
        this.m = m;
    }
    
    public void setServerInfo(String IP, int PortNumb) {
        this.ServerIP = IP;
        this.ServerPort = PortNumb;
    }
    
    private void setupUI() {
        s.setTitle("TCP ChatGroup Client");
        
        GridPane g = new GridPane();
        g.setPadding(new Insets(20));
        g.setHgap(10);
        g.setVgap(10);
        // for the sake of theme I went with pink Galaxy theme
        g.setStyle("-fx-background-color: #FFE5F1; -fx-background-radius: 10;");
        
        //Uname part
        Label UnameLabel = new Label("Username:");
        UnameLabel.setStyle("-fx-text-fill: #6B2C91; -fx-font-weight: bold;");
        UnameField = new TextField();
        UnameField.setPromptText("Please enter username (empty for read-only)");
        UnameField.setPrefWidth(250);
        UnameField.setStyle("-fx-background-color: #FFF0F8; -fx-border-color: #E1BEE7; -fx-border-radius: 5;");
        
        ConnectBtn = new Button("Connect");
        ConnectBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #9C27B0, #673AB7); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        ConnectBtn.setOnAction(e -> ControlConnect());
        
        HBox UnameBox = new HBox(10);
        UnameBox.getChildren().addAll(UnameLabel, UnameField, ConnectBtn);
        g.add(UnameBox, 0, 0, 2, 1);
        
        // Status PART
        Label StatTitle = new Label("Status:");
        StatTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B2C91; -fx-font-size: 14px;");
        StatusLabel = new Label("Offline");
        StatusLabel.setStyle("-fx-text-fill: #4A148C; -fx-font-size: 12px;");
        statusCircle = new Circle(8);
        statusCircle.setFill(Color.web("#E91E63"));
        
        HBox StatBox = new HBox(10);
        StatBox.getChildren().addAll(statusCircle, StatusLabel);
        g.add(StatTitle, 0, 1);
        g.add(StatBox, 1, 1);
        
        // CHATTING AREA PART
        Label GroupChatLabel = new Label("GroupChat Messages:");
        GroupChatLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B2C91; -fx-font-size: 14px;");
        
        ChatArea = new TextArea();
        ChatArea.setPrefHeight(300);
        ChatArea.setPrefWidth(600);
        ChatArea.setEditable(false);
        ChatArea.setWrapText(true);
        ChatArea.setStyle("-fx-background-color: #FFF0F8; -fx-text-fill: #4A148C; -fx-border-color: #E1BEE7; -fx-border-radius: 5; -fx-font-family: 'Arial';");
        
        VBox GroupChatBox = new VBox(5);
        GroupChatBox.getChildren().addAll(GroupChatLabel, ChatArea);
        g.add(GroupChatBox, 0, 2, 2, 1);
        
        // input of messages
        Label msgLabel = new Label("message:");
        msgLabel.setStyle("-fx-text-fill: #6B2C91; -fx-font-weight: bold;");
        MsgField = new TextField();
        MsgField.setPromptText("Please type message (use  'allUsers' for all active users,'end'/'bye' for  disconnecting)");
        MsgField.setPrefWidth(400);
        MsgField.setStyle("-fx-background-color: #FFF0F8; -fx-border-color: #E1BEE7; -fx-border-radius: 5;");
        MsgField.setOnAction(e -> ControlSend());
        
        SendBtn = new Button("Send");
        SendBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #9C27B0, #673AB7); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        SendBtn.setDisable(true);
        SendBtn.setOnAction(e -> ControlSend());
        
        allUsersBtn = new Button("AllUsers");
        allUsersBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #E91E63, #AD1457); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        allUsersBtn.setDisable(true);
        allUsersBtn.setOnAction(e -> {
            if (m != null) {
                m.ReqAllUsers();
            }
        });
        
        DiscotBtn = new Button("Disconnect");
        DiscotBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #E91E63, #AD1457); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        DiscotBtn.setDisable(true);
        DiscotBtn.setOnAction(e -> {
            if (m != null) {
                m.disconnect();
            }
        });
        
        HBox msgBox = new HBox(10);
        msgBox.getChildren().addAll(msgLabel, MsgField, SendBtn, allUsersBtn, DiscotBtn);
        g.add(msgBox, 0, 3, 2, 1);
        
        Scene sc = new Scene(g, 700, 450);
        sc.setFill(Color.web("#FFE5F1")); // Light pink background
        s.setScene(sc);
        s.setResizable(false);
    }
    
    private void ControlConnect() {
        if (connected) {
            ShowError("ERROR you are already connected");
            return;
        }
        
        if (ServerIP == null || ServerPort == 0) {
            ShowError("sorry please set first server information");
            return;
        }
        
        String Uname = UnameField.getText().trim();
        
        if (m != null) {
            new Thread(() -> m.connect(ServerIP, ServerPort, Uname)).start();
        }
    }
    
    private void ControlSend() {
        if (!connected) {
            ShowError("not connected");
            return;
        }
        
        String msg = MsgField.getText().trim();
        if (msg.isEmpty()) {
            return;
        }
        
        if ("allUsers".equalsIgnoreCase(msg)) {
            if (m != null) {
                m.ReqAllUsers();
            }
            MsgField.clear();
            return;
        }
        
        if ("end".equalsIgnoreCase(msg) || "bye".equalsIgnoreCase(msg)) {
            if (m != null) {
                m.disconnect();
            }
            MsgField.clear();
            return;
        }
        
        if (m != null) {
            m.SendMessage(msg);
        }
        MsgField.clear();
    }
    
    public void AddMessage(String msg) {
        Platform.runLater(() -> {
            ChatArea.appendText(">>> " + msg + "\n");
            ChatArea.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    public void UpdateStatus(boolean online) {
        Platform.runLater(() -> {
            connected = online;
            if (online) {
                statusCircle.setFill(Color.web("#00E5FF"));
                StatusLabel.setText("Online");
                UnameField.setDisable(true);
                ConnectBtn.setDisable(true);
                SendBtn.setDisable(m != null && m.isReadOnly());
                allUsersBtn.setDisable(false);
                DiscotBtn.setDisable(false);
                MsgField.setDisable(m != null && m.isReadOnly());
                
                if (m != null && m.isReadOnly()) {
                    AddMessage("SORRY, you can't send messages because this is READ-ONLY MODE.");
                }
            } else {
                statusCircle.setFill(Color.web("#E91E63"));
                StatusLabel.setText("Offline");
                UnameField.setDisable(false);
                ConnectBtn.setDisable(false);
                SendBtn.setDisable(true);
                allUsersBtn.setDisable(true);
                DiscotBtn.setDisable(true);
                MsgField.setDisable(true);
            }
        });
    }
    
    public void ShowError(String ERR) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERR");
            alert.setHeaderText(null);
            alert.setContentText(ERR);
            alert.showAndWait();
        });
    }
    
    public void show() {
        s.show();
    }
}
