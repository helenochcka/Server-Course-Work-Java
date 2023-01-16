package org.servercourseworkjava.server.net;

import org.servercourseworkjava.server.Server;
import org.servercourseworkjava.server.model.Entry;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResponseHandler {
    private final String CONNECTION_STRING = "jdbc:sqlite:data.db";
    private final char SUBINTERNAL_SEPARATOR = (char) 59;
    private final Socket socket;

    public ResponseHandler(Socket socket) {
        this.socket = socket;
    }

    public boolean create(String body) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            Entry entry = Entry.parseEntry(body, SUBINTERNAL_SEPARATOR);
            PreparedStatement selectStatement = connection.prepareStatement(
                "INSERT OR REPLACE INTO entry (id, proofOfOwnership, owner, realEstateType, location, squareFootage, dateOfPurchase)" +
                "VALUES ('"+ entry.getId() +"', '" + entry.getProofOfOwnership() + "', '" + entry.getOwner() + "', '" +
                        entry.getRealEstateType() + "', '" + entry.getLocation() + "', '" + entry.getSquareFootage() + "', '" +
                        entry.getDateOfPurchase() + "');");
            selectStatement.execute();

            Server.logger.info("Executed CREATE or UPDATE for user: " + socket.getInetAddress());

        } catch (Exception e) {
            Server.logger.error("CREATE or UPDATE error for user: " + socket.getInetAddress());
            e.printStackTrace();
            try {
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public boolean read(String pattern) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {

            ResultSet resultSet;
            PreparedStatement selectStatement;

            if (pattern.isEmpty()) {
                selectStatement = connection.prepareStatement("SELECT * FROM entry");
            } else {
                selectStatement = connection.prepareStatement(
                "SELECT * FROM entry WHERE id like '%"
                    + pattern
                    + "%' OR proofOfOwnership like '%"
                    + pattern
                    + "%' OR owner like '%"
                    + pattern
                    + "%' OR realEstateType like '%"
                    + pattern
                    + "%' OR location like '%"
                    + pattern
                    + "%' OR squareFootage = '"
                    + pattern
                    + "' OR dateOfPurchase like '%"
                    + pattern
                    + "%';");
            }

            resultSet = selectStatement.executeQuery();

            String packedEntry = null;
            StringBuilder stringBuilder = new StringBuilder();
            while(resultSet.next()) {
                Entry entry = new Entry(
                    resultSet.getString("id"),
                    resultSet.getString("proofOfOwnership"),
                    resultSet.getString("owner"),
                    resultSet.getString("realEstateType"),
                    resultSet.getString("location"),
                    resultSet.getInt("squareFootage"),
                    resultSet.getString("dateOfPurchase"));
                packedEntry = entry.pack();
                stringBuilder.append(packedEntry);
            }

            if (!stringBuilder.isEmpty()) {
                writeToSocket(PacketType.READ.ordinal() + stringBuilder.toString());
            } else {
                writeToSocket("-");
            }

            Server.logger.info("Executed READ for user: " + socket.getInetAddress());

        } catch (Exception e) {
            Server.logger.error("READ error for user: " + socket.getInetAddress());
            e.printStackTrace();
            try {
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public boolean update(String body) {
        return create(body);
    }

    public boolean delete(String body) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            Entry entry = Entry.parseEntry(body, SUBINTERNAL_SEPARATOR);
            PreparedStatement selectStatement = connection.prepareStatement("DELETE FROM entry " + "WHERE id = '" + entry.getId() + "';");
            selectStatement.execute();

            Server.logger.info("Executed DELETE for user: " + socket.getInetAddress());
        } catch (Exception e) {
            Server.logger.error("DELETE error for user: " + socket.getInetAddress());
            e.printStackTrace();
            try {
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void writeToSocket(String data) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.write(data);
        printWriter.write("#");
        printWriter.flush();
    }

    public void handleUnexpectedPacket() {
        Server.logger.info("Executed UNKNOWN request for user: " + socket.getInetAddress());
    }
}
