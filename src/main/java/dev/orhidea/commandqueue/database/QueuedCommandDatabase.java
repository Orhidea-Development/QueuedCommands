package dev.orhidea.commandqueue.database;

import dev.orhidea.commandqueue.entity.wrapper.CommandWrapper;
import dev.orhidea.commandqueue.utils.Callback;
import dev.orhidea.commandqueue.utils.MapUtil;
import dev.orhidea.commandqueue.utils.database.types.SQLite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class QueuedCommandDatabase extends SQLite {
    private static final QueuedCommandDatabase i = new QueuedCommandDatabase();
    public static QueuedCommandDatabase get() { return i; }
    private static final String TABLE_NAME = "main";

    public QueuedCommandDatabase() {
        super("queued-commands");
    }

    @Override
    public LinkedHashMap<String, String> getTableContents() {
        return MapUtil.linkedMap(
                "timeReceivedMs", "LONG",
                "name", "TEXT",
                "command", "TEXT"
        );
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);

        if(!active) return;

        setupTable();
    }
    public void addStoredCommand(CommandWrapper commandWrapper) {
        String id = parseTableId(TABLE_NAME);

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getPushRow(id));
            preparedStatement.setLong(1, commandWrapper.getTimeReceivedMs());
            preparedStatement.setString(2, commandWrapper.getPlayerName());
            preparedStatement.setString(3, commandWrapper.getCommand());

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getStoredCommands(String name, Callback<List<CommandWrapper>> callback, boolean delete) {
        List<CommandWrapper> commandWrappers = new ArrayList<>();

        if(name == null) {
            callback.call(commandWrappers);
            return;
        }

        String id = parseTableId(TABLE_NAME);

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getPullRow(id, "name"));
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet != null) {
                while(resultSet.next()) {
                    String playerName = resultSet.getString(2);
                    long timeReceivedMs = resultSet.getLong(1);
                    String command = resultSet.getString(3);

                    commandWrappers.add(new CommandWrapper(timeReceivedMs, playerName, command));
                }
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        callback.call(commandWrappers);

        if(delete)
            deleteStoredCommanc(
                    commandWrappers.stream()
                            .map(CommandWrapper::getTimeReceivedMs)
                            .collect(Collectors.toList())
            );
    }

    private void deleteStoredCommanc(List<Long> times) {
        String id = parseTableId(TABLE_NAME);

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getDeleteRow(id, "timeReceivedMs"));

            for(long timeReceivedMs : times) {
                preparedStatement.setLong(1, timeReceivedMs);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void setupTable() {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCreateTable(parseTableId(TABLE_NAME)));

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
