package dev.orhidea.commandqueue.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface IDatabase {

    LinkedHashMap<String, String> getTableContents();

    Connection getConnection() throws SQLException;

    String getPrimaryKey();

    void openConnection() throws Exception;

    void closeConnection() throws Exception;

}
