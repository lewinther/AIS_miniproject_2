import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

public class TLSServer {

    public static void main(String[] args) {
        int port = 9999;

        try {
            char[] keystorePassword = "Drejer1235".toCharArray();
            String keystorePath = "server_keystore.jks";

            // Load the keystore with the server's certificate and private key
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keystorePath), keystorePassword);

            // Set up the key manager factory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword);

            // Set up the SSL context with specific protocols and cipher suites
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Create an SSLServerSocketFactory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Create an SSLServerSocket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

            System.out.println("Server started on port " + port);

            while (true) {
                // Listen for incoming connections
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

                // Handle the connection
                handleConnection(sslSocket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(SSLSocket sslSocket) {
        try {
            // Read and write data on the SSL socket as needed
            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(sslSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                writer.println(inputLine.toUpperCase());
            }

            // Close the SSL socket when done
            sslSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
