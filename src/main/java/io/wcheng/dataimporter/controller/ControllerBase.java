package io.wcheng.dataimporter.controller;

import io.wcheng.dataimporter.common.StringUtils;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerBase implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(StringUtils.getFileName(url.getPath()) + " initializing.");
    }
}
