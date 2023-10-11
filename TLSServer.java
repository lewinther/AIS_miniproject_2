import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class TLSServer {

    public static void main(String[] args) {
        int port = 9999;

        try {
            // Load the keystore with the server's certificate and private key
            char[] keystorePassword = "Drejer".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("server_keystore.jks"), keystorePassword);

            // Set up the key manager factory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword);

            // Set up the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
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
