package com.patrick;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

class ReceiveThread extends Thread
{
    ReceiveThread()
    {
        // Name the thread, so it can be recognized. Super (in this case) calls the constructor of the Thread class.
        super("UDPDemo");
    }

    // Run is called when a thread is started. Notice how in the Main class run is not called, but start.
    @Override
    public void run()
    {
        try
        {
            // Bind the socket to a port. This does not start the listen just yet.
            DatagramSocket socket = new DatagramSocket(666);

            // Prevent flooding of the screen by placing sender IP's in a hashmap.
            HashMap<String, LocalTime> spamList = new HashMap<>();

            while(true)
            {
                // Create a packet to store incoming messages in. Size is 1024, so the messages can't be too large.
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

                // Start listening for incoming messages. Note this method blocks until a message comes in.
                socket.receive(packet);

                // Get the sender IP from the packet, convert it from a string.
                String ip = packet.getAddress().toString();

                // Get the current local time, so compensated for timezone. Timezone compensation is not necessary,
                // but at least also use the localtime for the remainder of the code.
                LocalTime now = LocalTime.now();

                // Set the previous time to the lowest possible value, for the first comparison.
                LocalTime previous = LocalTime.MIN;

                // If the IP is known in the hashmap, get the time they last sent a message and
                // remove the IP from the hashmap.
                if(spamList.containsKey(ip))
                {
                    previous = spamList.get(ip);
                    spamList.remove(ip);
                }

                // Put the IP with the current time in the spamlist.
                spamList.put(ip, now);

                // Set a time to get the interval from.
                LocalTime temp = LocalTime.from(previous);

                // Get the interval in seconds between the previous time and now.
                long seconds = temp.until(now, ChronoUnit.SECONDS);

                // If the interval is, or is over, a second, show the incoming message.
                if (seconds >= 1)
                {
                    System.out.println(ip + ": " + new String(packet.getData()));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}