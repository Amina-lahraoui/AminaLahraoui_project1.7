package ma.project;

import java.io.*;
import java.net.Socket;

/**
 * ClientHandler handles any client connection in different thread
 */
//I used Thread-per-connection approach
public class ClientHandler implements Runnable {
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private SModel server;
    private String Uname;
    private String color;
    private boolean connected = true;
    
    public ClientHandler(Socket so, SModel ser) {
        this.s = so;
        this.server = ser;
        try {
            dis = new DataInputStream(so.getInputStream());
            dos = new DataOutputStream(so.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {

            String receivedUname = dis.readUTF();
            
            if (receivedUname == null || receivedUname.trim().isEmpty()) {
                // read only mode IN NO USERNAME
                Uname = null;
                sendMessage("You are in READ-ONLY MODE. Enter a username to send messages.");
            } else {
                Uname = receivedUname.trim();
                server.clientConnected(this, Uname);
            }
            

            String msg;
            while (connected && (msg = dis.readUTF()) != null) {

                if ("end".equalsIgnoreCase(msg) || "bye".equalsIgnoreCase(msg)) {
                    break;
                }
                
                // allUsers command
                if ("allUsers".equalsIgnoreCase(msg)) {
                    var u = server.getAllUnames();
                    sendMessage("Active Users: " + String.join(", ", u));
                    continue;
                }
                
                //  username is fulfilled broadcast message
                if (Uname != null && !Uname.isEmpty()) {
                    server.broadcastMessage(msg, Uname);
                } else {
                    // ONLY read mode
                    sendMessage("hello, this is READ-ONLY MODE. Enter your username to be able to send messages.");
                }
            }
        } catch (IOException e) {
            // Cl is disconnected
        } finally {
            disconnect();
        }
    }
    

    public void sendMessage(String msg) {
        if (connected && dos != null) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                disconnect();
            }
        }
    }
    
    // Disconnect client
    public void disconnect() {
        connected = false;
        
        if (server != null && Uname != null) {
            server.clientDisconnected(Uname);
            server.RemoveClient(this);
        }
        
        try {
            if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (s != null) s.close();
        } catch (IOException e) {
            // Ignore
        }
    }
    
    public String getUname() {
        return Uname;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
}
