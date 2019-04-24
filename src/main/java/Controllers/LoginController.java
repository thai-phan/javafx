package main.java.Controllers;

import main.java.Main;
import main.java.Models.LoginModel;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileOutputStream;

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
    @FXML
    private Text loginNotification;

    public void initialize() {
        configurationView();
        if (prop.getProperty("USERNAME") != null && prop.getProperty("PASSWORD") != null) {
            username.setText(prop.getProperty("USERNAME"));
            password.setText(prop.getProperty("PASSWORD"));
        }
    }
    @FXML
    protected void onLogin(ActionEvent event) throws Exception {
        if (username.getText().isEmpty()) {
            loginNotification.setText(USERNAME_EMPTY);
        } else if(password.getText().isEmpty()) {
            loginNotification.setText(PASSWORD_EMPTY);
        } else {
            String usernameValue = username.getText();
            String passwordValue = password.getText();
            String urlForLogin = SERVER_URL + "/user/login?userid=" + usernameValue +"&passwd="+ passwordValue;
            String response = getResponseFromAPI(urlForLogin);
            LoginModel loginModel = gson.fromJson(response, LoginModel.class);
            linkId = loginModel.getPersonalInfo().getLink_id();
            if (loginModel.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
                if (rememberMeCheckBox.isSelected()
                        && (!prop.getProperty("USERNAME").equals(usernameValue) || !prop.getProperty("PASSWORD").equals(passwordValue))) {
                    FileOutputStream config = new FileOutputStream("config.properties");
                    prop.setProperty("USERNAME", usernameValue);
                    prop.setProperty("PASSWORD", passwordValue);
                    prop.store(config,null);
                    config.close();
                }

                changePageCampaignList();
            } else {
                loginNotification.setText(LOGIN_FAIL);
            }
        }
    }

    private void configurationView() {
        setTextFieldLength(username, 10);
        setTextFieldLength(password, 216);
    }

    private void changePageCampaignList() throws Exception {
        setSceneByView(CAMPAIGN_LIST_FXML);
        window.setMaximized(true);
    }
}
