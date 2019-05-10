package main.java.Controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.LoginModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LoginController extends Main {
    private static final String USERNAME_EMPTY = "Please enter a valid username.";
    private static final String PASSWORD_EMPTY = "The password field is required.";
    private static final String LOGIN_FAIL = "Username or password incorrect";

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private CheckBox rememberMeCheckBox;

    public void initialize() {
        configurationView();
        if (prop.getProperty("USERNAME") != null) {
            username.setText(prop.getProperty("USERNAME"));
        }

        if ( prop.getProperty("PASSWORD") != null) {
            password.setText(prop.getProperty("PASSWORD"));
        }
    }
    @FXML
    protected void onLogin() {
        if (username.getText().isEmpty()) {
            createNotificationDialog(USERNAME_EMPTY, null, null);
        } else if(password.getText().isEmpty()) {
            createNotificationDialog(PASSWORD_EMPTY, null, null);
        } else {
            currentUsername = username.getText();
            currentPassword = password.getText();
            InetAddress ip = null;
            String macAddress = null;
            try {
                ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                byte[] mac = network.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                macAddress = sb.toString();
            } catch (UnknownHostException | SocketException e) {
                e.printStackTrace();

            }
            assert ip != null;
            String urlForLogin = SERVER_URL + "/user/login?userid=" + currentUsername +"&passwd="+ currentPassword + "&ipaddr=" + ip.getHostAddress() + "&macaddr=" + macAddress;


            Task<String> newTask = new Task<String>() {
                @Override
                public String call() {
                    return getResponseFromAPI(urlForLogin);
                }
            };
            loadingStage.show();

            newTask.setOnSucceeded(response -> {
                loadingStage.close();
                LoginModel loginModel = gson.fromJson((String) response.getSource().getValue(), LoginModel.class);
                linkId = loginModel.getPersonalInfo().getLink_id();
                if (loginModel.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
                    stage.hide();
                    logger.info("Login success");
                    if (rememberMeCheckBox.isSelected()
                            && !prop.getProperty("USERNAME").equals(currentUsername)) {
                        FileOutputStream config = null;
                        try {
                            config = new FileOutputStream("config.properties");
                            prop.setProperty("USERNAME", currentUsername);
                            prop.store(config,null);
                            config.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        changePageCampaignList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    createNotificationDialog(LOGIN_FAIL, null, null);
                }

            });
            new Thread(newTask).start();
        }
    }

    @FXML
    private void onConfigure() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CONFIGURATION));
        Region root = loader.load();
        ConfigurationController configurationController = loader.getController();
        Scene scene = new Scene(root);
        configurationController.setKeyBinding(scene);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.getIcons().add(new Image("/images/configuration.png"));
        newStage.setTitle("Configuration");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    private void configurationView() {
        setTextFieldLength(username, 10);
        setTextFieldLength(password, 216);
    }

    public void setKeyBinding(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changePageCampaignList() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(CAMPAIGN_LIST_FXML));
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(primScreenBounds.getWidth());
        stage.setHeight(primScreenBounds.getHeight());
        stage.setX(0);
        stage.setY(0);
        stage.setMaximized(true);
        stage.show();
    }
}
