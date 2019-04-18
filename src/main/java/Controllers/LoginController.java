package main.java.Controllers;

import main.java.Main;
import main.java.Models.LoginModel;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController extends Main {
    private static final String USERNAME_EMPTY = "Please enter a valid username.";
    private static final String PASSWORD_EMPTY = "The password field is required.";
    private static final String LOGIN_FAIL = "Username or password incorrect";

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Text loginNotification;

    public void initialize() {
        configurationView();
        username.setText("trm");
        password.setText("12345678901");
    }
    @FXML
    protected void onLogin(ActionEvent event) throws Exception {
        if (username.getText().isEmpty()) {
            loginNotification.setText(USERNAME_EMPTY);
        } else if(password.getText().isEmpty()) {
            loginNotification.setText(PASSWORD_EMPTY);
        } else {
            boolean isLoginSuccess = validateUser(username.getText(), password.getText());
            if (isLoginSuccess) {
                changePageCampaignList();
            } else {
                loginNotification.setText(LOGIN_FAIL);
            }
        }
    }

    private boolean validateUser(String username, String password) {
        String url = SERVER_URL + "/user/login?userid=" + username +"&passwd="+ password;
        String response = getDataFromAPI(url);
        if (response != null) {
            LoginModel loginModel = gson.fromJson(response, LoginModel.class);
            String errorMsg = String.valueOf(loginModel.getResultinfo().getErrCd());
            setLinkId(loginModel.getPersonalInfo().getLink_id());
            return errorMsg.equals("0");
        } else {
            return false;
        }
    }

    private void configurationView() {
        setTextFieldLength(username, 10);
        setTextFieldLength(password, 216);
    }

    private void changePageCampaignList() throws Exception {
        setScene(CAMPAIGN_LIST_FXML);
        getWindow().setMaximized(true);
    }
}
