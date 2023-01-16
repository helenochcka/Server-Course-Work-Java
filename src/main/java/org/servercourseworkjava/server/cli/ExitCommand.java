package org.servercourseworkjava.server.cli;

public final class ExitCommand implements Command {
    @Override
    public void execute() {
        System.exit(0);
    }
}

