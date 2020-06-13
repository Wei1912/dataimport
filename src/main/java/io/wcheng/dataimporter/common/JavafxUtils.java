package io.wcheng.dataimporter.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class JavafxUtils {
    public static Scene loadScene(String pagePath) throws IOException {
        try {
            Parent root = FXMLLoader.load(JavafxUtils.class.getResource(pagePath));
            Scene scene = new Scene(root);
            return scene;
        } catch (IOException e) {
            System.out.println("Unable to load " + pagePath);
            throw e;
        }
    }
}
