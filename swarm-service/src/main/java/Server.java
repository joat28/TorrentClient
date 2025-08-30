import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection reached : " + clientSocket.toString());
            ClientSocketThread clientSocketThread = new ClientSocketThread();
            clientSocketThread.run(clientSocket);
        }
    }

    class ClientSocketThread extends Thread {
        public void run(Socket clientSocket) throws IOException {
            System.out.println("Creating new socket thread for the client " + clientSocket.toString() + " is running");
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from client: " + message);
            }
        }
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }

    public static void main(String [] args) throws IOException, InterruptedException {
        Server server = new Server(8084);
        server.start();

        Thread.sleep(10000);
        server.close();

    }
}
