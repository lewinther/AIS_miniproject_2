import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;

public class JavaTLSServerExample {

    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] cipher_suites = new String[]{"TLS_AES_128_GCM_SHA256"};

    public static void main(String[] args) throws Exception {

        SSLServerSocket serverSocket = null;

        try {

            // Step : 1
            SSLServerSocketFactory factory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            // Step : 2
            serverSocket = (SSLServerSocket) factory.createServerSocket(8980);

            // Step : 3
            serverSocket.setEnabledProtocols(protocols);
            serverSocket.setEnabledCipherSuites(cipher_suites);

            // Step : 4
            SSLSocket sslSocket = (SSLSocket) serverSocket.accept();

            // Step : 5
            InputStream inputStream = sslSocket.getInputStream();
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String request = null;
            while((request = bufferedReader.readLine()) != null) {
                 System.out.println(request);
                 System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}