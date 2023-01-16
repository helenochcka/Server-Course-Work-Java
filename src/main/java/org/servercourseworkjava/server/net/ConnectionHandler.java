package org.servercourseworkjava.server.net;

import org.servercourseworkjava.server.Server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;

public final class ConnectionHandler implements Runnable {
    private final List<Socket> clientSockets;
    private final ExecutorService threadPool;
    private final int port;

    public ConnectionHandler(int port, List<Socket> clientSockets, ExecutorService threadPool) {
        this.clientSockets = clientSockets;
        this.threadPool = threadPool;

        if (1 <= port && port <= 65535) {
            this.port = port;
        } else {
            this.port = 8000;
        }
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket clientSocket;
        boolean isUniqueAddress;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Server.logger.fatal("[Connections] Error when opening the socket...", e);
            System.exit(-1);
            return;
        }

        try {
            Server.logger.info("[Connections] Port " + port + " is listening now by address: " +
                    InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            Server.logger.error("[Connections] Can't get host IP address.", e);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();

                isUniqueAddress = true;
                for (Socket s: clientSockets) {
                    String storedAddress = s.getInetAddress().getHostAddress();
                    String newAddress = clientSocket.getInetAddress().getHostName();
                    if (storedAddress.equals(newAddress))
                        isUniqueAddress = false;
                }

                if (isUniqueAddress) {
                    clientSockets.add(clientSocket);
                    Server.logger.info("[Connections] New client with IP: " +
                        clientSockets.get(clientSockets.size() - 1).getInetAddress().getHostAddress() +
                        " connected.");
                } else {
                    Server.logger.error("[Connections] Already existing client tried to connect");
                    clientSocket.close();
                    continue;
                }

                ServerSession session = new ServerSession(clientSocket);
                threadPool.submit(session);

            } catch (IOException e) {
                Server.logger.error("[Connections] Bind operation failed, or the socket is already bound...", e);
            }
        }
    }
}
