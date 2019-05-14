package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.*;
import main.java.Models.ModelObject.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class CommunicationManagerController extends Main {
    private static final String NO_END_DATE = "99991231";
    private static final String PATTERN_DAY_LIST = "\\d{1,2}(,\\d{1,2})*";
    private static final String PATTERN_ORDINAL_WEEK = "\\d#\\d";
    private static final String PATTERN_LAST_OF_WEEK = "\\dL";
    @FXML
    private ComboBox<ControlBindingObj> frequencyComboBox;
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
    private ComboBox<ControlBindingObj> channelListComboBox;
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
    private Button saveCampNameBtn;
    @FXML
    private Button saveCampScheduleBtn;
    @FXML
    private Button saveCampFreqBtn;
    @FXML
    private Button saveCampDbBtn;
    @FXML
    private Button saveChannelBtn;
    @FXML
    private TextField resendNumberDay;
    @FXML
    private BorderPane namePane;
    @FXML
    private BorderPane datePane;
    @FXML
    private BorderPane schedulePane;
    @FXML
    private BorderPane channelPane;

    private ControlBindingObj firstOrdinal = new ControlBindingObj("First", "1");
    private ControlBindingObj secondOrdinal = new ControlBindingObj("Second", "2");
    private ControlBindingObj thirdOrdinal = new ControlBindingObj("Third", "3");
    private ControlBindingObj fourOrdinal = new ControlBindingObj("Four", "4");
    private ControlBindingObj lastOrdinal = new ControlBindingObj("Last", "5");
    private ControlBindingObj lastDayOrdinal = new ControlBindingObj("Last day", "6");
    private ControlBindingObj sunday = new ControlBindingObj("Sunday", "1");
    private ControlBindingObj monday = new ControlBindingObj("Monday", "2");
    private ControlBindingObj tuesday = new ControlBindingObj("Tuesday", "3");
    private ControlBindingObj wednesday = new ControlBindingObj("Wednesday", "4");
    private ControlBindingObj thursday = new ControlBindingObj("Thursday", "5");
    private ControlBindingObj friday = new ControlBindingObj("Friday", "6");
    private ControlBindingObj saturday = new ControlBindingObj("Saturday", "7");
    private ControlBindingObj january = new ControlBindingObj("January", "1");
    private ControlBindingObj february = new ControlBindingObj("February", "2");
    private ControlBindingObj march = new ControlBindingObj("March", "3");
    private ControlBindingObj april = new ControlBindingObj("April", "4");
    private ControlBindingObj may = new ControlBindingObj("May", "5");
    private ControlBindingObj june = new ControlBindingObj("June", "6");
    private ControlBindingObj july = new ControlBindingObj("July", "7");
    private ControlBindingObj august = new ControlBindingObj("August", "1");
    private ControlBindingObj september = new ControlBindingObj("September", "2");
    private ControlBindingObj october = new ControlBindingObj("October", "3");
    private ControlBindingObj november = new ControlBindingObj("November", "4");
    private ControlBindingObj december = new ControlBindingObj("December", "5");

    boolean isView;
    private String campaignId;
    private Map<String, CheckBox> weekDayMap = new HashMap<>();
    private LocalDate localDate;

    void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    private String getCampaignId() {
        return campaignId;
    }

    public void initialize() {
        if (getCampaignId() != null) {
            localDate = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            configurationView();
            loadDataFromAPI();
            configurationViewAfterLoadData();
            addListener();
        }
    }

    @FXML
    protected void saveCampaignName() throws Exception {
        Alert alert = createAlert("Confirm to Save");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String updatedName = campaignName.getText();
            String updatedDescription = campaignDescription.getText();
            String urlForUpdateCampInfo = SERVER_URL + "/cm/update/name?link_id=" + linkId + "&cm_id=" + getCampaignId()
                    + "&name=" + URLEncoder.encode(updatedName, "UTF-8") + "&desc=" + URLEncoder.encode(updatedDescription, "UTF-8");
            String responseForUpdateCampName = getResponseFromAPI(urlForUpdateCampInfo);
            Resultinfo resultinfo = gson.fromJson(responseForUpdateCampName, Resultinfo.class);
            notificationForAction(resultinfo, urlForUpdateCampInfo);
        }
    }

    @FXML
    private void onSaveCampSchedule() throws IOException {
        Alert alert = createAlert("Confirm to Save");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String updatedActlComStart = actlComStart.getValue().toString();
            String updatedActlComEnd = actlComEnd.getValue().toString();
            String updatedPlanComStart = planComStart.getValue().toString();
            String updatedPlanComEnd = planComEnd.getValue().toString();
            String resendDays = resendNumberDay.getText();
            String urlForUpdateCampSchedule = SERVER_URL + "/cm/update/date?link_id=" + linkId + "&cm_id=" + getCampaignId()
                    + "&act_comm_sdt=" + updatedActlComStart + "&act_comm_edt=" + updatedActlComEnd
                    + "&plan_comm_sdt=" + updatedPlanComStart + "&plan_comm_edt=" + updatedPlanComEnd + "&dedup_days=" + resendDays;
            String responseForUpdateCampSchedule = getResponseFromAPI(urlForUpdateCampSchedule);
            Resultinfo resultinfo = gson.fromJson(responseForUpdateCampSchedule, Resultinfo.class);
            notificationForAction(resultinfo, urlForUpdateCampSchedule);
        }
    }

    @FXML
    private void onSaveCampFrequencyOpt() throws IOException {
        Alert alert = createAlert("Confirm to Save");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
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
                    weekDayMap.forEach((key, value) -> {
                        if (value.isSelected()) {
                            dayList.add(key);
                        }
                    });
                    if (dayList.size() > 0) {
                        cronWk = String.join(",", dayList);
                        isUrlComplete = true;
                    } else {
                        createNotificationDialog("Select one day in week.", null, null);
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

                    } else if (monthlySpecificRadio.isSelected()) {
                        optionNumber = "2";
                        if (monthlyDateList.getText().matches(PATTERN_DAY_LIST)) {
                            cronDd = monthlyDateList.getText();
                            isUrlComplete = true;
                        } else if (monthlyDateList.getText().isEmpty()) {
                            createNotificationDialog("Day list require.", null, null);
                        } else {
                            createNotificationDialog("Day list wrong format.", null, null);
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
                    isUrlComplete = true;
                    break;
            }

            if (isUrlComplete) {
                String urlForUpdateSchedule = SERVER_URL + "/sch/update/cm?link_id=" + linkId + "&cm_id=" + getCampaignId()
                        + "&type_cd=" + frequencyType + "&start_dttm=" + schStartDate + "&end_dttm=" + schEndDate
                        + "&opt_num=" + optionNumber + "&cron_Dd=" + cronDd + "&cron_Mm=" + cronMm + "&cron_Week=" + URLEncoder.encode(cronWk, "UTF-8");
                String response = getResponseFromAPI(urlForUpdateSchedule);
                Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
                notificationForAction(resultinfo, urlForUpdateSchedule);
            }
        }
    }

    @FXML
    private void onSaveChannel() throws IOException {
        Alert alert = createAlert("Confirm to Save");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String channelId = channelListComboBox.getSelectionModel().getSelectedItem().getId();
            String url = SERVER_URL + "/cm/change/channel?link_id=" + linkId + "&cm_id=" + getCampaignId()
                    + "&channel_id=" + channelId;
            String response = getResponseFromAPI(url);
            Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
            notificationForAction(resultinfo, null);
        }
    }

    @FXML
    private void onSaveCampDatabase() throws IOException {
        Alert alert = createAlert("Confirm to Save");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            String currentDatabase = databaseListComboBox.getSelectionModel().getSelectedItem();
            String currentView = viewListComboBox.getSelectionModel().getSelectedItem();
            String url = SERVER_URL + "/dbs/update/cm?link_id=" + linkId + "&cm_id=" + getCampaignId()
                    + "&database_nm=" + currentDatabase + "&table_nm=" + currentView;
            String response = getResponseFromAPI(url);
            Resultinfo resultinfo = gson.fromJson(response, Resultinfo.class);
            notificationForAction(resultinfo, null);
        }
    }

    @FXML
    protected void onExplainDatabase() throws IOException {
        String currentDatabase = databaseListComboBox.getSelectionModel().getSelectedItem();
        String currentView = viewListComboBox.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EXPLAIN_LIST));
        Region root = loader.load();
        Stage newStage = new Stage();
        ExplainListController explainListController = loader.getController();
        explainListController.initTableExplain(currentDatabase, currentView);
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    @FXML
    protected void cancelCampaign() throws Exception {
        setSceneByView(CAMPAIGN_LIST_FXML);
    }

    private void configurationView() {
        if (isView) {
            saveCampNameBtn.setManaged(false);
            saveCampScheduleBtn.setManaged(false);
            saveCampFreqBtn.setManaged(false);
            saveCampDbBtn.setManaged(false);
            saveChannelBtn.setManaged(false);
        } else {
            monthlyOrdinalCombo.disableProperty().bind(monthlyDayRadio.selectedProperty().not());
            monthlyDayCombo.disableProperty().bind(monthlyDayRadio.selectedProperty().not().or(monthlyOrdinalCombo.valueProperty().isEqualTo(lastDayOrdinal)));
            monthlyDateList.disableProperty().bind(monthlySpecificRadio.selectedProperty().not());
            freqEnd.disableProperty().bind(endDateRadio.selectedProperty().not());
        }


        resendNumberDay.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && (!newValue.matches("\\d*") || (Integer.valueOf(newValue) > 1000))) {
                resendNumberDay.setText(oldValue);
            }
        });
        resendNumberDay.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && resendNumberDay.getText().isEmpty()) {
                resendNumberDay.setText("0");
            }
        });
        weekDayMap.put("1", weeklySundayCheck);
        weekDayMap.put("2", weeklyMondayCheck);
        weekDayMap.put("3", weeklyTuesdayCheck);
        weekDayMap.put("4", weeklyWednesdayCheck);
        weekDayMap.put("5", weeklyThursdayCheck);
        weekDayMap.put("6", weeklyFridayCheck);
        weekDayMap.put("7", weeklySaturdayCheck);

        monthlyOrdinalCombo.getItems().addAll(firstOrdinal, secondOrdinal, thirdOrdinal, fourOrdinal, lastOrdinal, lastDayOrdinal);
        monthlyOrdinalCombo.getSelectionModel().selectFirst();

        monthlyDayCombo.getItems().addAll(sunday, monday, tuesday, wednesday, thursday, friday, saturday);

        monthlyDayCombo.getSelectionModel().selectFirst();

        yearlyOrdinalCombo.getItems().addAll(firstOrdinal, secondOrdinal, thirdOrdinal, fourOrdinal, lastOrdinal);
        yearlyOrdinalCombo.getSelectionModel().selectFirst();

        yearlyDayCombo.getItems().addAll(sunday, monday, tuesday, wednesday, thursday, friday, saturday);
        yearlyDayCombo.getSelectionModel().selectFirst();

        yearlyMonthCombo.getItems().addAll(january, february, march, april, may, june, july, august, september, october, november, december);
        yearlyMonthCombo.getSelectionModel().selectFirst();

        changeDateTimeToKrTypeAndDisableEditor(actlComStart);
        changeDateTimeToKrTypeAndDisableEditor(actlComEnd);
        changeDateTimeToKrTypeAndDisableEditor(planComStart);
        changeDateTimeToKrTypeAndDisableEditor(planComEnd);
        changeDateTimeToKrTypeAndDisableEditor(freqStart);
        changeDateTimeToKrTypeAndDisableEditor(freqEnd);
        setTextFieldLength(campaignName, 40);
        setTextFieldLength(campaignDescription, 100);
    }

    private void configurationViewAfterLoadData() {
        if (isView) {
            namePane.setDisable(true);
            datePane.setDisable(true);
            schedulePane.setDisable(true);
            channelPane.setDisable(true);
            databaseListComboBox.setDisable(true);
            viewListComboBox.setDisable(true);
        }
    }

    private void addListener() {
        databaseListComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!StringUtils.isEmpty(oldVal)) {
                loadViewListByDatabaseName(newVal, null);
            }
        });
    }

    private void loadDataFromAPI() {
        loadCampaignInfo();
        loadDatabaseList();
        loadFrequencyList();
        loadFrequencyDetail();
    }

    private void loadCampaignInfo() {
        String urlForCampaignInfo = SERVER_URL + "/cm/info?link_id=" + linkId+ "&cm_id=" + getCampaignId();

        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForCampaignInfo);
            }
        };
        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            String responseForCampaignInfo = (String) response.getSource().getValue();
            CampaignInfoModel campaignInfoObj = gson.fromJson(responseForCampaignInfo, CampaignInfoModel.class);
            if (campaignInfoObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && campaignInfoObj.getResultList().getCms().size() > 0) {
                Cms campaign =  campaignInfoObj.getResultList().getCms().get(0);
                campaignName.setText(campaign.getName());
                campaignDescription.setText(campaign.getDescription());
                actlComStart.setValue(!StringUtils.isEmpty(campaign.getActual_Communication_Start_Dt()) ? LocalDate.parse(campaign.getActual_Communication_Start_Dt()) : localDate);
                actlComEnd.setValue(!StringUtils.isEmpty(campaign.getActual_Communication_End_Dt()) ? LocalDate.parse(campaign.getActual_Communication_End_Dt()) : localDate);
                planComStart.setValue(!StringUtils.isEmpty(campaign.getPlanned_Communication_Start_Dt()) ? LocalDate.parse(campaign.getPlanned_Communication_Start_Dt()) : localDate);
                planComEnd.setValue(!StringUtils.isEmpty(campaign.getPlanned_Communication_End_Dt()) ? LocalDate.parse(campaign.getPlanned_Communication_End_Dt()) : localDate);
                resendNumberDay.setText(!StringUtils.isEmpty(campaign.getDeduplication_Days_Num()) ? campaign.getDeduplication_Days_Num() : "0");
                loadChannelList(campaign.getChannel_INSTANCE_ID());
            } else if(campaignInfoObj.getResultinfo().getErrCd() == API_CODE_LOGOUT){
                logoutByExpireSession(urlForCampaignInfo);
            } else if (campaignInfoObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && campaignInfoObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
                createNotificationDialog(ERROR_HEADER, campaignInfoObj.getResultinfo().getErrString(), urlForCampaignInfo);
            }
            loadingStage.hide();
        });

        new Thread(newTask).start();
    }

    private void loadFrequencyList() {
        String urlForFrequencyList = SERVER_URL + "/dbs/list/cd?link_id=" + linkId + "&cdtype=2";

        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForFrequencyList);
            }
        };
        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            String responseForFrequencyList = (String) response.getSource().getValue();
            StatusListModel statusListObj = gson.fromJson(responseForFrequencyList,  StatusListModel.class);
            if (statusListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && statusListObj.getCdList().getCds().size() > 0) {
                ObservableList<ControlBindingObj> statusList = FXCollections.observableArrayList();
                statusListObj.getCdList().getCds().forEach(index -> statusList.add(new ControlBindingObj(index.getName(), index.getCd())));
                frequencyComboBox.setItems(statusList);
                weeklyOptionList.visibleProperty().bind(frequencyComboBox.valueProperty().isEqualTo(frequencyComboBox.getItems().stream().filter(index -> index.getName().equals("Weekly")).findAny().orElse(null)));
                monthlyOptionList.visibleProperty().bind(frequencyComboBox.valueProperty().isEqualTo(frequencyComboBox.getItems().stream().filter(index -> index.getName().equals("Monthly")).findAny().orElse(null)));
                yearlyOptionList.visibleProperty().bind(frequencyComboBox.valueProperty().isEqualTo(frequencyComboBox.getItems().stream().filter(index -> index.getName().equals("Yearly")).findAny().orElse(null)));

            } else if(statusListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT){
                logoutByExpireSession(urlForFrequencyList);
            } else if (statusListObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && statusListObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
                createNotificationDialog(ERROR_HEADER, statusListObj.getResultinfo().getErrString(), urlForFrequencyList);
            }
            loadingStage.hide();
        });

        new Thread(newTask).start();
    }

    private void loadChannelList(String channelId) {
        String urlForChannelList = SERVER_URL + "/dbs/list/cd?link_id=" + linkId + "&cdtype=3";
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForChannelList);
            }
        };
        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            String responseForChannelList = (String) response.getSource().getValue();
            StatusListModel statusListObj = gson.fromJson(responseForChannelList, StatusListModel.class);
            if (statusListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
                ObservableList<ControlBindingObj> channelList = FXCollections.observableArrayList();
                statusListObj.getCdList().getCds().forEach(index -> channelList.add(new ControlBindingObj(index.getName(), index.getCd())));
                channelListComboBox.setItems(channelList);
                if (!StringUtils.isEmpty(channelId)) {
                    ControlBindingObj selectedChannel = channelListComboBox.getItems().stream().filter(index -> index.getId().equals(channelId)).findAny().orElse(null);
                    if (selectedChannel != null) {
                        channelListComboBox.getSelectionModel().select(selectedChannel);
                    } else {
                        channelListComboBox.getSelectionModel().selectFirst();
                    }
                } else {
                    channelListComboBox.getSelectionModel().selectFirst();
                }
            } else if(statusListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForChannelList);

            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }
    private void loadFrequencyDetail() {
        String urlForFrequencyDetail = SERVER_URL + "/sch/info/cm?link_id=" + linkId + "&cm_id=" + getCampaignId();

        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForFrequencyDetail);
            }
        };
        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            String responseForFrequencyDetail = (String) response.getSource().getValue();
            CampaignScheduleModel campaignScheduleObj = gson.fromJson(responseForFrequencyDetail, CampaignScheduleModel.class);
            if (campaignScheduleObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && campaignScheduleObj.getSchInfoList().getSchInfoList().size() > 0) {
                String selectedFrequencyType = campaignScheduleObj.getSchInfoList().getSchInfoList().get(0).getSchedule_type_cd();
                ControlBindingObj selectedFrequency = frequencyComboBox.getItems()
                        .stream().filter(index -> index.getId().equals(selectedFrequencyType))
                        .findAny()
                        .orElse(null);
                frequencyComboBox.getSelectionModel().select(selectedFrequency);
                // Bind data to field list
                SchInfos scheduleDetail = campaignScheduleObj.getSchInfoList().getSchInfoList().get(0);
                if (!scheduleDetail.getSchedule_start_dttm_ftm().isEmpty()) {
                    Date freqStartDate = null;
                    try {
                        freqStartDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(scheduleDetail.getSchedule_start_dttm_ftm());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    freqStart.setValue(freqStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                if (!scheduleDetail.getSchedule_end_dttm_ftm().equals(NO_END_DATE)) {
                    endDateRadio.setSelected(true);
                    Date freqEndDate = null;
                    try {
                        freqEndDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(scheduleDetail.getSchedule_end_dttm_ftm());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    freqEnd.setValue(freqEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } else {
                    noEndDateRadio.setSelected(true);
                    freqEnd.setValue(localDate);
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
            } else if (campaignScheduleObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && campaignScheduleObj.getSchInfoList().getSchInfoList().size() == 0) {
                frequencyComboBox.getSelectionModel().selectFirst();
                freqStart.setValue(localDate);
                endDateRadio.setSelected(true);
                freqEnd.setValue(localDate);
            } else if (campaignScheduleObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForFrequencyDetail);
            } else if (campaignScheduleObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && campaignScheduleObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
                createNotificationDialog(ERROR_HEADER, campaignScheduleObj.getResultinfo().getErrString(), urlForFrequencyDetail);
            }
            loadingStage.hide();
        });

        new Thread(newTask).start();

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

    private void loadDatabaseList() {
        String urlForLoadDatabaseList = SERVER_URL + "/dbs/list/dbname?link_id=" + linkId;
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForLoadDatabaseList);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            String responseForDatabaseList = (String) response.getSource().getValue();
            DatabaseListModel databaseListObj = gson.fromJson(responseForDatabaseList, DatabaseListModel.class);
            if (databaseListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && databaseListObj.getResultListTable().getDbNames().size() > 0) {
                ObservableList<String> databaseList = FXCollections.observableArrayList();
                databaseListObj.getResultListTable().getDbNames().forEach(index -> databaseList.add(index.getDbname()));
                databaseListComboBox.setItems(databaseList);
                loadSelectedDatabaseAndView();

            } else if (databaseListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForLoadDatabaseList);
            } else if (databaseListObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && databaseListObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
                createNotificationDialog(ERROR_HEADER, databaseListObj.getResultinfo().getErrString(), urlForLoadDatabaseList);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }

    private void loadSelectedDatabaseAndView() {
        String urlForSelectedDatabase = SERVER_URL + "/dbs/list/cm?link_id=" + linkId + "&cm_id=" + getCampaignId();
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForSelectedDatabase);
            }
        };

        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            String responseForSelectedDatabase = (String) response.getSource().getValue();
                    SelectedDatabaseModel selectedDatabaseObj = gson.fromJson(responseForSelectedDatabase, SelectedDatabaseModel.class);
            if (selectedDatabaseObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && selectedDatabaseObj.getDatasources().getDatasource().size() > 0) {
                String selectedDbName = selectedDatabaseObj.getDatasources().getDatasource().get(0).getDatabase_name_txt();
                String selectedTableName = selectedDatabaseObj.getDatasources().getDatasource().get(0).getTable_name_txt();
                databaseListComboBox.getSelectionModel().select(selectedDbName);
                loadViewListByDatabaseName(selectedDbName, selectedTableName);
            } else {
                databaseListComboBox.getSelectionModel().selectFirst();
                loadViewListByDatabaseName(databaseListComboBox.getSelectionModel().getSelectedItem(), null);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }

    private void loadViewListByDatabaseName(String databaseName, String tableName) {
        String urlForViewList = SERVER_URL + "/dbs/list/view?link_id=" + linkId + "&dbname=" + databaseName;
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForViewList);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            String responseForViewList = (String) response.getSource().getValue();
            ViewListModel viewListObj = gson.fromJson(responseForViewList, ViewListModel.class);
            if (viewListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && viewListObj.getResultList().getSetViews().size() > 0) {
                ObservableList<String> viewList = FXCollections.observableArrayList();
                viewListObj.getResultList().getSetViews().forEach(index -> viewList.add(index.getTableName()));
                viewListComboBox.setItems(viewList);
                if (tableName == null) {
                    viewListComboBox.getSelectionModel().select(0);
                } else {
                    viewListComboBox.getSelectionModel().select(tableName);
                }
            } else if(viewListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT){
                logoutByExpireSession(urlForViewList);
            } else if (viewListObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && viewListObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
                createNotificationDialog(ERROR_HEADER, viewListObj.getResultinfo().getErrString(), urlForViewList);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }
}
