package main.java.Controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.Resultinfo;

import java.time.ZoneId;
import java.util.Date;

public class ScheduleDateController extends Main {
    @FXML
    private DatePicker scheduleDate;

    private String campaignId;
    private CampaignListController campaignListController;

    void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    void setCampaignListController(CampaignListController campaignListController) {
        this.campaignListController = campaignListController;
    }

    @FXML
    public void onSaveScheduleDate() {
        String scheduleDateValue = scheduleDate.getValue().toString().replace("-", "");
        String urlForRunSchedule = SERVER_URL + "/sch/run_link?link_id=" + linkId + "&date=" + scheduleDateValue + "&cm_id=" + campaignId;
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForRunSchedule);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            Resultinfo resultinfo = gson.fromJson((String) response.getSource().getValue(), Resultinfo.class);
            Stage currentStage = (Stage) scheduleDate.getScene().getWindow();
            if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
                createNotificationDialog(SUCCESS_HEADER, null, null);
                currentStage.close();
                campaignListController.loadCampaignTable();
            } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForRunSchedule);
            } else {
                createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), urlForRunSchedule);
                currentStage.close();
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
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
