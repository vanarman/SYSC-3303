import java.io.IOException;
import java.net.*;

/**
 * Intermediate host class that receives packet from the client and send it to the server. Once receives response from
 * the server transfers it to the client.
 *
 * Support usage of the PacketSocketHelper class.
 *
 * @author  Dmytro Sytnik (VanArman)
 * @version 11 January 2019
 * @see PacketSocketHelper
 */
public class Host {

    private DatagramSocket receivingSocket, sendReceiveSocket;
    private DatagramPacket receivePacket, sendPacket, boncePacket;
    private final PacketSocketHelper psh;

    public static final int SERVER_PORT = 69;
    public static final int HOST_PORT = 23;

    /**
     * Default constructor of the intermediate host class.
     * Creates two sockets were one for receiving data and another one for sending data.
     * Creates an object of PacketSocketHelper class in order to send, receive and print data.
     *
     * Receive socket uses port 23 by default
     * @see PacketSocketHelper
     */
    public Host() {
        try {
            receivingSocket = new DatagramSocket(HOST_PORT);
            sendReceiveSocket = new DatagramSocket();
        } catch(SocketException se) {
            se.getMessage();
            System.exit(1);
        }

        psh = new PacketSocketHelper();
    }

    /**
     * Receive packet from the client
     */
    private void receivePacket() {
        receivePacket = psh.receivePacket(receivingSocket);
        psh.print(receivePacket, "Host", "received from Client");

    }

    /**
     * Send received (from the client) packet to the server (default server port is 69)
     * @param port int port of the server (default - 69)
     */
    private void sendPacketToServer(int port) {
        sendPacket = psh.sendPacket(sendReceiveSocket, receivePacket.getData(), port);
        psh.print(sendPacket, "Host", "sent to Server");
    }

    private void receiveResponseFromServer() {
        boncePacket = psh.receivePacket(sendReceiveSocket);
        psh.print(boncePacket, "Host", "received bonce from Server");
    }

    private void bonceBackResponse() {
        sendPacket = psh.sendPacket(sendReceiveSocket, boncePacket.getData(), receivePacket.getPort());
        psh.print(sendPacket, "Host", "sent to Client");
    }

    /**
     * Run all function in the defined sequence:
     * - receive packet from client
     * - send received packet to the server
     * - receive responce from the server
     * - send response to the client
     */
    public void run() {
        receivePacket();
        sendPacketToServer(SERVER_PORT);
        receiveResponseFromServer();
        bonceBackResponse();
    }

    /**
     * Entering point
     *
     * Create host object and perform "run" method in infinite loop
     *
     * @param args String[] not used in current context
     */
    public static void main(String[] args) {
        Host h = new Host();
        System.out.println("Host started");

        while(true) {
            h.run();
        }
    }
}
