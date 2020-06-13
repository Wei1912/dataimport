package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.common.JavafxUtils;
import io.wcheng.dataimporter.ContextManager;
import io.wcheng.dataimporter.database.DBManager;
import io.wcheng.dataimporter.database.Column;
import io.wcheng.dataimporter.database.ConnectionInfo;
import io.wcheng.dataimporter.database.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for tableInfo.fxml
 */
public class TableInfoController extends ControllerBase {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane tableFields;

    @FXML private Text databaseName;
    @FXML private Text schemaName;
    @FXML private Text tableName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        // Get table info
        ContextManager cm = ContextManager.getInstance();
        DBManager dbManager = cm.getDbManager();
        ConnectionInfo connectionInfo = dbManager.getConnectionInfo();

        // table info
        databaseName.setText(connectionInfo.getDatabase());
        schemaName.setText(connectionInfo.getSchema());
        tableName.setText(connectionInfo.getTable());

        // query table information
        try {
            dbManager.queryTableInfo();

            Table table = dbManager.getTableInfo();
            List<Column> columns = table.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                Text name = new Text(column.getName());
                Text type = new Text(column.getTypeName());
                tableFields.add(name, 0, i, 1, 1);
                tableFields.add(type, 1, i, 1, 1);
            }

            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            // show all content vertically
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO
        }
    }

    @FXML
    public void toNextView(ActionEvent event) throws IOException {
        ContextManager cm = ContextManager.getInstance();
        String pagePath = "/view/headerMatch.fxml";
        Scene scene = JavafxUtils.loadScene(pagePath);
        cm.getPrimaryStage().setScene(scene);
    }
}
