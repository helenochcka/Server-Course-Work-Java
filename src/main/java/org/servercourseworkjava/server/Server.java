package org.servercourseworkjava.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servercourseworkjava.server.cli.CommandHandler;
import org.servercourseworkjava.server.cli.ArgumentsParser;
import org.servercourseworkjava.server.net.ConnectionHandler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final Logger logger = LogManager.getLogger();
    private static final List<Socket> clientSockets = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService sessionsThreadPool = Executors.newFixedThreadPool(5);
    private static Thread listenerThread = null;

    public static void main(String[] args) {
        //*** parsing arguments ***
        ArgumentsParser argumentsParser = new ArgumentsParser(args);

        int port = argumentsParser.parsePort();

        //*** start listening ***
        ConnectionHandler connectionHandler = new ConnectionHandler(port, clientSockets, sessionsThreadPool);
        listenerThread = new Thread(connectionHandler);
        listenerThread.start();

        //*** handle commands ***
        CommandHandler commandHandler = new CommandHandler(System.in);
        while(true) {
            commandHandler.handleCommand();
        }
    }
}
