package main.java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.Resultinfo;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class ScheduleDateController extends Main {
    @FXML
    private DatePicker scheduleDate;

    private String campaignId;
    private CampaignListController campaignListController;

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setCampaignListController(CampaignListController campaignListController) {
        this.campaignListController = campaignListController;
    }

    @FXML
    public void onSaveScheduleDate() throws IOException {
        String scheduleDateValue = scheduleDate.getValue().toString().replace("-", "");
        String urlForRunSchedule = SERVER_URL + "/sch/run_link?link_id=" + linkId + "&date=" + scheduleDateValue + "&cm_id=" + campaignId;
        String response = getResponseFromAPI(urlForRunSchedule);
        Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
        Stage currentStage = (Stage) scheduleDate.getScene().getWindow();
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            createNotificationDialog("Success", resultinfo.getErrString(), null);
            currentStage.close();
            campaignListController.loadCampaignTable();
        } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT, urlForRunSchedule);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), urlForRunSchedule);
            currentStage.close();
        }
    }

    public void initialize() {
        changeDateTimeToKrTypeAndDisableEditor(scheduleDate);
        scheduleDate.setValue((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void onCancelScheduleDate(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        currentStage.close();
    }
}
