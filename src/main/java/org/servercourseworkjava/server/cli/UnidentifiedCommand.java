package org.servercourseworkjava.server.cli;

public final class UnidentifiedCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Command is unidentified.");
    }
}
