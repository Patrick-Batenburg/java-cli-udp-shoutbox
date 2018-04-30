package com.patrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main
{
    private static String host = "127.0.0.1";
    private static int port = 666;

    public static void main(String[] args) throws IOException
    {
        // Create and start a separate thread, so that waiting for console input doesn't block receiving packets.
        ReceiveThread receiveThread = new ReceiveThread();
        receiveThread.start();

        // Create the UDP socket to send data with.
        DatagramSocket socket = new DatagramSocket();

        // Easy reading from the console. Make it a buffered reader, so you can read whole lines at once.
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        while(true)
        {
            // Read a line.
            String line = consoleInput.readLine();

            // Convert the line (String) to a byte array.
            byte[] sendData = line.getBytes();

            // Create a packet, containing the byte array, a definition of how big the package is, an InetAddress
            // and the port to send the packet to.
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(host), port);

            // Use the socket to send the packet.
            socket.send(sendPacket);
        }
    }
}