package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.common.JavafxUtils;
import io.wcheng.dataimporter.ContextManager;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for fileGlance.fxml
 */
public class FileGlanceController extends ControllerBase {

    @FXML private ScrollPane scrollPane;
    @FXML private GridPane sampleData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        ContextManager cm = ContextManager.getInstance();

        // set headers
        List<String> headers = cm.getFileManager().getHeaders();
        for (int i = 0; i < headers.size(); i++) {
            Text text = new Text(headers.get(i));
            sampleData.add(text, i, 0, 1, 1);
        }

        // set rows
        List<String[]> rows = cm.getFileManager().getSampleData();
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                Text text = new Text(row[j]);
                sampleData.add(text, j, i+1, 1, 1);
            }
        }

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        // show all content vertically
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    @FXML
    public void toNextView(ActionEvent event) throws IOException {
        ContextManager cm = ContextManager.getInstance();
        String pagePath = "/view/dbLogin.fxml";
        Scene scene = JavafxUtils.loadScene(pagePath);
        cm.getPrimaryStage().setScene(scene);
    }
}
