package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.common.JavafxUtils;
import io.wcheng.dataimporter.ContextManager;
import io.wcheng.dataimporter.common.StringUtils;
import io.wcheng.dataimporter.common.DbGuiSettings;
import io.wcheng.dataimporter.database.DBManager;
import io.wcheng.dataimporter.database.ConnectionInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for dbLogin.fxml
 */
public class DBLoginController extends ControllerBase {

    @FXML private TextField serverField;
    @FXML private TextField portField;
    @FXML private TextField databaseField;
    @FXML private TextField schemaField;
    @FXML private TextField tableField;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;

    private boolean hasInputError = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        ContextManager cm = ContextManager.getInstance();
        DbGuiSettings dbGuiSettings = cm.getUserSettings().getDbGuiSettings();

        // set default values
        serverField.setText(dbGuiSettings.getServer());
        portField.setText(dbGuiSettings.getPort());
        databaseField.setText(dbGuiSettings.getDatabase());
        schemaField.setText(dbGuiSettings.getSchema());
        tableField.setText(dbGuiSettings.getTable());
        userField.setText(dbGuiSettings.getUser());
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        // check inputs
        String server = getValueFromInputField(serverField, true);
        String port = getValueFromInputField(portField, true);
        String database = getValueFromInputField(databaseField, false);
        String schema = getValueFromInputField(schemaField, false);
        String table = getValueFromInputField(tableField, false);
        String user = getValueFromInputField(userField, false);
        String password = getValueFromInputField(passwordField, false);

        if (hasInputError) {
            return;
        }

        ConnectionInfo ci = new ConnectionInfo();
        ci.setServer(server);
        ci.setPort(port);
        ci.setDatabase(database);
        ci.setSchema(schema);
        ci.setTable(table);
        ci.setUser(user);
        ci.setPassword(password);

        ContextManager cm = ContextManager.getInstance();
        DBManager dbManager = cm.getDbManager();
        dbManager.setConnectionInfo(ci);

        // update DB connection settings
        cm.getUserSettings().updateDbGuiSettings(ci);

        // connect to database
        try {
            dbManager.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String pagePath = "/view/tableInfo.fxml";
        Scene scene = JavafxUtils.loadScene(pagePath);
        cm.getPrimaryStage().setScene(scene);
    }

    private String getValueFromInputField(TextField field, boolean required) {
        String value = field.getText();
        if (required && StringUtils.isEmpty(value)) {
            this.hasInputError = true;
            field.getStyleClass().add("error");
        }
        return value;
    }
}
