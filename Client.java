import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class Client {

    public static void main(String[] args) {
        Client client = new Client("Hello", "localhost", 7007);

    }

    public Client(String message, String ip, int port) {
        try {
            //Access the truststore
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(new FileInputStream("truststore.jks"), "password123".toCharArray());

            //Setup TLS protocol using TLS 1.3 and SunX509 algorithm to validate certificate
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(truststore);
            SSLContext TLS_Context = SSLContext.getInstance("TLSv1.3");
            TLS_Context.init(null, trustManagerFactory.getTrustManagers(), null);

            //Establish Connection
            SSLSocketFactory socketFactory = TLS_Context.getSocketFactory();
            SSLSocket clientSocket = (SSLSocket) socketFactory.createSocket(ip, port);

            clientSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
                @Override
                public void handshakeCompleted(HandshakeCompletedEvent event) {
                    System.out.println("Handshaked");
                }
            });

            //Set up input and output streams on the created socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            //Send message to server
            out.println(message);
            System.out.println("Message to server: " + message);

            //Read output from server
            System.out.println("Message from server: " + in.readLine());

            //Close client socket
            in.close();
            out.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
