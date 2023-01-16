package org.servercourseworkjava.server.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PacketReader {
    private final char EXTERNAL_SEPARATOR = (char) 35;
    private final InputStream inputStream;

    public PacketReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Packet readPacket() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        byte i;

        while ((i = (byte)in.read()) != EXTERNAL_SEPARATOR) {
            stringBuilder.append((char) i);
        }

        String rawString = stringBuilder.toString();

        Packet packet = new Packet(rawString);

        return packet;
    }
}
