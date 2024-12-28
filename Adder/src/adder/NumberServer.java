package adder;

import java.io.*;
import java.net.*;

public class NumberServer {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new NumberHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class NumberHandler extends Thread {
    private Socket socket;

    public NumberHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            String inputLine;
            int sum = 0;
            while ((inputLine = in.readLine()) != null) {
                try {
                    int number = Integer.parseInt(inputLine);
                    sum += number;
                    out.println("Current Sum: " + sum);
                } catch (NumberFormatException e) {
                    out.println("Invalid input. Please enter a number.");
                }
            }
        } catch (IOException ex) {
            System.out.println("Handler exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
