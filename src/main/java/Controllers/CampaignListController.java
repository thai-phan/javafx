package main.java.Controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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


public class CampaignListController extends Main
{
    private static final String STATUS_ACTIVE = "1";
    private static final String STATUS_INACTIVE = "2";
    private static final String STATUS_DRAFT = "0";
    @FXML
    private TreeView<ControlBindingObj> folderTree;
    @FXML
    private ComboBox<ControlBindingObj> masterFolderComboBox;
    @FXML
    private TableView<CmDatas> campaignTableView;
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
    private Button deactiveCampButton;
    @FXML
    private Button submitCampButton;
    @FXML
    private Button rejectCampButton;
    @FXML
    private Button editCampButton;
    @FXML
    private Button copyCampButton;


    private static ControlBindingObj selectedMasterFolder;

    @Override
    public void initialize() {
        configurationView();
        try {
            loadDataFromAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addListener();
    }

    @FXML
    private void editCampaign(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(COMMUNICATION_MANAGER_FXML));
        Region root = loader.load();
        CommunicationManagerController communicationManagerController = loader.getController();
        CmDatas campaignSelected = campaignTableView.getSelectionModel().getSelectedItem();
        communicationManagerController.setCampaignId(campaignSelected.getEntity_Id());
        communicationManagerController.initialize();
        scene.setRoot(root);
    }

    @FXML
    private void onLogout() throws IOException {
        logout();
    }

    @FXML
    private void showAddFolderDialog() throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            addFolder(result.get());
        }
    }

    @FXML
    public void showRenameFolderDialog() throws IOException {
        ControlBindingObj folderChanging = folderTree.getSelectionModel().getSelectedItems().get(0).getValue();
        TextInputDialog dialog = new TextInputDialog(folderChanging.getName());
        dialog.setTitle("Rename folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            renameFolder(result.get(), folderChanging.getId());
        }
    }

    @FXML
    public void onCopyCampaign() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FOLDER_SELECTION));
        Region root = loader.load();
        Stage secondStage = new Stage();
        FolderSelectionController folderSelectionController = loader.getController();
        folderSelectionController.loadTreeFolderList(masterFolderComboBox);
        CmDatas selectedCampaign = campaignTableView.getSelectionModel().getSelectedItem();
        folderSelectionController.setOldCampId(selectedCampaign.getEntity_Id());
        folderSelectionController.setCampaignListController(this);
        folderSelectionController.getSelectedCampNameAndDescription(selectedCampaign.getName(), selectedCampaign.getDescription());
        secondStage.setScene(new Scene(root));
        secondStage.initModality(Modality.APPLICATION_MODAL);
        secondStage.showAndWait();
    }

    @FXML
    public void onSearch() throws IOException {
        loadCampaignTable(null);
    }

    @FXML
    public void onActiveCamp() throws IOException {
        changeCampaignStatus(STATUS_ACTIVE);
    }

    @FXML
    public void onDeactiveCamp() throws IOException {
        changeCampaignStatus(STATUS_INACTIVE);
    }

    @FXML
    public void onSubmitCamp() throws IOException {
        changeCampaignStatus(STATUS_INACTIVE);
    }

    @FXML
    public void onRejectCamp() throws IOException {
        changeCampaignStatus(STATUS_DRAFT);
    }



    private void loadDataFromAPI() throws IOException {
        loadStatusList();
        loadMasterFolderList(null, null, null);
    }

    private void configurationView() {
        campClName.setCellValueFactory(new PropertyValueFactory<>("name"));
        campClDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        campClStatus.setCellValueFactory(new PropertyValueFactory<>("status_Cd_name"));
        campClFolder.setCellValueFactory(new PropertyValueFactory<>("path"));
        campClCreBy.setCellValueFactory(new PropertyValueFactory<>("create_User"));
        campClCreOn.setCellValueFactory(new PropertyValueFactory<>("create_Dttm"));
        campClModBy.setCellValueFactory(new PropertyValueFactory<>("update_User"));
        campClModOn.setCellValueFactory(new PropertyValueFactory<>("update_Dttm"));
        disableListCommandButton();

    }

    private void disableListCommandButton() {
        deactiveCampButton.setDisable(true);
        activeCampButton.setDisable(true);
        submitCampButton.setDisable(true);
        rejectCampButton.setDisable(true);
        editCampButton.setDisable(true);
        copyCampButton.setDisable(true);
    }

    private void addListener() {
        masterFolderComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedMasterFolder = newVal;
                try {
                    loadSubFolderListOnTree(null, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        folderTree.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if(event.getClickCount() == 2) {
                    disableListCommandButton();
                    try {
                        loadCampaignTable(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        campaignTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updateStatusCommandButton(newValue.getStatus_Cd());
                    }
                }
        );
    }

    private void updateStatusCommandButton(String status) {
        copyCampButton.setDisable(false);
        switch (status) {
            case STATUS_ACTIVE:
                deactiveCampButton.setDisable(false);
                rejectCampButton.setDisable(true);
                submitCampButton.setDisable(true);
                activeCampButton.setDisable(true);
                editCampButton.setDisable(true);
                break;
            case STATUS_INACTIVE:
                activeCampButton.setDisable(false);
                rejectCampButton.setDisable(false);
                deactiveCampButton.setDisable(true);
                submitCampButton.setDisable(true);
                editCampButton.setDisable(true);
                break;
            case STATUS_DRAFT:
                deactiveCampButton.setDisable(true);
                activeCampButton.setDisable(true);
                rejectCampButton.setDisable(true);
                submitCampButton.setDisable(false);
                editCampButton.setDisable(false);
                break;
            default:
                deactiveCampButton.setDisable(true);
                activeCampButton.setDisable(true);
                rejectCampButton.setDisable(true);
                submitCampButton.setDisable(true);
                editCampButton.setDisable(true);
                break;
        }
    }

    public void loadMasterFolderList(ControlBindingObj selectedMasterParam, ControlBindingObj selectedSubParam, String campaignName) throws IOException {
        String urlForMasterFolder = SERVER_URL + "/cm/list/folder_up?link_id=" + linkId;
        String response = getResponseFromAPI(urlForMasterFolder);
        MasterFolderModel masterFolderObj = gson.fromJson(response, MasterFolderModel.class);
        if (masterFolderObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<ControlBindingObj> folderList = FXCollections.observableArrayList();
            masterFolderObj.getCmFolderList().getCmFolderData().
                    forEach(index -> folderList.add(new ControlBindingObj(index.getName(), index.getFolder_Id())));
            masterFolderComboBox.setItems(folderList);
            if (selectedMasterParam != null) {
                masterFolderComboBox.getSelectionModel().select(selectedMasterParam);
                selectedMasterFolder = selectedMasterParam;
                loadSubFolderListOnTree(selectedSubParam.getName(), campaignName);
            } else {
                masterFolderComboBox.getSelectionModel().selectFirst();
                selectedMasterFolder = masterFolderComboBox.getSelectionModel().getSelectedItem();
                loadSubFolderListOnTree(null, null);
            }
        } else if (masterFolderObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, masterFolderObj.getResultinfo().getErrString());
        }
    }


    private void loadSubFolderListOnTree(String selectedFolderName, String campaignName) throws IOException {
        TreeItem<ControlBindingObj> item = loadFolderTreeItem(selectedMasterFolder);
        folderTree.setRoot(item);
        folderTree.setShowRoot(false);
        if (selectedFolderName == null) {
            folderTree.getSelectionModel().selectFirst();
        } else {
            TreeItem<ControlBindingObj> selectedFolderItem = item.getChildren().stream()
                    .filter(index -> index.getValue().getName().equals(selectedFolderName))
                    .findAny()
                    .orElse(null);
            if (selectedFolderItem != null) {
                folderTree.getSelectionModel().select(selectedFolderItem);
            }
        }
        loadCampaignTable(campaignName);
    }

    private void loadCampaignTable(String selectedCampaignId) throws IOException {
        String subFolderId = folderTree.getSelectionModel().getSelectedItems().get(0).getValue().getId();
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
        String responseForCampList = getResponseFromAPI(urlForCampaignList);
        CampaignListModel campListObj = gson.fromJson(responseForCampList, CampaignListModel.class);
        if (campListObj.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<CmDatas> campListObs = FXCollections.observableArrayList();
            campListObj.getCmList().getCmDatas().forEach(index -> campListObs.
                    add(new CmDatas(index.getName(),
                            index.getDescription(), index.getStatus_Cd_name(), index.getPath(),
                            index.getCreate_User(), index.getCreate_Dttm(), index.getUpdate_User(),
                            index.getUpdate_Dttm(), index.getEntity_Id(), index.getStatus_Cd())));
            campaignTableView.setItems(campListObs);
            if (selectedCampaignId != null) {
                CmDatas cmDatas = campaignTableView.getItems().stream()
                        .filter(index -> index.getName().equals(selectedCampaignId))
                        .findAny()
                        .orElse(null);
                if (cmDatas != null) {
                    campaignTableView.getSelectionModel().select(cmDatas);
                }
            } else {
                campaignTableView.getSelectionModel().selectFirst();
            }
            updateStatusCommandButton(campaignTableView.getSelectionModel().getSelectedItem().getStatus_Cd());
            recordsNumber.setText(campListObj.getCmList().getCnt());
        } else if (campListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, campListObj.getResultinfo().getErrString());
        }
    }

    private void addFolder(String folderName) throws IOException {
        String urlForAddFolder = null;
        try {
            urlForAddFolder = SERVER_URL + "/cm/ins/folder?link_id=" + linkId +
                    "&parentfolderId=" + selectedMasterFolder.getId() + "&name=" + URLEncoder.encode(folderName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForAddFolder = getResponseFromAPI(urlForAddFolder);
        Resultinfo resultinfo = gson.fromJson(responseForAddFolder, Resultinfo.class);
        afterFolderAction(resultinfo, folderName);
    }

    private void renameFolder(String newName, String folderId) throws IOException {
        String urlForRenameFolder = null;
        try {
            urlForRenameFolder = SERVER_URL + "/cm/update/folder_name?link_id=" + linkId + "&folderid=" + folderId + "&name=" + URLEncoder.encode(newName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForRenameFolder = getResponseFromAPI(urlForRenameFolder);
        Resultinfo resultinfo = gson.fromJson(responseForRenameFolder, Resultinfo.class);
        afterFolderAction(resultinfo, newName);
    }

    private void afterFolderAction(Resultinfo resultinfo, String name) throws IOException {
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            loadSubFolderListOnTree(name, null);
        } else if(resultinfo.getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString());
        }
    }

    private void loadStatusList() throws IOException {
        String urlForStatusList = SERVER_URL + "/dbs/list/cd?link_id=" + linkId + "&cdtype=1";
        String responseForStatusList = getResponseFromAPI(urlForStatusList);
        StatusListModel statusListModel = gson.fromJson(responseForStatusList,  StatusListModel.class);
        if (statusListModel.getResultinfo().getErrCd() == API_CODE_SUCCESS) {
            ObservableList<ControlBindingObj> statusList = FXCollections.observableArrayList();
            statusListModel.getCdList().getCds().forEach(index -> statusList.add(new ControlBindingObj(index.getName(), index.getCd())));
            statusListComboBox.setItems(statusList);
            statusListComboBox.getSelectionModel().selectFirst();
        } else if(statusListModel.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, statusListModel.getResultinfo().getErrString());
        }
    }

    private void changeCampaignStatus(String status) throws IOException {
        String campaignId = campaignTableView.getSelectionModel().getSelectedItem().getEntity_Id();
        String urlForChangeStatus = SERVER_URL + "/cm/change/status?link_id=" + linkId + "&cm_id=" + campaignId + "&statuscd=" + status;
        String responseForChangeStatus = getResponseFromAPI(urlForChangeStatus);
        Resultinfo resultinfo = gson.fromJson(responseForChangeStatus, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            loadCampaignTable(campaignTableView.getSelectionModel().getSelectedItem().getName());
        } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString());
        }
    }
}
