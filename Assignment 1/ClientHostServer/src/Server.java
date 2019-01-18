import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Server class that that receives packets from the intermediate host and validate message.
 * If message is valid (read or write) send response to the host.
 *
 * For valid read request send 0 3 0 1 (4 byte)
 * For valid write request send 0 4 0 0 (4 byte)
 * For invalid request throws an exception and stops execution with system exit 1
 *
 * Support usage of the PacketSocketHelper class.
 *
 * @author  Dmytro Sytnik (VanArman)
 * @version 11 January 2019
 * @see PacketSocketHelper
 */
public class Server {

    private DatagramSocket receivingSocket, sendReceiveSocket;
    private DatagramPacket receivePacket, boncePacket;
    private static final PacketSocketHelper PSH = new PacketSocketHelper();

    /**
     * Default constructor of the server class.
     * Creates two sockets were one for receiving data and another one for sending data.
     * Creates an object of PacketSocketHelper class in order to send, receive and print data.
     *
     * Receive socket uses port 69 by default
     * @see PacketSocketHelper
     */
    public Server() {
        try {
            receivingSocket = new DatagramSocket(Host.SERVER_PORT);
        } catch(SocketException se) {
            System.out.println(se.getMessage());
            System.exit(1);
        }
    }

    /**
     * Receive packet from the intermediate host and validate it.
     *
     * If validation passed - send confirmation response to the host.
     * If validation fail - throws an exception and stops execution of the program with system exit 1
     */
    private void receivePacket() {
        receivePacket = PSH.receivePacket(receivingSocket);
        PSH.print(receivePacket, "Server", "received message from host");

        receivePacketValidation(receivePacket);
    }

    /**
     * Validation method that check is the received package is valid (follows a specified pattern)
     * 0 [1|2] message.byte 0 mode.byte 0
     *
     * Response if received package is valid.
     *
     * @param rp DatagramPacket received packed to validate
     */
    private void receivePacketValidation(DatagramPacket rp) {
        boolean validPacket = true;
        byte data[] = rp.getData();

        if(data[0] != (byte) 0) {
            this.throwException("Message format is invalid!\nFirst byte should be \'0\'");
            validPacket = false;
        }

        if( !(data[1] == (byte) 1 || data[1] == (byte) 2)) {
            this.throwException("Message format is invalid!\nSecond byte should be either \'1\' or \'2\'.\nReceived value is: " +String.valueOf(data[1]));
            validPacket = false;
        }

        List<List> arr = this.byteSplitter(Arrays.copyOfRange(data, 2, data.length));

        byte[] mode = new byte[arr.get(1).size()];

        for(int i = 0; i < mode.length; i++) {
            mode[i] = ((Byte) arr.get(1).get(i)).byteValue();
        }

        String modeStr = new String(mode).toLowerCase();

        if( !(modeStr.equals("netascii") || modeStr.equals("octet")) ) {
            this.throwException("Specified mode can not be recognized.\nShould be either \"octet\" or \"netascii\".\nReceived mode: " +modeStr);
            validPacket = false;
        }

        byte[] bonceBackMessage = new byte[4];

        if(validPacket) {
            System.out.println("Message is valid!!");

            bonceBackMessage[0] = (byte) 0;
            bonceBackMessage[2] = (byte) 0;
            bonceBackMessage[3] = (byte) 0;

            if(data[1] == (byte) 1) {
                bonceBackMessage[1] = (byte) 3;
                bonceBackMessage[3] = (byte) 1;
            }

            if(data[1] == (byte) 2) {
                bonceBackMessage[1] = (byte) 4;
            }
        } else {
            System.out.println("*Message is invalid!!*");
            System.exit(1);
        }

        delayTime(7000);

        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        boncePacket = PSH.sendPacket(sendReceiveSocket, bonceBackMessage, rp.getPort());
        PSH.print(boncePacket, "Server", "bonce back to Host");

        sendReceiveSocket.close();
    }

    /**
     * Split byte array into two dimension array of bytes.
     * Separate given byte array using "0" byte separator
     *
     * @param data byte[] array of bytes to separate
     * @return List list array of arrays
     */
    private List byteSplitter(byte[] data) {
        List<List> arr = new ArrayList<>();
        List<Byte> temp = new ArrayList<>();

        for(byte b : data) {
            if(b == (byte) 0) {
                if(temp.size() > 0) {
                    arr.add(temp);
                    temp = new ArrayList<>();
                }
                continue;
            }

            temp.add(Byte.valueOf(b));
        }

        return arr;
    }

    /**
     * Generate delay using Threads
     *
     * @param mSec int tome of delay in milliseconds
     */
    private void delayTime(int mSec) {
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException e ) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Exception handler that throws an exception with specified text
     *
     * @param text String message that would be shown in the console
     */
    private void throwException(String text) {
        try {
            throw new Exception(text);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Run all function in the defined sequence:
     * - receive packet from host
     * - * validate
     * - * generate response message (if applicable)
     * # generates delay of 5 seconds
     * - send response message to the host
     */
    public void run() {
        receivePacket();
    }

    /**
     * Entering point
     *
     * Create server object and perform "run" method in infinite loop
     *
     * @param args String[] not used in current context
     */
    public static void main(String[] args) {
        Server s = new Server();
        System.out.println("Server started");

        while(true) {
            s.run();
        }
    }
}
