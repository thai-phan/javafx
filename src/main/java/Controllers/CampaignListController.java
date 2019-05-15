package main.java.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.CampaignListModel;
import main.java.Models.MasterFolderModel;
import main.java.Models.ModelObject.CmDatas;
import main.java.Models.ModelObject.ControlBindingObj;
import main.java.Models.ModelObject.Resultinfo;
import main.java.Models.StatusListModel;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CampaignListController extends Main
{
    private static final String STATUS_ACTIVE = "1";
    private static final String STATUS_INACTIVE = "2";
    private static final String STATUS_DRAFT = "0";
    private static final String STATUS_FINISH = "3";
    private static final String STATUS_NULL = "";
    private static final String STATUS_DEFAULT = "9999";
    private static final String PATTERN_FOLDER_NAME = "^\\s*(\\S.*)";
    @FXML
    private TreeView<ControlBindingObj> folderTree;
    @FXML
    private ComboBox<ControlBindingObj> masterFolderComboBox;
    @FXML
    private TableView<CmDatas> campaignTableView;
    @FXML
    private TableColumn<CmDatas, String> campClId;
    @FXML
    private TableColumn<CmDatas, String> campClName;
    @FXML
    private TableColumn<CmDatas, String> campClDesc;
    @FXML
    private TableColumn<CmDatas, String> campClStatus;
    @FXML
    private TableColumn<CmDatas, String> campClFolder;
    @FXML
    private TableColumn<CmDatas, String> campClCreBy;
    @FXML
    private TableColumn<CmDatas, String> campClCreOn;
    @FXML
    private TableColumn<CmDatas, String> campClModBy;
    @FXML
    private TableColumn<CmDatas, String> campClModOn;
    @FXML
    private Text recordsNumber;
    @FXML
    private TextField campaignSearch;
    @FXML
    private ComboBox<ControlBindingObj> statusListComboBox;
    @FXML
    private Button activeCampButton;
    @FXML
    private Button deactivateCampButton;
    @FXML
    private Button submitCampButton;
    @FXML
    private Button rejectCampButton;
    @FXML
    private Button editCampButton;
    @FXML
    private Button copyCampButton;
    @FXML
    private Button viewCampButton;
    @FXML
    private Button launchScheduleButton;

    private static String searchedText;
    private static ControlBindingObj selectedStatus;
    static ControlBindingObj selectedMasterFolder;
    static String selectedSubFolder;
    private static CmDatas selectedCampaign;

    private static boolean isSearch;
    private Pattern folderNamePattern;
    private SimpleStringProperty status = new SimpleStringProperty(STATUS_DEFAULT);
    @Override
    public void initialize() {
        folderNamePattern = Pattern.compile(PATTERN_FOLDER_NAME);
        configurationView();
        loadDataFromAPI();
        addListener();
    }

    @FXML
    private void editCampaign() throws IOException {
        searchedText = campaignSearch.getText();
        selectedStatus = statusListComboBox.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(COMMUNICATION_MANAGER_FXML));
        Region root = loader.load();
        CommunicationManagerController communicationManagerController = loader.getController();
        CmDatas campaignSelected = campaignTableView.getSelectionModel().getSelectedItem();
        communicationManagerController.setCampaignId(campaignSelected.getEntity_Id());
        communicationManagerController.isView = false;
        communicationManagerController.initialize();
        scene.setRoot(root);
    }

    @FXML
    public void onViewCampaign() throws IOException {
        searchedText = campaignSearch.getText();
        selectedStatus = statusListComboBox.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(COMMUNICATION_MANAGER_FXML));
        Region root = loader.load();
        CommunicationManagerController communicationManagerController = loader.getController();
        CmDatas campaignSelected = campaignTableView.getSelectionModel().getSelectedItem();
        communicationManagerController.setCampaignId(campaignSelected.getEntity_Id());
        communicationManagerController.isView = true;
        communicationManagerController.initialize();
        scene.setRoot(root);
    }

    @FXML
    private void onLogout() throws IOException {
        Alert alert = createAlert("Confirm to logout");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            logout();
        }
    }

    @FXML
    private void onChangePassword() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PASSWORD));
        Region root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.getIcons().add(new Image("/main/resources/images/change-password.png"));
        newStage.setTitle("Change password");
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    @FXML
    private void showAddFolderDialog() {
        TextInputDialog dialog = new TextInputDialog();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/main/resources/css/main.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogFolder");
        dialog.setTitle("Add folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            Matcher matcher = folderNamePattern.matcher(result.get());
            if (matcher.find()) {
                addFolder(matcher.group(1));
            } else {
                createNotificationDialog("Folder name invalid", null, null);
            }
        }
    }

    @FXML
    public void showRenameFolderDialog() {
        ControlBindingObj folderChanging = folderTree.getSelectionModel().getSelectedItems().get(0).getValue();
        TextInputDialog dialog = new TextInputDialog(folderChanging.getName());
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/main/resources/css/main.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogFolder");
        dialog.setTitle("Rename folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            Matcher matcher = folderNamePattern.matcher(result.get());
            String folderId = folderTree.getSelectionModel().getSelectedItems().get(0).getValue().getId();
            if (matcher.find()) {
                renameFolder(matcher.group(1), folderId);
            } else {
                createNotificationDialog("Folder name invalid", null, null);
            }
        }
    }

    @FXML
    public void onCopyCampaign() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FOLDER_SELECTION));
        Region root = loader.load();
        Stage newStage = new Stage();
        FolderSelectionController folderSelectionController = loader.getController();
        folderSelectionController.loadTreeFolderList(masterFolderComboBox);
        CmDatas selectedCampaign = campaignTableView.getSelectionModel().getSelectedItem();
        folderSelectionController.setOldCampId(selectedCampaign.getEntity_Id());
        folderSelectionController.setCampaignListController(this);
        folderSelectionController.getSelectedCampNameAndDescription(selectedCampaign.getName(), selectedCampaign.getDescription());
        newStage.getIcons().add(new Image("/main/resources/images/copy.png"));
        newStage.setTitle("Copy campaign");
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    @FXML
    public void onSearch() {
        isSearch = true;
        selectedCampaign = null;

        loadCampaignTable();
    }

    @FXML
    public void onActiveCamp() {
        Alert alert = createAlert("Confirm to Active Campaign");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            changeCampaignStatus(STATUS_ACTIVE);
        }
    }

    @FXML
    public void onDeactiveCamp() {
        Alert alert = createAlert("Confirm to Deactivate Campaign");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            changeCampaignStatus(STATUS_INACTIVE);
        }
    }

    @FXML
    public void onSubmitCamp() {
        Alert alert = createAlert("Confirm to Submit Campaign");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            changeCampaignStatus(STATUS_INACTIVE);
        }
    }

    @FXML
    public void onRejectCamp() {
        Alert alert = createAlert("Confirm to Reject Campaign");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            changeCampaignStatus(STATUS_DRAFT);
        }
    }

    @FXML
    public void onLaunchSchedule() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SCHEDULE_DATE));
        Region root = loader.load();
        Stage newStage = new Stage();
        ScheduleDateController scheduleDateController = loader.getController();
        scheduleDateController.setCampaignId(selectedCampaign.getEntity_Id());
        scheduleDateController.setCampaignListController(this);
        newStage.setScene(new Scene(root));
        newStage.setTitle("Schedule");
        newStage.getIcons().add(new Image("/main/resources/images/schedule.png"));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

    }

    private void loadDataFromAPI() {
        loadStatusList();
        loadMasterFolderList();
    }

    private void configurationView() {
        if (searchedText != null) {
            campaignSearch.setText(searchedText);
        }
        campClId.setCellValueFactory(new PropertyValueFactory<>("entity_Id"));
        campClName.setCellValueFactory(new PropertyValueFactory<>("name"));
        campClDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        campClStatus.setCellValueFactory(new PropertyValueFactory<>("status_Cd_name"));
        campClFolder.setCellValueFactory(new PropertyValueFactory<>("path"));
        campClCreBy.setCellValueFactory(new PropertyValueFactory<>("create_User"));
        campClCreOn.setCellValueFactory(new PropertyValueFactory<>("create_Dttm"));
        campClModBy.setCellValueFactory(new PropertyValueFactory<>("update_User"));
        campClModOn.setCellValueFactory(new PropertyValueFactory<>("update_Dttm"));

        bindStatusForCommandButton();
    }

    private void bindStatusForCommandButton() {
        deactivateCampButton.disableProperty().bind(status.isNotEqualTo(STATUS_ACTIVE));
        activeCampButton.disableProperty().bind(status.isNotEqualTo(STATUS_INACTIVE));
        submitCampButton.disableProperty().bind(status.isNotEqualTo(STATUS_DRAFT));
        rejectCampButton.disableProperty().bind(status.isNotEqualTo(STATUS_INACTIVE).and(status.isNotEqualTo(STATUS_FINISH)));
        editCampButton.disableProperty().bind(status.isNotEqualTo(STATUS_DRAFT).and(status.isNotEqualTo(STATUS_NULL)));
        copyCampButton.disableProperty().bind(status.isEqualTo(STATUS_NULL).or(status.isEqualTo(STATUS_DEFAULT)));
        viewCampButton.disableProperty().bind(status.isEqualTo(STATUS_DEFAULT));
        launchScheduleButton.disableProperty().bind(status.isNotEqualTo(STATUS_ACTIVE));
    }

    private void addListener() {
        masterFolderComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) {
                selectedMasterFolder = newVal;
                loadSubFolderListOnTree(false);
            }
        });
        folderTree.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if(event.getClickCount() == 2) {
                    isSearch = false;
                    selectedSubFolder = folderTree.getSelectionModel().getSelectedItem().getValue().getName();
                    selectedCampaign = null;
                    loadCampaignTable();
                }
            }
        });
        campaignTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedCampaign = newValue;
                        status.setValue(newValue.getStatus_Cd());
                    } else {
                        status.setValue(STATUS_DEFAULT);
                    }
                }
        );
    }

    void loadMasterFolderList() {
        String urlForMasterFolder = SERVER_URL + "/cm/list/folder_up?link_id=" + linkId;

        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForMasterFolder);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            MasterFolderModel masterFolderObj = gson.fromJson((String) response.getSource().getValue(), MasterFolderModel.class);
            if (masterFolderObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && masterFolderObj.getCmFolderList().getCmFolderData().size() > 0) {
                ObservableList<ControlBindingObj> folderList = FXCollections.observableArrayList();
                masterFolderObj.getCmFolderList().getCmFolderData().
                        forEach(index -> folderList.add(new ControlBindingObj(index.getName(), index.getFolder_Id())));
                masterFolderComboBox.setItems(folderList);
                if (selectedMasterFolder != null) {
                    masterFolderComboBox.getSelectionModel().select(selectedMasterFolder);
                } else {
                    masterFolderComboBox.getSelectionModel().selectFirst();
                    selectedMasterFolder = masterFolderComboBox.getSelectionModel().getSelectedItem();
                }
                loadSubFolderListOnTree(false);
            } else if (masterFolderObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForMasterFolder);
            } else if (masterFolderObj.getResultinfo().getErrCd() != API_CODE_SUCCESS){
                createNotificationDialog(ERROR_HEADER, masterFolderObj.getResultinfo().getErrString(), urlForMasterFolder);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }


    private void loadSubFolderListOnTree(Boolean newFolder) {
        Task<TreeItem<ControlBindingObj>> newTask = new Task<TreeItem<ControlBindingObj>>() {
            @Override
            public TreeItem<ControlBindingObj> call() {
                return loadFolderTreeItem(selectedMasterFolder);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            TreeItem<ControlBindingObj> item = (TreeItem<ControlBindingObj>) response.getSource().getValue();
            folderTree.setRoot(item);
            folderTree.setShowRoot(false);
            if (selectedSubFolder != null) {
                TreeItem<ControlBindingObj> selectedFolderItem = item.getChildren().stream()
                        .filter(index -> index.getValue().getName().equals(selectedSubFolder))
                        .findAny()
                        .orElse(null);
                if (selectedFolderItem != null) {
                    folderTree.getSelectionModel().select(selectedFolderItem);
                } else {
                    folderTree.getSelectionModel().selectFirst();
                }
            } else {
                folderTree.getSelectionModel().selectFirst();
            }
            if (!newFolder) {
                loadCampaignTable();
            }
            loadingStage.hide();

        });
        new Thread(newTask).start();


    }

    void loadCampaignTable() {
        // clear table data before load
        campaignTableView.getItems().clear();
        recordsNumber.setText("0");

        // load data
        String subFolderId = !isSearch ? folderTree.getSelectionModel().getSelectedItems().get(0).getValue().getId() : "";
        String statusId = statusListComboBox.getSelectionModel().getSelectedItem().getId() != null ?
                statusListComboBox.getSelectionModel().getSelectedItem().getId() : "";
        String nameSearch = campaignSearch.getText() != null ? campaignSearch.getText() : "";
        String urlForCampaignList = null;
        try {
            urlForCampaignList = SERVER_URL + "/cm/list?link_id=" + linkId + "&folderid=" + subFolderId +
                    "&statuscd=" + statusId + "&cmname=" + URLEncoder.encode(nameSearch, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String finalUrlForCampaignList = urlForCampaignList;
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(finalUrlForCampaignList);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(event -> {
            String responseForCampList = (String) event.getSource().getValue();
            CampaignListModel campListObj = gson.fromJson(responseForCampList, CampaignListModel.class);
            if (campListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && campListObj.getCmList().getCmDatas().size() > 0) {
                ObservableList<CmDatas> campListObs = FXCollections.observableArrayList();
                campListObj.getCmList().getCmDatas().forEach(index -> campListObs.
                        add(new CmDatas(index.getName(),
                                index.getDescription(), index.getStatus_Cd_name(), index.getPath(),
                                index.getCreate_User(), index.getCreate_Dttm(), index.getUpdate_User(),
                                index.getUpdate_Dttm(), index.getEntity_Id(), index.getStatus_Cd())));
                campaignTableView.setItems(campListObs);
                if (selectedCampaign != null) {
                    CmDatas campaign = campaignTableView.getItems().stream()
                            .filter(index -> index.getEntity_Id().equals(selectedCampaign.getEntity_Id()))
                            .findAny()
                            .orElse(null);
                    if (campaign != null) {
                        campaignTableView.getSelectionModel().select(campaign);
                    } else {
                        campaignTableView.getSelectionModel().selectFirst();
                    }
                } else {
                    campaignTableView.getSelectionModel().selectFirst();
                    selectedCampaign = campaignTableView.getSelectionModel().getSelectedItem();
                }
                recordsNumber.setText(campListObj.getCmList().getCnt());
            } else if (campListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && campListObj.getCmList().getCmDatas().size() == 0) {
                createNotificationDialog("Campaign list empty", null, null);
            } else if (campListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(finalUrlForCampaignList);
            } else if (campListObj.getResultinfo().getErrCd() != API_CODE_SUCCESS){
                createNotificationDialog(ERROR_HEADER, campListObj.getResultinfo().getErrString(), finalUrlForCampaignList);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }

    private void addFolder(String folderName) {
        String urlForAddFolder = null;
        try {
            urlForAddFolder = SERVER_URL + "/cm/ins/folder?link_id=" + linkId +
                    "&parentfolderId=" + selectedMasterFolder.getId() + "&name=" + URLEncoder.encode(folderName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForAddFolder = getResponseFromAPI(urlForAddFolder);
        selectedSubFolder = folderName;
        selectedCampaign = null;
        Resultinfo resultinfo = gson.fromJson(responseForAddFolder, Resultinfo.class);
        afterFolderAction(resultinfo, urlForAddFolder);
    }

    private void renameFolder(String newName, String folderId) {
        String urlForRenameFolder = null;
        try {
            urlForRenameFolder = SERVER_URL + "/cm/update/folder_name?link_id=" + linkId + "&folderid=" + folderId + "&name=" + URLEncoder.encode(newName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        selectedSubFolder = newName;
        selectedCampaign = null;
        String responseForRenameFolder = getResponseFromAPI(urlForRenameFolder);
        Resultinfo resultinfo = gson.fromJson(responseForRenameFolder, Resultinfo.class);
        afterFolderAction(resultinfo, urlForRenameFolder);
    }

    private void afterFolderAction(Resultinfo resultinfo, String url) {
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            createNotificationDialog(SUCCESS_HEADER, null, url);
            loadSubFolderListOnTree(true);
        } else if(resultinfo.getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(url);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), url);
        }
    }

    private void loadStatusList() {
        String urlForStatusList = SERVER_URL + "/dbs/list/cd?link_id=" + linkId + "&cdtype=1";
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForStatusList);
            }
        };
        loadingStage.show();
        newTask.setOnSucceeded(response -> {
            StatusListModel statusListObj = gson.fromJson((String) response.getSource().getValue(),  StatusListModel.class);
            if (statusListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && statusListObj.getCdList().getCds().size() > 0) {
                ObservableList<ControlBindingObj> statusList = FXCollections.observableArrayList();
                statusListObj.getCdList().getCds().forEach(index -> statusList.add(new ControlBindingObj(index.getName(), index.getCd())));
                statusListComboBox.setItems(statusList);
                if (selectedStatus != null) {
                    statusListComboBox.getSelectionModel().select(selectedStatus);
                } else {
                    statusListComboBox.getSelectionModel().selectFirst();
                }
            } else if(statusListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForStatusList);
            } else if (statusListObj.getResultinfo().getErrCd() != API_CODE_SUCCESS){
                createNotificationDialog(ERROR_HEADER, statusListObj.getResultinfo().getErrString(), urlForStatusList);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }

    private void changeCampaignStatus(String status) {
        String campaignId = selectedCampaign.getEntity_Id();
        String urlForChangeStatus = SERVER_URL + "/cm/change/status?link_id=" + linkId + "&cm_id=" + campaignId + "&statuscd=" + status;
        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForChangeStatus);
            }
        };

        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            Resultinfo resultinfo = gson.fromJson((String) response.getSource().getValue(), Resultinfo.class);
            if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
                createNotificationDialog(SUCCESS_HEADER, null, null);
                loadCampaignTable();
            } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForChangeStatus);
            } else {
                createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), urlForChangeStatus);
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }
}
