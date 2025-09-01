import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

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

//        for (int i = 0; i < 1; ++i) {
            Client client = new Client(38355, "136.35.138.88", 1);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(client.serverIp, client.serverPort), 5000);
            client.socket = socket;
            // client.sendMessageToServer("Hello from the client id =  " + client.id, client);

            OutputStream out = client.socket.getOutputStream();
            InputStream in   = client.socket.getInputStream();

            // --- Build and send handshake ---
            byte[] infoHash = hexToBytes("DEFEE68F7A6CEF4B8C2ABD5953DFDAAA92B15E50");
            byte[] peerId   = "-PC0032-6wfG2wk6wWLc".getBytes("ISO-8859-1");

            out.write(19); // pstrlen
            out.write("BitTorrent protocol".getBytes("ISO-8859-1"));
            out.write(new byte[8]);    // reserved
            out.write(infoHash);       // 20 bytes
            out.write(peerId);         // 20 bytes
            out.flush();

            // --- Read 68-byte handshake response ---
            byte[] response = new byte[68];
            int read = in.read(response);
            if (read == 68) {
                System.out.println("✅ Got handshake from peer " + 1);
                System.out.println("Protocol: " + new String(response, 1, 19, "ISO-8859-1"));


                byte[] lenBytes = in.readNBytes(4);
                int length = ByteBuffer.wrap(lenBytes).getInt();
                int msgId = in.read();
                if (msgId == 5) {
                    byte[] payload = in.readNBytes(length - 1);
                    System.out.println("BITFIELD: " + Arrays.toString(payload));
                }
            } else {
                System.out.println("❌ Invalid handshake from peer " + 1 + ", read " + read + " bytes");
            }
    }
    private static byte[] hexToBytes(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return data;
    }
}
