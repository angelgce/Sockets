import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;// socket -> the connector between the client and the server
    private ObjectInputStream input; // we will send data with this stream
    private ObjectOutputStream output;// we will receive data with this stream

    public Client() {

        System.out.println("We are going to receive a file from the server.");// Welcome message

    }

    public void ask_file(String file_name) {// We are going to request a file from the server
        try {
            socket = new Socket("localhost", 9000); // creating a socket (the ip of the sever) - (port)
            // Sending...
            output = new ObjectOutputStream(socket.getOutputStream());// creating our output stream to send requests
            output.writeObject(file_name);// sending the name of the file that we want to download
            // Receiving...
            input = new ObjectInputStream(socket.getInputStream());// creating our input stream to receive files
            File file = (File) input.readObject(); // getting the file from server
            File file2 = new File(System.getProperty("user.home") + "\\Desktop\\" + file_name); // creating the user new
            copy_file(file, file2);// calling copy_file method
            // closing the streams and socket.
            output.close();
            input.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void copy_file(File in, File out) {// in (file from server) out (file from user pc)
        try {
            FileInputStream file_in = new FileInputStream(in); // creating a file input stream
            FileOutputStream file_out = new FileOutputStream(out);// creating a file output stream
            byte[] buf = new byte[1024];// new buff
            int len;
            while ((len = file_in.read(buf)) > 0) {
                file_out.write(buf, 0, len);// writting the new file
            }
            // closing the streams
            file_in.close();
            file_out.close();
            System.out.println("Operation competed");// the file is now in user's pc
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        // the name of the file you want to request from server
        client.ask_file("readme.txt");
        client.ask_file("image1.ico");
        client.ask_file("Instaler.exe");
    }

}
