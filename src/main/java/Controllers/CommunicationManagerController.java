package main.java.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.java.Main;
import main.java.Models.CampaignInfoModel;
import main.java.Models.DatabaseListModel;
import main.java.Models.ModelObject.Cms;
import main.java.Models.ModelObject.Resultinfo;
import main.java.Models.SelectedDatabaseModel;
import main.java.Models.ViewListModel;

;import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommunicationManagerController extends Main {
    @FXML
    private ComboBox<String> frequencyChoiceBox;
    @FXML
    private VBox dailyOptionList;
    @FXML
    private VBox monthlyOptionList;
    @FXML
    private VBox weeklyOptionList;
    @FXML
    private VBox yearlyOptionList;
    @FXML
    private TextField campaignName;
    @FXML
    private TextField campaignDescription;
    @FXML
    private DatePicker actlComStart;
    @FXML
    private DatePicker actlComEnd;
    @FXML
    private DatePicker planComStart;
    @FXML
    private DatePicker planComEnd;
    @FXML
    private DatePicker freqStart;
    @FXML
    private Button saveCampNameButton;
    @FXML
    private Button saveCampScheduleButton;
    @FXML
    private Button saveCampFrequencyOptButton;
    @FXML
    private Button saveCampDatabaseButton;
    @FXML
    private ComboBox<String> databaseListComboBox;
    @FXML
    private ComboBox<String> viewListComboBox;

    private String campaignId;

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    @Override
    public void initialize() {

        configurationView();
        loadDataFromAPI();
//        fakeData();
    }

    @FXML
    protected void saveCampaignName(ActionEvent event) throws Exception {
        String updatedName = campaignName.getText();
        String updatedDescription = campaignDescription.getText();
        String urlForUpdateCampInfo = SERVER_URL + "/cm/update/name?link_id=" + getLinkId() + "&cm_id=" + getCampaignId()
                + "&name=" + URLEncoder.encode(updatedName, "UTF-8") + "&desc=" + URLEncoder.encode(updatedDescription, "UTF-8");
        String responseForUpdateCampName = getResponseFromAPI(urlForUpdateCampInfo);
        Resultinfo resultinfo = gson.fromJson(responseForUpdateCampName, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {

        } else {

        }
        lg(responseForUpdateCampName);
    }

    @FXML
    private void onSaveCampSchedule() throws IOException {
        String updatedActlComStart = actlComStart.getValue().toString();
        String updatedActlComEnd = actlComEnd.getValue().toString();
        String updatedPlanComStart = planComStart.getValue().toString();
        String updatedPlanComEnd = planComEnd.getValue().toString();
        String urlForUpdateCampSchedule = SERVER_URL + "/cm/update/date?link_id=" + getLinkId() + "&cm_id=" + getCampaignId()
                + "&act_comm_sdt=" + updatedActlComStart + "&act_comm_edt=" + updatedActlComEnd
                + "&plan_comm_sdt=" + updatedPlanComStart + "&plan_comm_edt=" + updatedPlanComEnd;
        lg(urlForUpdateCampSchedule);
        String repsonseForUpdateCampSchedule = getResponseFromAPI(urlForUpdateCampSchedule);
        lg(repsonseForUpdateCampSchedule);
        Resultinfo resultinfo = gson.fromJson(repsonseForUpdateCampSchedule, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            lg("update date ss");
        } else if(resultinfo.getErrCd() == API_CODE_LOGOUT){
            logoutByExpireSession();
        }
    }


    @FXML
    private void onSaveCampFrequencyOpt() {

    }

    @FXML
    private void onSaveCampDatabase() {
        String currentDatabase = databaseListComboBox.getSelectionModel().getSelectedItem();
        String currentView = viewListComboBox.getSelectionModel().getSelectedItem();
        String url = SERVER_URL + "/dbs/update/cm?link_id=" + getLinkId() + "&cm_id=" + getCampaignId()
                + "&database_nm=" + currentDatabase + "&table_nm=" + currentView;
        String response = getResponseFromAPI(url);
        Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {

        } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {

        }
    }

    @FXML
    protected void cancelCampaign(ActionEvent event) throws Exception {
        setSceneByView(CAMPAIGN_LIST_FXML);
    }

    private void configurationView() {
        weeklyOptionList.setVisible(false);
        monthlyOptionList.setVisible(false);
        yearlyOptionList.setVisible(false);
        changeDateTimeToKrType(actlComStart);
        actlComStart.getEditor().setDisable(true);
        changeDateTimeToKrType(actlComEnd);
        actlComEnd.getEditor().setDisable(true);
        changeDateTimeToKrType(planComStart);
        planComStart.getEditor().setDisable(true);
        changeDateTimeToKrType(planComEnd);
        planComEnd.getEditor().setDisable(true);
        changeDateTimeToKrType(freqStart);
        setTextFieldLength(campaignName, 40);
        setTextFieldLength(campaignDescription, 100);


    }

    private void loadDataFromAPI() {
        if (getCampaignId() != null) {
            loadCampaignInfo();
            loadDatabaseList();
            fakeData();
        }
    }

    private void loadCampaignInfo() {
        String urlForCampaignInfo = SERVER_URL + "/cm/info?link_id=" + getLinkId()+ "&cm_id=" + getCampaignId();
        String responseForCampaignInfo = getResponseFromAPI(urlForCampaignInfo);
        CampaignInfoModel campaignInfoObj = gson.fromJson(responseForCampaignInfo, CampaignInfoModel.class);
        Cms campaign =  campaignInfoObj.getResultList().getCms().get(0);
        campaignName.setText(campaign.getName());
        campaignDescription.setText(campaign.getDescription());
        if (campaign.getActual_Communication_Start_Dt() != null) {
            actlComStart.setValue(LocalDate.parse(campaign.getActual_Communication_Start_Dt()));
        }
        if (campaign.getActual_Communication_End_Dt() != null) {
            actlComEnd.setValue(LocalDate.parse(campaign.getActual_Communication_End_Dt()));
        }
        if (campaign.getPlanned_Communication_Start_Dt() != null) {
            planComStart.setValue(LocalDate.parse(campaign.getPlanned_Communication_Start_Dt()));
        }
        if (campaign.getPlanned_Communication_End_Dt() != null) {
            planComEnd.setValue(LocalDate.parse(campaign.getPlanned_Communication_End_Dt()));
        }
    }

    private void changeDateTimeToKrType(DatePicker datePicker) {
        String pattern = "yyyy/MM/dd";
        datePicker.setPromptText(pattern.toLowerCase());
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format((date));
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });
    }


    public void fakeData() {
        String st[] = {"Daily", "Weekly", "Monthly", "Yearly"};
        frequencyChoiceBox.getItems().addAll(st);
        frequencyChoiceBox.getSelectionModel().select(0);
        frequencyChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String choiceValue = frequencyChoiceBox.getItems().get((Integer) newValue);
                switch (choiceValue) {
                    case "Daily": {
                        System.out.println("case1");
                        dailyOptionList.setVisible(true);
                        weeklyOptionList.setVisible(false);
                        monthlyOptionList.setVisible(false);
                        yearlyOptionList.setVisible(false);
                        break;
                    }
                    case "Weekly": {
                        dailyOptionList.setVisible(false);
                        weeklyOptionList.setVisible(true);
                        monthlyOptionList.setVisible(false);
                        yearlyOptionList.setVisible(false);
                        break;
                    }
                    case "Monthly": {
                        dailyOptionList.setVisible(false);
                        weeklyOptionList.setVisible(false);
                        monthlyOptionList.setVisible(true);
                        yearlyOptionList.setVisible(false);
                        break;
                    }
                    case "Yearly": {
                        dailyOptionList.setVisible(false);
                        weeklyOptionList.setVisible(false);
                        monthlyOptionList.setVisible(false);
                        yearlyOptionList.setVisible(true);
                        break;
                    }
                    default: {
                    }
                }
            }
        });
    }

    private void loadDatabaseList() {
        String urlForLoadDatabaseList = SERVER_URL + "/dbs/list/dbname?link_id=" + getLinkId();
        String responseForDatabaseList = getResponseFromAPI(urlForLoadDatabaseList);
        DatabaseListModel databaseListModel = gson.fromJson(responseForDatabaseList, DatabaseListModel.class);
        ObservableList<String> databaseList = FXCollections.observableArrayList();
        databaseListModel.getResultListTable().getDbNames().forEach(index -> databaseList.add(index.getDbname()));
        databaseListComboBox.setItems(databaseList);
        if (databaseListComboBox.getItems().size() >= 1) {
            String urlForSelectedDatabase = SERVER_URL + "/dbs/list/cm?link_id=" + getLinkId() + "&cm_id=" + getCampaignId();
            String responseForSelectedDatabase = getResponseFromAPI(urlForSelectedDatabase);
            SelectedDatabaseModel selectedDatabaseObj = gson.fromJson(responseForSelectedDatabase, SelectedDatabaseModel.class);
            String selectedDbName = selectedDatabaseObj.getDatasources().getDatasource().get(0).getDatabase_name_txt();
            databaseListComboBox.getSelectionModel().select(selectedDbName);
            loadViewListByDatabaseName(selectedDbName);
            databaseListComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                loadViewListByDatabaseName(newVal);
            });
        }
    }
    private void loadViewListByDatabaseName(String databaseName) {
        String urlForViewList = SERVER_URL + "/dbs/list/view?link_id=" + getLinkId() + "&dbname=" + databaseName;
        String responseForViewList = getResponseFromAPI(urlForViewList);
        lg(responseForViewList);
        ViewListModel viewListModel = gson.fromJson(responseForViewList, ViewListModel.class);
        ObservableList<String> viewList = FXCollections.observableArrayList();
        viewListModel.getResultList().getSetViews().forEach(index -> viewList.add(index.getTableName()));
        viewListComboBox.setItems(viewList);
        viewListComboBox.getSelectionModel().select(0);
    }
}
