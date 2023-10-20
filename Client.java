import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class Client {

    public static void main(String[] args) {
        try {
            //Open the truststore
            char[] truststorePassword = "password123".toCharArray();
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(new FileInputStream("truststore.jks"), truststorePassword);

            //Create the SSL (Secure Sockets Layer)
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(truststore);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            //Create client socket SSL
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket clientSocket = (SSLSocket) socketFactory.createSocket("localhost", 7007);

            //Set up input and output streams for communication with the server
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            //Setup input from terminal
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            //Listen for input from the terminal
            while (true) {
                //Wait for input
                System.out.print("Input: ");
                String clientMessage = consoleInput.readLine();
                //Send input to server
                out.println(clientMessage);

                //Read output from server
                System.out.println("Server: " + in.readLine());

                //Close connection when sending specific message
                if (clientMessage.equalsIgnoreCase("fuck")) {
                    System.out.println("Disconnected from server");
                    break;
                }
            }

            //Close client socket when input loop ends
            in.close();
            out.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
