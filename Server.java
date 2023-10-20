import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class Server {

    public static void main(String[] args) {
        try {
            //Open the keystore to get certificate
            char[] keystorePassword = "password123".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("keystore.jks"), keystorePassword);

            //Create the SSL (Secure Sockets Layer)
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keystorePassword);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            //Create server socket listening on port with created SSL
            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(7007);
            System.out.println("Server started");

            //listen for connecting clients on port
            while (true) {
                //Create client socket from the connected client from .accept()
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("Client connected.");

                //Set up input and output streams for communication with the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


                //Listen for input on the client socket
                while (true) {
                    //read input when new message is availble
                    String clientMessage = in.readLine();
                    System.out.println("Client: " + clientMessage);

                    //Send response to the client when recieving message
                    out.println("Go " + clientMessage + " yourself!");

                    //Close socket when recieving specific message
                    if (clientMessage.equalsIgnoreCase("fuck")) {
                        System.out.println("Client disconnected");
                        break;
                    }
                }

                //Close client socket when input loop ends
                in.close();
                out.close();
                clientSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
