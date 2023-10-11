import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        int port = 7007; // Change this to the port you want to use

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                //setup reader and write object
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                //read input and perform
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    
                    //remove BOM
                    if (inputLine != null && inputLine.startsWith("\uFEFF")) {
                        inputLine = inputLine.substring(1); // Remove the BOM character
                    }

                    System.out.println("Received message from " + clientSocket.getInetAddress() + ": " + inputLine);
                    writer.println("Server: Received your message - " + inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //try to close the server
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}