package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.java.Main;
import main.java.Models.*;
import main.java.Models.ModelObject.Cms;
import main.java.Models.ModelObject.ControlBindingObj;
import main.java.Models.ModelObject.Resultinfo;
import main.java.Models.ModelObject.SchInfos;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CommunicationManagerController extends Main {
    private static final String NO_END_DATE = "99991231";
    private static final String PATTERN_DAY_LIST = "\\d{1,2}(,\\d{1,2})*";
    private static final String PATTERN_ORDINAL_WEEK = "\\d#\\d";
    private static final String PATTERN_LAST_OF_WEEK = "\\dL";
    @FXML
    private ComboBox<ControlBindingObj> frequencyComboBox;
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
    private DatePicker freqEnd;
    @FXML
    private RadioButton endDateRadio;
    @FXML
    private RadioButton noEndDateRadio;
    @FXML
    private ComboBox<String> databaseListComboBox;
    @FXML
    private ComboBox<String> viewListComboBox;
    @FXML
    private CheckBox weeklySundayCheck;
    @FXML
    private CheckBox weeklyMondayCheck;
    @FXML
    private CheckBox weeklyTuesdayCheck;
    @FXML
    private CheckBox weeklyWednesdayCheck;
    @FXML
    private CheckBox weeklyThursdayCheck;
    @FXML
    private CheckBox weeklyFridayCheck;
    @FXML
    private CheckBox weeklySaturdayCheck;
    @FXML
    private RadioButton monthlyDayRadio;
    @FXML
    private ComboBox<ControlBindingObj> monthlyOrdinalCombo;
    @FXML
    private ComboBox<ControlBindingObj> monthlyDayCombo;
    @FXML
    private RadioButton monthlySpecificRadio;
    @FXML
    private TextField monthlyDateList;
    @FXML
    private ComboBox<ControlBindingObj> yearlyOrdinalCombo;
    @FXML
    private ComboBox<ControlBindingObj> yearlyDayCombo;
    @FXML
    private ComboBox<ControlBindingObj> yearlyMonthCombo;
    @FXML
    private ToggleGroup frequencyDefault;
    @FXML
    private ToggleGroup frequencyMonthly;


    private String campaignId;
    private Map<String, CheckBox> weekDayMap = new HashMap<>();

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignId() {
        return campaignId;
    }


    @Override
    public void initialize() {
        if (getCampaignId() != null) {
            configurationView();
            try {
                loadDataFromAPI();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            addListener();
        }
    }

    @FXML
    protected void saveCampaignName(ActionEvent event) throws Exception {
        String updatedName = campaignName.getText();
        String updatedDescription = campaignDescription.getText();
        String urlForUpdateCampInfo = SERVER_URL + "/cm/update/name?link_id=" + linkId + "&cm_id=" + getCampaignId()
                + "&name=" + URLEncoder.encode(updatedName, "UTF-8") + "&desc=" + URLEncoder.encode(updatedDescription, "UTF-8");
        String responseForUpdateCampName = getResponseFromAPI(urlForUpdateCampInfo);
        Resultinfo resultinfo = gson.fromJson(responseForUpdateCampName, Resultinfo.class);
        notificationForAction(resultinfo);

    }

    private void notificationForAction(Resultinfo resultinfo) throws IOException {
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            createNotificationDialog(SUCCESS_TITLE, null);
        } else if(resultinfo.getErrCd() == API_CODE_LOGOUT){
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString());
        }
    }
    @FXML
    private void onSaveCampSchedule() throws IOException {
        String updatedActlComStart = actlComStart.getValue().toString();
        String updatedActlComEnd = actlComEnd.getValue().toString();
        String updatedPlanComStart = planComStart.getValue().toString();
        String updatedPlanComEnd = planComEnd.getValue().toString();
        String urlForUpdateCampSchedule = SERVER_URL + "/cm/update/date?link_id=" + linkId + "&cm_id=" + getCampaignId()
                + "&act_comm_sdt=" + updatedActlComStart + "&act_comm_edt=" + updatedActlComEnd
                + "&plan_comm_sdt=" + updatedPlanComStart + "&plan_comm_edt=" + updatedPlanComEnd;
        String responseForUpdateCampSchedule = getResponseFromAPI(urlForUpdateCampSchedule);
        Resultinfo resultinfo = gson.fromJson(responseForUpdateCampSchedule, Resultinfo.class);
        notificationForAction(resultinfo);
    }

    @FXML
    private void onSaveCampFrequencyOpt() throws IOException {
        String frequencyType = frequencyComboBox.getSelectionModel().getSelectedItem().getId();
        String schStartDate = freqStart.getValue().toString().replace("-", "");
        String schEndDate = endDateRadio.isSelected() ? freqEnd.getValue().toString().replace("-", "") : NO_END_DATE;
        String optionNumber = "1";
        String cronDd = "*";
        String cronMm = "*";
        String cronWk = "*";
        boolean isUrlComplete = false;

        switch (frequencyType) {
            case "1":
                isUrlComplete = true;
                break;
            case "2":
                ArrayList<String> dayList = new ArrayList<>();
                weekDayMap.forEach((key, value)-> {
                    if (value.isSelected()) {
                        dayList.add(key );
                    }
                });
                if (dayList.size() > 0) {
                    cronWk = String.join(",", dayList);
                    isUrlComplete = true;
                } else {
                    createNotificationDialog("Select one day in week.", null);
                }

                break;
            case "3":
                if (monthlyDayRadio.isSelected()) {
                    String monthlyOrdinalId = monthlyOrdinalCombo.getSelectionModel().getSelectedItem().getId();
                    switch (monthlyOrdinalId) {
                        case "6":
                            cronDd = "L";
                            break;
                        case "5":
                            cronWk = monthlyDayCombo.getSelectionModel().getSelectedItem().getId() + "L";
                            break;
                        default:
                            cronWk = monthlyDayCombo.getSelectionModel().getSelectedItem().getId() + "#" + monthlyOrdinalId;
                            break;
                    }
                    isUrlComplete = true;

                } else if(monthlySpecificRadio.isSelected()) {
                    optionNumber = "2";
                    if (monthlyDateList.getText().matches(PATTERN_DAY_LIST)) {
                        cronDd = monthlyDateList.getText();
                        isUrlComplete = true;
                    } else if(monthlyDateList.getText().isEmpty()){
                        createNotificationDialog("Day list require.", null);
                    } else {
                        createNotificationDialog("Day list wrong format.", null);
                    }
                }
                break;
            case "4":
                String yearlyOrdinalId = yearlyOrdinalCombo.getSelectionModel().getSelectedItem().getId();
                if (yearlyOrdinalId.equals("5")) {
                    cronWk = yearlyDayCombo.getSelectionModel().getSelectedItem().getId() + "L";
                } else {
                    cronWk = yearlyDayCombo.getSelectionModel().getSelectedItem().getId() + "#" + yearlyOrdinalId;
                }
                cronMm = yearlyMonthCombo.getSelectionModel().getSelectedItem().getId();
                break;
        }

        if (isUrlComplete) {
            String urlForUpdateSchedule = SERVER_URL + "/sch/update/cm?link_id=" + linkId + "&cm_id=" + getCampaignId()
                    + "&type_cd=" + frequencyType + "&start_dttm=" + schStartDate + "&end_dttm=" + schEndDate
                    + "&opt_num=" + optionNumber + "&cron_Dd=" + cronDd + "&cron_Mm=" + cronMm + "&cron_Week=" + URLEncoder.encode(cronWk, "UTF-8");
            String response = getResponseFromAPI(urlForUpdateSchedule);
            Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
            notificationForAction(resultinfo);
        }
    }

    @FXML
    private void onSaveCampDatabase() throws IOException {
        String currentDatabase = databaseListComboBox.getSelectionModel().getSelectedItem();
        String currentView = viewListComboBox.getSelectionModel().getSelectedItem();
        String url = SERVER_URL + "/dbs/update/cm?link_id=" + linkId + "&cm_id=" + getCampaignId()
                + "&database_nm=" + currentDatabase + "&table_nm=" + currentView;
        String response = getResponseFromAPI(url);
        Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
        notificationForAction(resultinfo);
    }

    @FXML
    protected void cancelCampaign(ActionEvent event) throws Exception {
        setSceneByView(CAMPAIGN_LIST_FXML);
    }

    private void configurationView() {
        weekDayMap.put("1", weeklySundayCheck);
        weekDayMap.put("2", weeklyMondayCheck);
        weekDayMap.put("3", weeklyTuesdayCheck);
        weekDayMap.put("4", weeklyWednesdayCheck);
        weekDayMap.put("5", weeklyThursdayCheck);
        weekDayMap.put("6", weeklyFridayCheck);
        weekDayMap.put("7", weeklySaturdayCheck);

        monthlyOrdinalCombo.getItems().addAll(
                new ControlBindingObj("First", "1"),
                new ControlBindingObj("Second", "2"),
                new ControlBindingObj("Third", "3"),
                new ControlBindingObj("Four", "4"),
                new ControlBindingObj("Last", "5"),
                new ControlBindingObj("Last day", "6"));
        monthlyOrdinalCombo.getSelectionModel().selectFirst();

        monthlyDayCombo.getItems().addAll(
                new ControlBindingObj("Sunday", "1"),
                new ControlBindingObj("Monday", "2"),
                new ControlBindingObj("Tuesday", "3"),
                new ControlBindingObj("Wednesday", "4"),
                new ControlBindingObj("Thursday", "5"),
                new ControlBindingObj("Friday", "6"),
                new ControlBindingObj("Saturday", "7"));
        monthlyDayCombo.getSelectionModel().selectFirst();

        yearlyOrdinalCombo.getItems().addAll(
                new ControlBindingObj("First", "1"),
                new ControlBindingObj("Second", "2"),
                new ControlBindingObj("Third", "3"),
                new ControlBindingObj("Four", "4"),
                new ControlBindingObj("Last", "5"));
        yearlyOrdinalCombo.getSelectionModel().selectFirst();

        yearlyDayCombo.getItems().addAll(
                new ControlBindingObj("Sunday", "1"),
                new ControlBindingObj("Monday", "2"),
                new ControlBindingObj("Tuesday", "3"),
                new ControlBindingObj("Wednesday", "4"),
                new ControlBindingObj("Thursday", "5"),
                new ControlBindingObj("Friday", "6"),
                new ControlBindingObj("Saturday", "7"));
        yearlyDayCombo.getSelectionModel().selectFirst();

        yearlyMonthCombo.getItems().addAll(
                new ControlBindingObj("January", "1"),
                new ControlBindingObj("February", "2"),
                new ControlBindingObj("March", "3"),
                new ControlBindingObj("April", "4"),
                new ControlBindingObj("May", "5"),
                new ControlBindingObj("June", "6"),
                new ControlBindingObj("July", "7"),
                new ControlBindingObj("August", "1"),
                new ControlBindingObj("September", "2"),
                new ControlBindingObj("October", "3"),
                new ControlBindingObj("November", "4"),
                new ControlBindingObj("December", "5"));
        yearlyMonthCombo.getSelectionModel().selectFirst();

        changeDateTimeToKrTypeAndDisableEditor(actlComStart);
        changeDateTimeToKrTypeAndDisableEditor(actlComEnd);
        changeDateTimeToKrTypeAndDisableEditor(planComStart);
        changeDateTimeToKrTypeAndDisableEditor(planComEnd);
        changeDateTimeToKrTypeAndDisableEditor(freqStart);
        changeDateTimeToKrTypeAndDisableEditor(freqEnd);
        setTextFieldLength(campaignName, 40);
        setTextFieldLength(campaignDescription, 100);

        monthlyOrdinalCombo.setDisable(true);
        monthlyDayCombo.setDisable(true);
        monthlyDateList.setDisable(true);
        freqEnd.setDisable(true);
    }

    private void addListener() {
        frequencyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFrequencyBox(newValue.getId());
        });
        frequencyDefault.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(endDateRadio)) {
                freqEnd.setDisable(false);
            } else {
                freqEnd.setDisable(true);
            }
        });
        frequencyMonthly.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(monthlyDayRadio)) {
                monthlyOrdinalCombo.setDisable(false);
                monthlyDayCombo.setDisable(false);
                monthlyDateList.setDisable(true);
            } else {
                monthlyOrdinalCombo.setDisable(true);
                monthlyDayCombo.setDisable(true);
                monthlyDateList.setDisable(false);
            }
        });
        monthlyOrdinalCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getName().equals("Last day")) {
                monthlyDayCombo.setDisable(true);
            } else {
                monthlyDayCombo.setDisable(false);
            }
        });
        databaseListComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                loadViewListByDatabaseName(newVal, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateFrequencyBox(String id) {
        switch (id) {
            case "1": {
                dailyOptionList.setVisible(true);
                weeklyOptionList.setVisible(false);
                monthlyOptionList.setVisible(false);
                yearlyOptionList.setVisible(false);
                break;
            }
            case "2": {
                dailyOptionList.setVisible(false);
                weeklyOptionList.setVisible(true);
                monthlyOptionList.setVisible(false);
                yearlyOptionList.setVisible(false);
                break;
            }
            case "3": {
                dailyOptionList.setVisible(false);
                weeklyOptionList.setVisible(false);
                monthlyOptionList.setVisible(true);
                yearlyOptionList.setVisible(false);
                break;
            }
            case "4": {
                dailyOptionList.setVisible(false);
                weeklyOptionList.setVisible(false);
                monthlyOptionList.setVisible(false);
                yearlyOptionList.setVisible(true);
                break;
            }
        }
    }

    private void loadDataFromAPI() throws IOException, ParseException {
        loadCampaignInfo();
        loadDatabaseList();
        loadFrequencyList();
    }

    private void loadCampaignInfo() {
        String urlForCampaignInfo = SERVER_URL + "/cm/info?link_id=" + linkId+ "&cm_id=" + getCampaignId();
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

    private void changeDateTimeToKrTypeAndDisableEditor(DatePicker datePicker) {
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
        datePicker.setEditable(false);
    }


    private void loadFrequencyList() throws IOException, ParseException {
        String urlForFrequencyList = SERVER_URL + "/dbs/list/cd?link_id=" + linkId + "&cdtype=2";
        String response = getResponseFromAPI(urlForFrequencyList);
        StatusListModel statusListObj = gson.fromJson(response,  StatusListModel.class);
        if (statusListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<ControlBindingObj> statusList = FXCollections.observableArrayList();
            statusListObj.getCdList().getCds().forEach(index -> statusList.add(new ControlBindingObj(index.getName(), index.getCd())));
            frequencyComboBox.setItems(statusList);
            loadFrequencyDetail();


        } else if(statusListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT){
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        }  else {
            createNotificationDialog(ERROR_HEADER, statusListObj.getResultinfo().getErrString());
        }
    }

    private void loadFrequencyDetail() throws IOException, ParseException {
        String urlForFrequencyDetail = SERVER_URL + "/sch/info/cm?link_id=" + linkId + "&cm_id=" + getCampaignId();
        String response = getResponseFromAPI(urlForFrequencyDetail);
        CampaignScheduleModel campaignScheduleObj = gson.fromJson(response, CampaignScheduleModel.class);
        if (campaignScheduleObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            String selectedFrequencyType = campaignScheduleObj.getSchInfoList().getSchInfoList().get(0).getSchedule_type_cd();
            ControlBindingObj selectedFrequency = frequencyComboBox.getItems()
                    .stream().filter(index -> index.getId().equals(selectedFrequencyType))
                    .findAny()
                    .orElse(null);
            frequencyComboBox.getSelectionModel().select(selectedFrequency);
            // Bind data to field list
            SchInfos scheduleDetail = campaignScheduleObj.getSchInfoList().getSchInfoList().get(0);
            updateFrequencyBox(scheduleDetail.getSchedule_type_cd());
            Date freqStartDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(scheduleDetail.getSchedule_start_dttm_ftm());
            freqStart.setValue(freqStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            if (!scheduleDetail.getSchedule_end_dttm_ftm().equals(NO_END_DATE)) {
                endDateRadio.setSelected(true);
                Date freqEndDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(scheduleDetail.getSchedule_end_dttm_ftm());
                freqEnd.setDisable(false);
                freqEnd.setValue(freqEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            } else {
                noEndDateRadio.setSelected(true);
            }

            switch (scheduleDetail.getSchedule_type_cd()){
                case "2":
                    bindDateForWeeklyFrequency(scheduleDetail);
                    break;
                case "3":
                    bindDateForMonthlyFrequency(scheduleDetail);
                    break;
                case "4":
                    bindDateForYearlyFrequency(scheduleDetail);
                    break;
            }


        } else if (campaignScheduleObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, campaignScheduleObj.getResultinfo().getErrString());
        }
    }

    private void bindDateForWeeklyFrequency(SchInfos schedule) {
        switch (schedule.getOpt_num()) {
            case "1":
                if (schedule.getCron_Week().matches(PATTERN_DAY_LIST)) {
                    List<String> dayList = Arrays.asList(schedule.getCron_Week().split(","));
                    dayList.forEach(index -> {
                       if (weekDayMap.get(index) != null ) {
                           weekDayMap.get(index).setSelected(true);
                       }
                    });
                }
                break;
        }
    }

    private void bindDateForMonthlyFrequency(SchInfos schedule) {
        switch (schedule.getOpt_num()) {
            case "1":
                monthlyDayRadio.setSelected(true);
                monthlyOrdinalCombo.setDisable(false);
                monthlyDayCombo.setDisable(false);
                if (schedule.getCron_Week().matches(PATTERN_ORDINAL_WEEK)){
                    String dayPos = schedule.getCron_Week().split("#")[0];
                    String ordinalPos = schedule.getCron_Week().split("#")[1];
                    monthlyOrdinalCombo.getSelectionModel().select(
                            monthlyOrdinalCombo.getItems().stream().filter(index -> index.getId().equals(ordinalPos)).findAny().orElse(null)
                    );
                    monthlyDayCombo.getSelectionModel().select(
                            monthlyDayCombo.getItems().stream().filter(index -> index.getId().equals(dayPos)).findAny().orElse(null)
                    );
                } else if(schedule.getCron_Week().matches(PATTERN_LAST_OF_WEEK)){
                    String dayPos = schedule.getCron_Week().substring(0, 1);
                    ControlBindingObj ordinalPos = new ControlBindingObj("Last", "5");
                    monthlyOrdinalCombo.getSelectionModel().select(ordinalPos);
                    monthlyDayCombo.getSelectionModel().select(
                            monthlyDayCombo.getItems().stream().filter(index -> index.getId().equals(dayPos)).findAny().orElse(null)
                    );
                } else if(schedule.getCron_Dd().equals("L")){
                    monthlyOrdinalCombo.getSelectionModel().selectLast();
                }
                break;
            case "2":
                monthlySpecificRadio.setSelected(true);
                monthlyDateList.setDisable(false);
                if (schedule.getCron_Dd().matches(PATTERN_DAY_LIST)) {
                    monthlyDateList.setText(schedule.getCron_Dd());
                }
        }
    }

    private void bindDateForYearlyFrequency(SchInfos schedule) {
        switch (schedule.getOpt_num()) {
            case "1":
                if (schedule.getCron_Week().matches(PATTERN_ORDINAL_WEEK)){
                    String dayPos = schedule.getCron_Week().split("#")[0];
                    String ordinalPos = schedule.getCron_Week().split("#")[1];

                    yearlyOrdinalCombo.getSelectionModel().select(
                            yearlyOrdinalCombo.getItems().stream().filter(index -> index.getId().equals(ordinalPos)).findAny().orElse(null)
                    );
                    yearlyDayCombo.getSelectionModel().select(
                            yearlyDayCombo.getItems().stream().filter(index -> index.getId().equals(dayPos)).findAny().orElse(null)
                    );
                } else if(schedule.getCron_Week().matches(PATTERN_LAST_OF_WEEK)){
                    String dayPos = schedule.getCron_Week().substring(0, 1);
                    ControlBindingObj ordinalPos = new ControlBindingObj("Last", "5");
                    yearlyOrdinalCombo.getSelectionModel().select(ordinalPos);
                    yearlyDayCombo.getSelectionModel().select(
                            yearlyDayCombo.getItems().stream().filter(index -> index.getId().equals(dayPos)).findAny().orElse(null)
                    );
                }
                String monthPos = schedule.getCron_Mm();
                yearlyMonthCombo.getSelectionModel().select(
                        yearlyMonthCombo.getItems().stream().filter(index -> index.getId().equals(monthPos)).findAny().orElse(null)
                );
                break;

        }
    }

    private void loadDatabaseList() throws IOException {
        String urlForLoadDatabaseList = SERVER_URL + "/dbs/list/dbname?link_id=" + linkId;
        String responseForDatabaseList = getResponseFromAPI(urlForLoadDatabaseList);
        DatabaseListModel databaseListObj = gson.fromJson(responseForDatabaseList, DatabaseListModel.class);
        if (databaseListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<String> databaseList = FXCollections.observableArrayList();
            databaseListObj.getResultListTable().getDbNames().forEach(index -> databaseList.add(index.getDbname()));
            databaseListComboBox.setItems(databaseList);
            String urlForSelectedDatabase = SERVER_URL + "/dbs/list/cm?link_id=" + linkId + "&cm_id=" + getCampaignId();
            String responseForSelectedDatabase = getResponseFromAPI(urlForSelectedDatabase);
            SelectedDatabaseModel selectedDatabaseObj = gson.fromJson(responseForSelectedDatabase, SelectedDatabaseModel.class);
            String selectedDbName = selectedDatabaseObj.getDatasources().getDatasource().get(0).getDatabase_name_txt();
            String selectedTableName = selectedDatabaseObj.getDatasources().getDatasource().get(0).getTable_name_txt();
            databaseListComboBox.getSelectionModel().select(selectedDbName);
            loadViewListByDatabaseName(selectedDbName, selectedTableName);
        } else if (databaseListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, databaseListObj.getResultinfo().getErrString());
        }
    }

    private void loadViewListByDatabaseName(String databaseName, String tableName) throws IOException {
        String urlForViewList = SERVER_URL + "/dbs/list/view?link_id=" + linkId + "&dbname=" + databaseName;
        String responseForViewList = getResponseFromAPI(urlForViewList);
        ViewListModel viewListObj = gson.fromJson(responseForViewList, ViewListModel.class);
        if (viewListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<String> viewList = FXCollections.observableArrayList();
            viewListObj.getResultList().getSetViews().forEach(index -> viewList.add(index.getTableName()));
            viewListComboBox.setItems(viewList);
            if (tableName == null) {
                viewListComboBox.getSelectionModel().select(0);
            } else {
                viewListComboBox.getSelectionModel().select(tableName);
            }
        } else if(viewListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT){
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, viewListObj.getResultinfo().getErrString());
        }
    }
}
