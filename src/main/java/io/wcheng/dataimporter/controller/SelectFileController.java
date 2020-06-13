package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.common.JavafxUtils;
import io.wcheng.dataimporter.ContextManager;
import io.wcheng.dataimporter.common.StringUtils;
import io.wcheng.dataimporter.file.FileSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Controller for selectFile.fxml
 *
 */
public class SelectFileController extends ControllerBase {

    @FXML private Text selectedFile;

    @FXML private TextField delimiterField;

    @FXML private TextField qualifierField;

    @FXML private TextField encodingField;

    @FXML
    public void selectFile(ActionEvent event) {
        ContextManager cm = ContextManager.getInstance();

        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(cm.getPrimaryStage());
        if (file != null) {
            Scene scene =  cm.getPrimaryStage().getScene();
            selectedFile.setText(file.getAbsolutePath());
            selectedFile.setVisible(true);

            VBox part2 = (VBox) scene.lookup("#part2");
            part2.setVisible(true);
        }
    }

    @FXML
    public void loadFile(ActionEvent event) throws IOException {
        String filePath = selectedFile.getText().trim();
        String delimiterStr = delimiterField.getText().trim();
        String qualifierStr = qualifierField.getText().trim();
        String encodingStr = encodingField.getText().trim();

        // is valid file path?
        if (StringUtils.isEmpty(filePath)) {
            // empty file path
            System.out.println("Please select a valid file.");
            // TODO
        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                // invalid file path
                System.out.println("Please select a valid file.");
                // TODO
            }
        }

        // set default values if empty
        char delimiter = StringUtils.isEmpty(delimiterStr) ? ',' : delimiterStr.charAt(0);
        char qualifier = StringUtils.isEmpty(qualifierStr) ? '"' : qualifierStr.charAt(0);

        Charset encoding = Charset.defaultCharset();
        if (StringUtils.isNotEmpty(encodingStr)) {
            // is Encoding supported?
            try {
                encoding = Charset.forName(encodingStr);
            } catch (UnsupportedCharsetException e) {
                // TODO
            }
        }

        FileSettings fileSettings = new FileSettings();
        fileSettings.setFilePath(filePath);
        fileSettings.setDelimiter(delimiter);
        fileSettings.setQualifier(qualifier);
        fileSettings.setEncoding(encoding);

        ContextManager cm = ContextManager.getInstance();
        cm.getFileManager().setFileSettings(fileSettings);

        String pagePath = "/view/fileGlance.fxml";
        Scene scene = JavafxUtils.loadScene(pagePath);
        cm.getPrimaryStage().setScene(scene);
    }
}
