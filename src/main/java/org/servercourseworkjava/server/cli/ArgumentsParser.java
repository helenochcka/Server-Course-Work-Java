package org.servercourseworkjava.server.cli;

import org.apache.commons.cli.*;
import org.servercourseworkjava.server.Server;

public class ArgumentsParser {
    public CommandLine cmd;

    public ArgumentsParser(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            cmd = parser.parse(createParseOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            Server.logger.fatal("[CLI-parser] Cant read your arguments. You may be running the program incorrectly.");
            System.exit(-1);
        }
    }

    private Options createParseOptions() {
        Options options = new Options();

        Option notDefaultPortOption = new Option("p", "port", true, "port");
        notDefaultPortOption.setArgs(1);
        notDefaultPortOption.setOptionalArg(true);
        notDefaultPortOption.setArgName("port for listening");

        options.addOption(notDefaultPortOption);

        return options;
    }

    public int parsePort() {
        if (cmd.hasOption("port")) {
            return Integer.parseInt(cmd.getOptionValue("port"));
        }

        return 8000;
    }
}
