package dev.orhidea.commandqueue.utils.database.types;

import dev.orhidea.commandqueue.utils.database.Database;

import java.io.File;
import java.sql.DriverManager;
import java.util.LinkedHashMap;

public class SQLite extends Database {

    public SQLite(String dbName) {
        super(dbName);
    }

    @Override
    public LinkedHashMap<String, String> getTableContents() {
        return null;
    }

    @Override
    public void openConnection() throws Exception {
        File folder = new File("databases/" + this.getActivePlugin().getName());

        if(!folder.exists()) folder.mkdirs();

        File file = new File(folder, this.databaseName + ".db");

        Class.forName("org.sqlite.JDBC");

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        getActivePlugin().log("Connected to database '" + file.getName() + "'");
    }

    @Override
    public void closeConnection() throws Exception {
        this.connection.close();
    }
}
