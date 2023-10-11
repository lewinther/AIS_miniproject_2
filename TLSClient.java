import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TLSClient {

    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 9999;

        try {
            // Create an SSLSocketFactory
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            // Create an SSLSocket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverHost, serverPort);

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(sslSocket.getOutputStream(), true);

            // Send a message to the server
            String message = "Hello, Server!";
            writer.println(message);

            // Receive the response from the server
            String response = reader.readLine();
            System.out.println("Received from server: " + response);

            // Close the socket
            sslSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
