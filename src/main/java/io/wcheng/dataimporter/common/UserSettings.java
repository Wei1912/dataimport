package io.wcheng.dataimporter.common;

import io.wcheng.dataimporter.database.ConnectionInfo;

public class UserSettings {

    private int prefWidth;
    private int prefHeight;

    private DbGuiSettings dbGuiSettings;

    public int getPrefWidth() {
        return prefWidth;
    }

    public void setPrefWidth(int prefWidth) {
        this.prefWidth = prefWidth;
    }

    public int getPrefHeight() {
        return prefHeight;
    }

    public void setPrefHeight(int prefHeight) {
        this.prefHeight = prefHeight;
    }

    public DbGuiSettings getDbGuiSettings() {
        return dbGuiSettings;
    }

    public void setDbGuiSettings(DbGuiSettings dbGuiSettings) {
        this.dbGuiSettings = dbGuiSettings;
    }

    public DbGuiSettings updateDbGuiSettings(ConnectionInfo connectionInfo) {
        if (dbGuiSettings == null) {
            dbGuiSettings = new DbGuiSettings();
        }
        dbGuiSettings.setServer(connectionInfo.getServer());
        dbGuiSettings.setPort(connectionInfo.getPort());
        dbGuiSettings.setDatabase(connectionInfo.getDatabase());
        dbGuiSettings.setSchema(connectionInfo.getSchema());
        dbGuiSettings.setTable(connectionInfo.getTable());
        dbGuiSettings.setUser(connectionInfo.getUser());
        return dbGuiSettings;
    }
}
