package main.java.Controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import main.java.Main;
import main.java.Models.LoginModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    protected void onLogin() throws Exception {
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
            String response = getResponseFromAPI(urlForLogin);
            LoginModel loginModel = gson.fromJson(response, LoginModel.class);
            linkId = loginModel.getPersonalInfo().getLink_id();
            if (loginModel.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
                stage.hide();
                logger.info("Login success");
                if (rememberMeCheckBox.isSelected()
                        && !prop.getProperty("USERNAME").equals(currentUsername)) {
                    FileOutputStream config = new FileOutputStream("config.properties");
                    prop.setProperty("USERNAME", currentUsername);
                    prop.store(config,null);
                    config.close();
                }

                changePageCampaignList();
            } else {
                createNotificationDialog(LOGIN_FAIL, null, null);
            }
        }
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
        setSceneByView(CAMPAIGN_LIST_FXML);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(primScreenBounds.getWidth());
        stage.setHeight(primScreenBounds.getHeight());
        stage.setX(0);
        stage.setY(0);
        stage.setMaximized(true);
        stage.show();
    }
}
