package main.java.Controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.Resultinfo;

import java.io.IOException;


public class Password extends Main {
    @FXML
    private TextField currentPwd;
    @FXML
    private TextField newPwd;
    @FXML
    private TextField confirmPwd;
    @Override
    public void initialize() {

    }

    private boolean validateField() {
        if (currentPwd.getText().isEmpty()) {
            createNotificationDialog(ALERT_HEADER, "Old password require.", null);
            currentPwd.requestFocus();
            return false;
        } else if (newPwd.getText().isEmpty()) {
            createNotificationDialog(ALERT_HEADER, "New password require.", null);
            newPwd.requestFocus();
            return false;
        } else if (confirmPwd.getText().isEmpty()) {
            createNotificationDialog(ALERT_HEADER, "Confirm password require.", null);
            confirmPwd.requestFocus();
            return false;
        } else if(!confirmPwd.getText().equals(newPwd.getText())) {
            createNotificationDialog(ALERT_HEADER, "Confirm password not match with new password.", null);
            confirmPwd.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void onSaveNewPassword() {
        boolean isValidated = validateField();
        if (isValidated) {
            String oldPwdValue = currentPwd.getText();
            String newPwdValue = newPwd.getText();
            String urlForSaveNewPassword = SERVER_URL + "/user/chg/password?link_id=" + linkId
                    + "&passwd=" + oldPwdValue + "&newpasswd=" + newPwdValue;
            Task<String> createTask = new Task<String>() {
                @Override
                public String call() {
                    return getResponseFromAPI(urlForSaveNewPassword);
                }
            };
            loadingStage.show();
            createTask.setOnSucceeded(event -> {
                String response = (String) event.getSource().getValue();
                Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
                switch (resultinfo.getErrCd()) {
                    case API_CODE_SUCCESS:
                        createNotificationDialog(SUCCESS_HEADER, null, urlForSaveNewPassword);
                        Stage currentWindow = (Stage) currentPwd.getScene().getWindow();
                        currentWindow.close();
                        break;
                    case API_CODE_LOGOUT:
                        try {
                            logoutByExpireSession(urlForSaveNewPassword);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), urlForSaveNewPassword);
                        break;
                }
                loadingStage.hide();
            });
            new Thread(createTask).start();
        }
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) ((Node)actionEvent.getTarget()).getScene().getWindow();
        currentWindow.close();
    }
}
