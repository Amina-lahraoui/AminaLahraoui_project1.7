# CSC3374_GroupChatApplication_Project
This mini-project is about Java Sockets by implementing a group chat application using TCP (Transmission Control Protocol) and JavaFX. The application enables real-time communication between multiple clients through a central server.
**The project has Features:**
- Java Sockets (TCP)
- JavaFX for GUI
- Thread-per-connection architecture
- Model-View separation pattern
# Problems that the GroupChatApplication Tackles
The project tackles issues like connection of multiple client to one server and meassage broadcasting form one-to-many ueser at the same time. This means implementation of thread-per-connection model to handle various connections simoultaneously. In addiition, it uses users authentification using their names and restrict to read-only mode when the name is not provided. 

# CSC3374_GroupChatApplication Project Structure
```plaintext
ParadigmsProject/
├── Diagrams                            # UML Class, Deployment, Sequence, and Use-Case diagrams
├── tcp_socket_server/                  # The Server-side application
│   ├── src/main/java/ma/project/
│   │   ├── TCPServer.java
│   │   ├── SModel.java
│   │   ├── SView.java
│   │   └── ClientHandler.java
│   └── pom.xml
├── tcp_socket_client/                  # The Client-side  application
│  ├── src/main/java/ma/project/
│  │   ├── TCPClient.java
│  │   ├── CModel.java
│  │   └── CView.java
│  └── pom.xml
└── Readme.md                           # Project Description
```
##  Interoperability
The poroject is implemented using Java language and for the UI display the JavaFX is used "the theme is galaxy pink".
## Requirements for GroupChat Application:
- Java 22 or higher
- JavaFX 21
- Maven 3.6+
  ## Prerequisites of GroupChat Application:
- Java 17 or higher
- JavaFX 17
- Maven
- IntelliJ IDEA

## Installation and Building of the Project (copy-paste commands)

### To build the project you need to run the following commads:

**Server:**
```bash
cd tcp_socket_server
mvn clean package
```

**Client:**
```bash
cd tcp_socket_client
mvn clean package
```

### To Run the Two  Applications

**step 1: Starting the Server first is crucial:**
```bash
cd tcp_socket_server
java -jar target/tcp_socket_server-1.0-SNAPSHOT.jar
```

**Step 2: Starting  Client:**
```bash
cd tcp_socket_client
java -jar target/tcp_socket_client-1.0-SNAPSHOT.jar localhost 3000
```
**Step 3: Alternative method**
Use Directly the Class Files :
```bash
java TCPServer
java TCPClient localhost 3000
```

## How To Use The GroupChat Application:
- First, Enter a username in order to be able to join the chat or leave it empty if read-only mode is wanted.
- Type `allUsers` or click on the allUsers button to see all connected users
- Type `end` or `bye` to disconnect or click the disconnect button.
## Live Demo:
example of how the application works:
It is important to initialize or run the server before the client or connection wil fail:
<p> <img src="https://github.com/user-attachments/assets/18bdce9c-8305-4b47-bfaa-6c35ecb770bf" width="400"> </p>

The server will be started:
<p>
<img src="https://github.com/user-attachments/assets/cbf63d8e-8e0e-4f02-956b-cc3ff8e1e7ef" width="400"> </p>


Client Able To connect:
<p>
<img src="https://github.com/user-attachments/assets/cec8c848-4fb8-416a-88fd-ab8cd3a0530b" width="400"> </p>

Server Log: 
<p>
<img src="https://github.com/user-attachments/assets/4e0bca0a-2fb0-474a-86bb-e726a04d5d99" width="400"> </p>

Other Client Joins the chat
<p> <img src="https://github.com/user-attachments/assets/81e09025-4b3c-4c62-a0bd-d89c83de3ba0" width="400"> </p>

The Server Log:
<p> <img src="https://github.com/user-attachments/assets/259397e3-91d2-4fbf-9110-3d46f6ed12b6" width="400"> </p>


If the client click on the disconnect button then:
 <p> <img src="https://github.com/user-attachments/assets/0ddffa52-ff5e-42d0-9265-c1be55ee39a8" width="400"> </p>








## UML Diagrams Demonstration
I used PlantUML Pluging where I wrote .puml files and images for the diagrams were created. 
See the `diagrams/` folder for:
- Detailed UML Class Diagram
 
  <img src="diagrams/UMLClassDiagram.png" width="300"  alt="Description of the image">

- Deployment Diagram for GroupChat Application:
<p>
  <img src="diagrams/Deployment_Diagram-Deployment Diagram.png" width="300"  alt="Description of the image"> </p>
- Sequence Diagram: one example would be connect User
<img src="diagrams/Sequence_Diagram___Client_Connection-Sequence Diagram - Client Connection.png" width="300"  alt="Description of the image"> </p>
- Use case Diagram
<img src="diagrams/Use_Case_Diagram___GroupChat_Application-Use-Case Diagram  GroupChat Application.png" width="300"  alt="Description of the image"> </p

## Author
Amina Lahraoui

