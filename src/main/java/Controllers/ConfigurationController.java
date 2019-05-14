package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.Main;
import main.java.Models.ModelObject.ControlBindingObj;


public class ConfigurationController extends Main {
    @FXML
    private ComboBox<ControlBindingObj> protocolComboBox;
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;

    private ControlBindingObj httpPROT = new ControlBindingObj("http", "1");
    private ControlBindingObj httpsPROT = new ControlBindingObj("https", "2");

    public void initialize() {
        configurationView();
        try {
            loadDataFromConfigFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        SERVER_URL = new
    }

    private void configurationView() {
        ObservableList<ControlBindingObj> protocolList = FXCollections.observableArrayList();
        protocolList.addAll(httpPROT, httpsPROT);
        protocolComboBox.setItems(protocolList);
    }

    private void loadDataFromConfigFile() throws IOException {
        FileInputStream config = new FileInputStream("config.properties");
        prop.load(config);
        String serverUrl = prop.getProperty("SERVER_URL");
        if (serverUrl.matches("^https://.+")) {
            protocolComboBox.getSelectionModel().select(httpsPROT);
        } else if (serverUrl.matches("^http://.+")) {
            protocolComboBox.getSelectionModel().select(httpPROT);
        } else {
            protocolComboBox.getSelectionModel().selectFirst();
        }
        Pattern urlPattern = Pattern.compile("^https?://(\\S+):(\\d+)/.+$");
        Matcher matcher = urlPattern.matcher(serverUrl);
        if (matcher.find()) {
            serverField.setText(matcher.group(1));
            portField.setText(matcher.group(2));
        }
        config.close();
    }

    @FXML
    public void onSaveConfig() {
        String protocol = protocolComboBox.getSelectionModel().getSelectedItem().getName();
        String server = serverField.getText().replaceAll("\\s", "");
        String port = portField.getText().replaceAll("\\s", "");
        if (!server.matches("^\\S+$")) {
            createNotificationDialog(ALERT_HEADER, "Server value invalid", null);
        } else if (!port.matches("^\\d*$")) {
            createNotificationDialog(ALERT_HEADER, "Port value is number", null);
        } else {
            SERVER_URL = protocol + "://" + server + (!port.isEmpty() ? ":"+ port : "")+ "/rest";
            FileOutputStream config;
            try {
                config = new FileOutputStream("config.properties");
                prop.setProperty("SERVER_URL", SERVER_URL);
                prop.store(config,null);
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
                createNotificationDialog(ALERT_HEADER, e.getMessage() + "\nRun program in Administration Permission", null);
            }
            Stage currentWindow = (Stage) protocolComboBox.getScene().getWindow();
            currentWindow.close();
        }
    }

    public void setKeyBinding(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onSaveConfig();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) ((Node)actionEvent.getTarget()).getScene().getWindow();
        currentWindow.close();
    }

}
