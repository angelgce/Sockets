
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {

    private ServerSocket server; // A serversocket that will be listening for client requests.
    private Socket socket; // the connector between the server and the client
    private boolean stop = false; // to kill thread
    private static Servidor servidor;

    // singleton Design pattern
    public static Servidor getInstance() {
        if (servidor == null) {
            servidor = new Servidor();
        }
        return servidor;
    }

    public static Servidor removeInstance() {
        return servidor = null;
    }

    // constructor
    private Servidor() {

        System.out.println("Server 1.0");// typing the version of the server on console
        try {
            server = new ServerSocket(9000);// creating a serersocket at the port (9000)
            Thread thread = new Thread(this); // new thread
            thread.start();// starting thread
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
        }
    }

    private void send_file(String file_name) { // Method to send Files
        File file = new File("src\\Files\\" + file_name); // creating new file
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); // creating our output stream
                                                                                          // with our client (by the
                                                                                          // same socket)
            output.writeObject(file); // sending the file to client
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void run() {
        while (!stop) {
            try {
                socket = server.accept(); // if the server listen a request it will accept it by a socket!
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); // we will receive your
                                                                                          // request by our input stream
                String file_name = (String) input.readObject(); // will get the name of the file
                System.out.println("A user is asking for : " + file_name); // we write it on console
                send_file(file_name); // calling the send_file method giving the file name as a parameter
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args) {
       Servidor server = servidor.getInstance();
    }

}
