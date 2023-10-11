import java.io.*;
import java.net.*;
 
// Main class
public class Server {
 
    // Main driver method
    public static void main(String[] args)
    {
 
        // Try block to check for exceptions
        try {
            ServerSocket ss = new ServerSocket(7007);
            while(true) {
                Socket soc = ss.accept();
    
                // Invoking input stream via getInputStream()
                // method by creating DataInputStream class
                // object
                DataInputStream dis = new DataInputStream(soc.getInputStream());
    
                String str = (String)dis.readUTF();
    
                // Display the string on the console
                System.out.println("message= " + str);
    
                // Lastly close the socket using standard close
                // method to release memory resources
                System.out.println("fuck");

                ss.close();  
            }
        }
 
        // Catch block to handle the exceptions
        catch (Exception e) {
 
            // Display the exception on the console
            System.out.println(e);
        }
    }
}