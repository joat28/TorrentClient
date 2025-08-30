import java.io.*;
import java.net.Socket;

public class Client {

    private int id;
    private Socket socket;
    private int serverPort;
    private String serverIp;

    public Client(int serverPort, String serverIp, int id) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.id = id;
    }
    public static void sendMessageToServer(String msg, Client client) throws IOException {
        OutputStream os = client.socket.getOutputStream();
        os.write(msg.getBytes());
    }

    public static void main(String [] args) throws IOException {

        for (int i = 0; i < 1; ++i) {
            Client client = new Client(6881, "152.173.86.104", i);
            client.socket = new Socket(client.serverIp, client.serverPort);
//            client.sendMessageToServer("Hello from the client id =  " + client.id, client);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from client: " + message);
            }
        }
    }
}
