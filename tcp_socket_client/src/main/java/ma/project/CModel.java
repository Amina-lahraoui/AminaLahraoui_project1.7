package ma.project;

import java.io.*;
import java.net.Socket;

/**
 * CModel  to control the communication between sockets and the logic of the server.
 */
public class CModel {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean connected = false;
    private boolean ReadOnly = false;
    private Thread RecieveThread;
    private CView View;

    public CModel() {
    }
    
    public void setView(CView v) {
        this.View = v;
    }
    
    // Initialization of parameter to connect and activation of read only mode when Uname is empty.
    public void connect(String IP, int PortNumb, String Uname) {
        this.ReadOnly = (Uname == null || Uname.trim().isEmpty());
        
        try {
            socket = new Socket(IP, PortNumb);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            connected = true;
            
            // Uname must be sent unless its empty since it is READONLY mode.
            if (ReadOnly) {
                dos.writeUTF("");
            } else {
                dos.writeUTF(Uname.trim());
            }
            dos.flush();
            
            // the user starts getting messages
            RecieveThread = new Thread(this::RecMessages);
            RecieveThread.setDaemon(true);
            RecieveThread.start();
            
            String msg = "is connected to " + IP + ":" + PortNumb;
            if (ReadOnly) {
                msg += " (YOU ARE IN READ-ONLY MODE)";
            }
            updateMessage(msg);
            updateStatus(true);
            
        } catch (IOException e) {
            connected = false;
            showError("ERROR YOU CAN'T CONNECT. RETRY! " + e.getMessage());
        }
    }
    
    // the user will be able to GET messages from the server.
    private void RecMessages() {
        try {
            String msg;
            while (connected && (msg = dis.readUTF()) != null) {
                updateMessage(msg);
            }
        } catch (IOException e) {
            if (connected) {
                connected = false;
                showError("ERROR, the connection is lost: " + e.getMessage());
                updateMessage("disconnected from server");
                updateStatus(false);
            }
        }
    }
    
    // the user can send messages to the server or to people connected in the same server
    public void SendMessage(String msg) {
        if (!connected) {
            showError("you are not connected to server");
            return;
        }
        
        if (ReadOnly) {
            showError("SORRY, this is READ-ONLY MODE. Please enter your name to be able to send messages.");
            return;
        }
        
        // if the user write end or bye in the comment section then they are disconnected.
        if ("end".equalsIgnoreCase(msg) || "bye".equalsIgnoreCase(msg)) {
            disconnect();
            return;
        }
        
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            showError("ERROR sending message: " + e.getMessage());
            disconnect();
        }
    }
    
    // If the user want to see all the users connected then he/she should end allUsers in the comment section.
    public void ReqAllUsers() {
        if (connected) {
            SendMessage("allUsers");
        }
    }
    
    // If the user wants to disconnect from the server
    public void disconnect() {
        if (!connected) {
            return;
        }
        
        connected = false;
        
        try {
            if (dos != null) {
                dos.writeUTF("end");
                dos.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        try {
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        updateMessage("Disconnected from server");
        updateStatus(false);
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public boolean isReadOnly() {
        return ReadOnly;
    }
    
    // updating the messages
    private void updateMessage(String msg) {
        if (View != null) {
            View.AddMessage(msg);
        }
    }
    
    private void updateStatus(boolean Online) {
        if (View != null) {
            View.UpdateStatus(Online);
        }
    }
    
    private void showError(String ERR) {
        if (View != null) {
            View.ShowError(ERR);
        }
    }
}
