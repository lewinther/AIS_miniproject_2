import java.io.*;
import java.net.*;
import java.util.Scanner;
 
// Main class
public class Client {
    DataOutputStream d;
    Socket soc;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("Enter a command (type 'exit' to quit): ");
            input = scanner.nextLine();
            Client client = new Client(input, "localhost", 7007);
        }
    }

    public Client(String msg, String ip, int port) {
        try {
            soc = new Socket("localhost", port);
            d = new DataOutputStream(soc.getOutputStream());
            d.writeUTF(msg);
            d.flush();
            d.close();
            soc.close();
            System.out.println("Done");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void end() {
        try {
            d.close();
            soc.close();
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
