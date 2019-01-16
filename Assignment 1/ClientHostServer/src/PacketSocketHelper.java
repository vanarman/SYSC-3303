import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Helper class that adds opportunity to minify the code and avoid code repeating.
 * The class do receive data from the given socket, send data from the given socket and print received message.
 *
 * @author  Dmytro Sytnik (VanArman)
 * @version 11 January 2019
 */
public final class PacketSocketHelper {

    private final static int MAX_BUFFER_SIZE = 30;

    /**
     * Receive packet from a defined socket
     *
     * @param receiveSocket DatagramSocket socket that used to receive data
     * @return DatagramPacket received packet
     */
    public DatagramPacket receivePacket(DatagramSocket receiveSocket) {
        byte dataReceived[] = new byte[MAX_BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(dataReceived, dataReceived.length);

        try {
            receiveSocket.receive(receivePacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return receivePacket;
    }

    /**
     * Sending message as a byte array using specific datagram socket and port (Used local IP addres)
     *
     * @param ds DatagramSocket socket that used to send data
     * @param message byte[] array of bytes that would be send
     * @param port int port number to send data through
     * @return DatagramPacket packet that has been sent. If cannot sent - null.
     */
    public DatagramPacket sendPacket(DatagramSocket ds, byte[] message, int port) {
        try {
            return this.sendPacket(ds, message, InetAddress.getLocalHost(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Sending message as a byte array using specific datagram socket and port with specified IP address
     *
     * @param ds DatagramSocket socket that used to send data
     * @param message byte[] array of bytes that would be send
     * @param IP InetAddess object that contain IP address of the host or server
     * @param port int port number to send data through
     * @return DatagramPacket packet that has been sent.
     */
    public DatagramPacket sendPacket(DatagramSocket ds, byte[] message, InetAddress IP, int port) {
        DatagramPacket sendPacket = new DatagramPacket(message, message.length, IP, port);

        return this.sendPacket(ds, sendPacket);
    }

    /**
     * Sending packet using specified socket
     *
     * @param sendSocket DatagramSocket socket that would be used to send packet
     * @param sendPacket DatagramPacket packet that would be sent
     * @return DatagramPacket packet that has been sent
     */
    public DatagramPacket sendPacket(DatagramSocket sendSocket, DatagramPacket sendPacket) {
        try {
            sendSocket.send(sendPacket);
        } catch(IOException ioe) {
            ioe.getMessage();
            System.exit(1);
        }

        return sendPacket;
    }

    /**
     * Printing information about the host such as host IP, port, length of the message and content (as string and bytes)
     *
     * @param dp DatagramPacket packet that information has to pe printed
     * @param currentHost String what host (client, server) does the operation
     * @param action String action and subject of action (eg. "sent to Host")
     */
    public void print(DatagramPacket dp, String currentHost, String action) {
        System.out.println("\n" +currentHost+ ":\n\t Packet " +action+ ":");
        System.out.println("\t\tFrom host: " + dp.getAddress() +":"+  dp.getPort());
        System.out.println("\t\tLength: "    + dp.getData().length);

        System.out.print("\t\tContaining [byte]: ");
        for(byte m : dp.getData()) {
            System.out.print(m+ " ");
        }

        System.out.println("\n\t\tContaining: " + new String(dp.getData()) +"\n\n");
    }
}
