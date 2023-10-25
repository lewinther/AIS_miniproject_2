import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class Server {

    public static void main(String[] args) {
        try {
            //Access the keystore
            char[] keystorePassword = "password123".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("keystore.jks"), keystorePassword);

            //Create key managers to handle certificates using SunX509 algorithm
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keystorePassword);

            //Setup TLS protocol using TLS 1.3 and the created key managers
            SSLContext TLS_Context = SSLContext.getInstance("TLSv1.3");
            TLS_Context.init(keyManagerFactory.getKeyManagers(), null, null);

            //Create server socket using the created TLS protocol
            SSLServerSocketFactory serverSocketFactory = TLS_Context.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(7007);
            System.out.println("Server started");

            //Listen for connecting clients on port
            while (true) {
                //Create a client socket for the connected client on serverSocket.accept()
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("Client Connecting");

                //listen for completed handshake on the client socket
                clientSocket.addHandshakeCompletedListener(event -> {
                    System.out.println("Handshake completed using " + event.getCipherSuite());
                });

                //Set up input and output streams on the created socket
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                //read input message
                String clientMessage = in.readLine();
                System.out.println("Message from client: " + clientMessage);

                //Send response to the client when recieving message
                String msg = "Go " + clientMessage + " yourself!";
                out.println(msg);
                System.out.println("Message to client: " + msg);

                //Close client socket
                in.close();
                out.close();
                clientSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
