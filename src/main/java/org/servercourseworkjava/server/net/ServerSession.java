package org.servercourseworkjava.server.net;

import org.servercourseworkjava.server.Server;

import java.io.*;
import java.net.Socket;

public class ServerSession implements Runnable {
    private final Socket socket;

    public ServerSession(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PacketReader reader = new PacketReader(socket.getInputStream());
            ResponseHandler responseHandler = new ResponseHandler(socket);

            while (true) {
                Packet packet = reader.readPacket();

                Server.logger.info("Received packet: (Type: " + packet.getType() + " Body: " + packet.getBody() + ")");

                switch (packet.getType()) {
                    case CREATE -> responseHandler.create(packet.getBody());
                    case READ -> responseHandler.read(packet.getBody());
                    case UPDATE -> responseHandler.update(packet.getBody());
                    case DELETE -> responseHandler.delete(packet.getBody());
                    default -> responseHandler.handleUnexpectedPacket();
                };

            }
        } catch (Exception e) {
            Server.logger.warn("[Connection] Client by address " + socket.getInetAddress() + " disconnected.");
        }
    }
}