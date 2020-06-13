package io.wcheng.dataimporter;

import io.wcheng.dataimporter.common.JavafxUtils;
import io.wcheng.dataimporter.database.DBManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * Main class of this application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoggerManager loggerManager = new LoggerManager();
        ContextManager cm = ContextManager.getInstance();


        // init the application
        cm.loadAppConfig();


        cm.loadUserSettings();
        cm.setPrimaryStage(primaryStage);

        primaryStage.setTitle(cm.getAppConfig().getProperty("app.title"));
        primaryStage.setWidth(cm.getUserSettings().getPrefWidth());
        primaryStage.setHeight(cm.getUserSettings().getPrefHeight());

        String pagePath = "/view/selectFile.fxml";
        Scene scene = JavafxUtils.loadScene(pagePath);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void stop() {
        ContextManager cm = ContextManager.getInstance();
        if (cm != null) {
            // disconnect database
            DBManager dbManager = cm.getDbManager();
            if (dbManager != null) {
                System.out.println("Calling DBManager#disConnect...");
                dbManager.disConnect();
            }

            // save settings
            cm.saveUserSettings();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
