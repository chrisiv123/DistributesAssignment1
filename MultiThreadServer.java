import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileWriter;


public class MultiThreadServer {
    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket(3500);
            server.setReuseAddress(true);
            // The main thread is just accepting new connections
            while (true) {
                Socket client = server.accept();
                System.out.println("New client:" + client.getInetAddress().getHostAddress());
                ClientHandler clientSock = new ClientHandler(client);

                // The background thread will handle each client separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
// each client handler
    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        public String bounce ="";
        public String theSecret = "7654";

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
// override with the run function
        @Override
        public void run() {
            // local variables
            File file = new File("end.txt");
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                // setting up buffers
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    // System.out.printf("Sent from the client: %s\n", line);
                    // here
                    bounce= BullsCows(line,theSecret);
                    if (file.length() == 0){
                        out.println(bounce);
                    }
                    else {
                        // plyers wins
                        if (bounce =="win"){
                            bounce = "you are the winner, the game is now finished. Congratz!";
                        }
                        else {
                            // player lost (someone else won)
                            bounce = "Someone got the answer before you and the game" +
                                    " is now finished, better luck next time.";
                        }
                        out.println(bounce);
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // releasing the resources
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null)
                        in.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // writing to a file as a global variable
        public void EndingForAll(){
            try {
                // write to end.txt
                FileWriter writer = new FileWriter("end.txt");
                writer.write("end the game");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }


        //////////////// bulls and cows game

        public String BullsCows (String g, String s){
            //// Variables
            int bulls=0, cows=0;
            int[] array1 = new int[10], array2=new int[10];

            // checking for bulls
            for(int i=0; i<s.length(); i++){
                char secret = s.charAt(i);
                char guess = g.charAt(i);

                if(secret==guess)
                    bulls++;
                else{
                    array1[secret-'0']++;
                    array2[guess-'0']++;
                }
            }
            // checking for cows
            for(int i=0; i<10; i++){
                cows += Math.min(array1[i], array2[i]);
            }
                if (bulls==4){
                    EndingForAll();
                    return "win";
                }
            return bulls+" Bulls, and "+cows+" Cows.";


        }
    }
}
