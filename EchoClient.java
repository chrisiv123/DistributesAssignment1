// by christian ivano 100555278, Assignment 1 for distributed systems
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) {
// host is local host and port used is 3500.
        String host = "localhost";
        int port = 3500;
	    // connecting to the server.
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            String line = null;
		// the game starts.
            System.out.println("You have connected to a Bulls and cows game.");
            System.out.println("The point of this game is to find the secret number before the others");
            System.out.println("Just enter a 4 digit number and you will get feedback on how close");
            System.out.println("you are to the answer.");
            System.out.println("Cows mean you got a correct digit but not the place");
            System.out.println("Bulls mean you got the correct digit and place");
            System.out.println("");
            System.out.println("Now good luck and go get them!");
            System.out.println(" if you want to exit just say so :)");
		// constantly asking for inputs.
            while (!"exit".equalsIgnoreCase(line)) {
				System.out.println(" That was not the correct answer, try something else or exit to leave");
                line = scanner.nextLine();
                out.println(line);
                out.flush();
                System.out.println(in.readLine());
            }// closeing all the opened resources.
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
