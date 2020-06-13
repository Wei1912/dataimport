package io.wcheng.dataimporter;

import com.google.gson.Gson;
import io.wcheng.dataimporter.common.UserSettings;
import io.wcheng.dataimporter.database.DBManager;
import io.wcheng.dataimporter.file.FileManager;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class ContextManager {

    // Application Configuration
    private Properties appConfig;

    // User Settings
    private UserSettings userSettings;

    // Node
    private Stage primaryStage;

    // Import data
    private FileManager fileManager = new FileManager();

    // database
    private DBManager dbManager = new DBManager();

    private static ContextManager cm;

    private ContextManager() {}

    public static ContextManager getInstance() {
        if (cm == null) {
            cm = new ContextManager();
        }
        return cm;
    }

    public Properties getAppConfig() {
        return appConfig;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void loadAppConfig() {
        appConfig = new Properties();

        String appConfigName = "appconfig.properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(appConfigName)) {
            if (input != null) {
                appConfig.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserSettings() {
        userSettings = new UserSettings();
        File settingFile = new File(appConfig.getProperty("user.setting.file"));
        if (settingFile.exists() && !settingFile.isDirectory()) {
            // load user settings from file
            Gson gson = new Gson();
            try (Reader reader = new FileReader(settingFile)) {
                userSettings = gson.fromJson(reader, UserSettings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // default settings from app config
            userSettings.setPrefWidth(Integer.valueOf(appConfig.getProperty("default.pref.width")));
            userSettings.setPrefHeight(Integer.valueOf(appConfig.getProperty("default.pref.height")));
        }
    }

    public void saveUserSettings() {
        File settingFile = new File(appConfig.getProperty("user.setting.file"));
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(settingFile)) {
            gson.toJson(userSettings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
