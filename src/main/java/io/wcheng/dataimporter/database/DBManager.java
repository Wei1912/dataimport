package io.wcheng.dataimporter.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager of database
 */
public class DBManager {

    private String dbType;
    private ConnectionInfo connectionInfo;
    private Table tableInfo;
    private Connection connection;

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Connect to database
     */
    public void connect() throws SQLException {
        String server = connectionInfo.getServer();
        String port = connectionInfo.getPort();
        String database = connectionInfo.getDatabase();
        String user = connectionInfo.getUser();
        String password = connectionInfo.getPassword();

        dbType = "postgresql";

        String url = "jdbc:" + dbType + "://" + server + ":" + port + "/" + database;

        System.out.println("Connecting to " + url);

        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);

        // TODO write log
        System.out.println("Connected to " + url);
    }

    /**
     * Disconnect from database
     */
    public void disConnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Disconnected already, or has not connected to database yet");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Query all database names
     * @return
     * @throws SQLException
     */
    public List<String> queryDatabases() throws SQLException {
        List<String> dbNames = new ArrayList<>();
        ResultSet rs = connection.getMetaData().getCatalogs();
        while (rs.next()) {
            dbNames.add(rs.getString("TABLE_CAT"));
        }
        return dbNames;
    }

    /**
     * Query schema names
     * @param databaseName
     * @return
     * @throws SQLException
     */
    public List<String> querySchemas(String databaseName) throws SQLException {
        List<String> schemaNames = new ArrayList<>();
        ResultSet rs = connection.getMetaData().getSchemas(databaseName, null);
        while (rs.next()) {
            schemaNames.add(rs.getString("TABLE_SCHEM"));
        }
        return schemaNames;
    }

    /**
     * Query table names
     * @param databaseName
     * @param schemaName
     * @return
     * @throws SQLException
     */
    public List<String> queryTables(String databaseName, String schemaName) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        ResultSet rs = connection.getMetaData().getTables(databaseName, schemaName, null, null);
        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    /**
     * Query column info
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<Column> queryColumns(String databaseName, String schemaName, String tableName) throws SQLException {
        List<Column> columns = new ArrayList<>();
        ResultSet rs = connection.getMetaData().getColumns(databaseName, schemaName, tableName, null);
        while (rs.next()) {
            Column c = new Column();
            c.setName(rs.getString("COLUMN_NAME"));
            c.setDataType(rs.getInt("DATA_TYPE"));
            c.setTypeName(rs.getString("TYPE_NAME"));
            c.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls ? false : true);
            c.setDefaultValue(rs.getString("COLUMN_DEF"));
            columns.add(c);
        }
        return columns;
    }

    /**
     * Query Table info
     * @return
     * @throws SQLException
     */
    public void queryTableInfo() throws SQLException {
        if (tableInfo == null) {
            tableInfo = new Table(connectionInfo.getTable());
            tableInfo.setColumns(queryColumns(connectionInfo.getDatabase(),
                    connectionInfo.getSchema(), connectionInfo.getTable()));
        }
    }

    public Table getTableInfo() {
        return tableInfo;
    }
}
