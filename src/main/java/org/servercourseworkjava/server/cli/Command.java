package org.servercourseworkjava.server.cli;

public interface Command {
    String commandName = null;

    public void execute();
}