package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.ContextManager;
import io.wcheng.dataimporter.common.StringUtils;
import io.wcheng.dataimporter.database.Column;
import io.wcheng.dataimporter.database.ConnectionInfo;
import io.wcheng.dataimporter.database.DBManager;
import io.wcheng.dataimporter.file.FileSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for headerMatch.fxml
 */
public class HeaderMatchController extends ControllerBase {

    @FXML private GridPane dataHeadersPane;
    @FXML private GridPane dbTableHeadersPane;

    @FXML private Text message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        ContextManager cm = ContextManager.getInstance();

        List<String> dataHeaders = cm.getFileManager().getHeaders();
        for (int i = 0; i < dataHeaders.size(); i++) {
            Text header = new Text(dataHeaders.get(i));
            dataHeadersPane.add(header, 0, i, 1, 1);
        }

        List<Column> dbTableColumns = cm.getDbManager().getTableInfo().getColumns();
        for (int i = 0; i < dbTableColumns.size(); i++) {
            Text header = new Text(dbTableColumns.get(i).getName());
            dbTableHeadersPane.add(header, 0, i, 1, 1);
        }
    }

    @FXML
    public void insert(ActionEvent event) {
        ContextManager cm = ContextManager.getInstance();
        FileSettings fs = cm.getFileManager().getFileSettings();
        DBManager dbManager = cm.getDbManager();
        ConnectionInfo connectionInfo = dbManager.getConnectionInfo();
        Connection conn = cm.getDbManager().getConnection();

        BufferedReader br = null;
        PreparedStatement ps = null;

        // create sql statement
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("INSERT INTO ");
        sqlsb.append(wrapWithQuotes(connectionInfo.getSchema()));
        sqlsb.append(".");
        sqlsb.append(wrapWithQuotes(connectionInfo.getTable()));
        sqlsb.append(" (");

        List<Column> columns = dbManager.getTableInfo().getColumns();
        for (int i = 0; i < columns.size(); i++) {
            sqlsb.append(wrapWithQuotes(columns.get(i).getName()));
            if (i < (columns.size() - 1)) {
                sqlsb.append(", ");
            }
        }

        sqlsb.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            sqlsb.append("?");
            if (i < (columns.size() - 1)) {
                sqlsb.append(", ");
            }
        }
        sqlsb.append(")");

        String sql = sqlsb.toString();
        System.out.println(sql);

        try {
            ps = conn.prepareStatement(sql);

            br = new BufferedReader(new FileReader(fs.getFilePath(), fs.getEncoding()));
            int rowCnt = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                // skip first row - headers
                if (rowCnt > 0) {
                    List<String> data = StringUtils.parseTextLine(line, fs.getDelimiter(), fs.getQualifier());
                    int r = addBatch(ps, columns, data);
                    if (r != 0) {
                        // TODO
                        System.out.println("Failed to add line to batch: " + line);
                    }
                }
                rowCnt++;

                if (rowCnt % 1000 == 0) {
                    // execute sql
                    ps.executeBatch();
                    conn.commit();
                }
            }

            ps.executeBatch();
            conn.commit();

            br.close();
            ps.close();

            message.setText("Inserted rows: " + (rowCnt-1));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TDDO should remove this catch block
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    private int addBatch(PreparedStatement ps, List<Column> columns, List<String> data) {
        try {
            for (int i = 0; i < data.size(); i++) {
                int parameterIndex = i + 1;
                Column c = columns.get(i);
                switch (c.getDataType()) {
                    case Types.BIGINT:
                        ps.setLong(parameterIndex, Long.valueOf(data.get(i)));
                        break;

                    case Types.INTEGER:
                        ps.setInt(parameterIndex, Integer.valueOf(data.get(i)));
                        break;

                    case Types.FLOAT:
                    case Types.REAL:
                        ps.setFloat(parameterIndex, Float.valueOf(data.get(i)));
                        break;

                    case Types.DOUBLE:
                        ps.setDouble(parameterIndex, Double.valueOf(data.get(i)));
                        break;

                    case Types.CHAR:
                    case Types.VARCHAR:
                        ps.setString(parameterIndex, data.get(i));
                        break;

                    case Types.DATE:
                        // TODO
                        ps.setDate(parameterIndex, Date.valueOf(data.get(i).replace('/', '-')));
                }
            }
            ps.addBatch();
            ps.clearParameters();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private String wrapWithQuotes(String s) {
        return "\"" + s + "\"";
    }
}
