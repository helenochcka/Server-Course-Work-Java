package org.servercourseworkjava.server.net;

public class Packet {
    private final char INTERNAL_SEPARATOR = (char) 47;
    private final PacketType type;
    private final String body;

    public Packet(String rawString) {
        String[] rawStringTokens = rawString.split(String.valueOf(INTERNAL_SEPARATOR));

        this.type = PacketType.values()[Integer.parseInt(rawStringTokens[0])];

        if (rawStringTokens.length == 1) {
            this.body = "";
        } else {
            this.body = rawStringTokens[1];
        }
    }

    public PacketType getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

}
