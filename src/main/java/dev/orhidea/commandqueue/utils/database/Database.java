package dev.orhidea.commandqueue.utils.database;

import com.massivecraft.massivecore.Engine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public abstract class Database extends Engine implements IDatabase, IDatabaseMethods {

    protected final String databaseName;
    protected Connection connection;

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);

        if(active) {
            try {
                if(this.connection != null && !this.connection.isClosed()) {
                    throw new Exception("The connection is already connected and stable");
                }

                openConnection();
            } catch (Exception ex) {
                getActivePlugin().log("An error occurred while trying to connect to the database '" + this.databaseName + "': " + ex.getMessage());
            }
        } else {
            try {
                closeConnection();
            } catch (Exception ex) {
                getActivePlugin().log("An error occurred while trying to shut down the connection to the database '" + this.databaseName + "'" + ex.getMessage());
            }
        }
    }

    @Override
    public String getPrimaryKey() {
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public String getDeleteTable(String table) {
        return "DROP TABLE IF EXISTS `" + table + "`";
    }

    @Override
    public String getCreateTable(String table) {
        return "CREATE TABLE IF NOT EXISTS `" + table + "` " + getCreateTableVariables();
    }

    @Override
    public String getPullAll(String table) {
        return "SELECT * FROM `" + table + "`";
    }

    @Override
    public String getPullRow(String table, String row) {
        return "SELECT * FROM `" + table + "` WHERE `" + row + "`=?";
    }

    @Override
    public String getPushRow(String table) {
        return "INSERT INTO `" + table + "`" + getPushRowKeys() + " VALUES " + getPushRowValues();
    }

    @Override
    public String getDeleteRow(String table, String row) {
        return "DELETE FROM `" + table + "` WHERE `" + row + "`=?";
    }

    @Override
    public String parseTableId(String input) {
        return input
                .replace(",", "")
                .replace(";", "")
                .replace("-", "_")
                .replace(".", "_");
    }

    protected String getPushRowKeys(boolean duplicateKeyUpdate) {
        StringBuilder stringBuilder = new StringBuilder(duplicateKeyUpdate? "" : "(");

        Iterator<Map.Entry<String, String>> iterator = getTableContents().entrySet().iterator();

        String primaryKey = getPrimaryKey();

        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            if(primaryKey != null && duplicateKeyUpdate) {
                if(entry.getKey().equalsIgnoreCase(primaryKey)) continue;
            }

            stringBuilder.append("`").append(entry.getKey()).append("`");

            if(duplicateKeyUpdate) {
                stringBuilder.append("=? ");
            }

            if(iterator.hasNext()) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(duplicateKeyUpdate? "" : ")");
                break;
            }
        }

        return stringBuilder.toString();
    }

    protected String getPushRowValues() {
        StringBuilder stringBuilder = new StringBuilder("(");

        Iterator<Map.Entry<String, String>> iterator = getTableContents().entrySet().iterator();

        while (iterator.hasNext()) {
            iterator.next();
            stringBuilder.append("?");

            if(iterator.hasNext()) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(")");
            }
        }

        return stringBuilder.toString();
    }

    private String getPushRowKeys() {
        return getPushRowKeys(false);
    }

    private String getCreateTableVariables() {
        StringBuilder stringBuilder = new StringBuilder("(");

        Iterator<Map.Entry<String, String>> iterator = getTableContents().entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            stringBuilder.append("`").append(entry.getKey()).append("`").append(" ").append(entry.getValue());

            if(iterator.hasNext()) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(")");
            }
        }

        return stringBuilder.toString();
    }
}
