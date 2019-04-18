package main.java.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.CampaignListModel;
import main.java.Models.MasterFolderModel;
import main.java.Models.ModelObject.CmDatas;
import main.java.Models.ModelObject.ControlBindingObject;
import main.java.Models.ModelObject.Resultinfo;
import main.java.Models.StatusListModel;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;


public class CampaignListController extends Main
{
    public static final String STATUS_ACTIVE = "1";
    public static final String STATUS_INACTIVE = "2";
    public static final String STATUS_DRAFT = "0";
    @FXML
    private TreeView<ControlBindingObject> folderTree;
    @FXML
    private ComboBox<ControlBindingObject> masterFolderComboBox;
    @FXML
    private TableView<CmDatas> campaignTableView;
    @FXML
    private TableColumn campClName;
    @FXML
    private TableColumn campClDesc;
    @FXML
    private TableColumn campClStatus;
    @FXML
    private TableColumn campClFolder;
    @FXML
    private TableColumn campClCreBy;
    @FXML
    private TableColumn campClCreOn;
    @FXML
    private TableColumn campClModBy;
    @FXML
    private TableColumn campClModOn;
    @FXML
    private Text recordsNumber;
    @FXML
    private TextField campaignSearch;
    @FXML
    private ComboBox<ControlBindingObject> statusListComboBox;
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

    public void setMasterFolderComboBox(ComboBox<ControlBindingObject> masterFolderComboBox) {
        this.masterFolderComboBox = masterFolderComboBox;
    }

    public ComboBox<ControlBindingObject> getMasterFolderComboBox() {
        return masterFolderComboBox;
    }

    private static ControlBindingObject selectedMasterFolder;

    public static void setSelectedMasterFolder(ControlBindingObject selectedMasterFolderId) {
        CampaignListController.selectedMasterFolder = selectedMasterFolderId;
    }

    public static ControlBindingObject getSelectedMasterFolder() {
        return selectedMasterFolder;
    }


    @FXML
    private void editCampaign(ActionEvent event) throws Exception {
        setScene(COMMUNICATION_MANAGER_FXML);
    }

    @FXML
    private void onLogout() throws IOException {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        getWindow().setWidth(WIDTH);
        getWindow().setHeight(HEIGHT);
        getWindow().setX((primScreenBounds.getWidth() - WIDTH) / 2);
        getWindow().setY((primScreenBounds.getHeight() - HEIGHT) / 2);
        setScene(LOGIN_FXML);
    }

    @FXML
    private void showAddFolderDialog(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() ) addFolder(result.get());
    }

    @FXML
    public void showRenameFolderDialog() throws IOException {
        ControlBindingObject folderChanging = folderTree.getSelectionModel().getSelectedItems().get(0).getValue();
        TextInputDialog dialog = new TextInputDialog(folderChanging.getName());
        dialog.setTitle("Rename folder");
        dialog.setHeaderText("Enter new folder name");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() ) renameFolder(result.get(), folderChanging.getId());
    }

    @FXML
    public void onCopyCampaign() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FOLDER_SELECTION));
        Region root = (Region) loader.load();
        Stage secondStage = new Stage();
        FolderSelectionController folderSelectionController = loader.<FolderSelectionController>getController();
        folderSelectionController.loadTreeFolderList(masterFolderComboBox);
        secondStage.setScene(new Scene(root));
        secondStage.show();
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

    private void loadDataFromAPI() throws IOException {
        loadStatusList();
        loadMasterFolderList();
    }

    private void configurationView() {
        campClName.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("name"));
        campClDesc.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("description"));
        campClStatus.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("status_Cd_name"));
        campClFolder.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("path"));
        campClCreBy.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("create_User"));
        campClCreOn.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("create_Dttm"));
        campClModBy.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("update_User"));
        campClModOn.setCellValueFactory(new PropertyValueFactory<CmDatas, String>("update_Dttm"));
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
                setSelectedMasterFolder(newVal);
                try {
                    loadSubFolderListOnTree(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        folderTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    if(event.getClickCount() == 2) {
                        try {
                            loadCampaignTable(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        campaignTableView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<CmDatas>() {
                @Override
                public void changed(ObservableValue<? extends CmDatas> observable, CmDatas oldValue, CmDatas newValue) {
                    if (newValue != null) {
                        copyCampButton.setDisable(false);
                        lg(newValue.getStatus_Cd());
                        if (newValue.getStatus_Cd().equals(STATUS_ACTIVE)) {
                            deactiveCampButton.setDisable(false);
                            rejectCampButton.setDisable(true);
                            submitCampButton.setDisable(true);
                            activeCampButton.setDisable(true);
                            editCampButton.setDisable(true);
                        } else if (newValue.getStatus_Cd().equals(STATUS_INACTIVE)) {
                            activeCampButton.setDisable(false);
                            rejectCampButton.setDisable(false);
                            deactiveCampButton.setDisable(true);
                            submitCampButton.setDisable(true);
                            editCampButton.setDisable(true);
                        } else if (newValue.getStatus_Cd().equals(STATUS_DRAFT)){
                            deactiveCampButton.setDisable(true);
                            activeCampButton.setDisable(true);
                            rejectCampButton.setDisable(true);
                            submitCampButton.setDisable(false);
                            editCampButton.setDisable(false);
                        } else {
                            deactiveCampButton.setDisable(true);
                            activeCampButton.setDisable(true);
                            rejectCampButton.setDisable(true);
                            submitCampButton.setDisable(true);
                            editCampButton.setDisable(true);
                        }
                    }
                }
            }
        );
    }

    private void loadMasterFolderList() throws IOException {
        String urlForMasterFolder = SERVER_URL + "/cm/list/folder_up?link_id=" + getLinkId();
        String response = getDataFromAPI(urlForMasterFolder);
        MasterFolderModel masterFolderObj = gson.fromJson(response, MasterFolderModel.class);
        ObservableList<ControlBindingObject> folderList = FXCollections.observableArrayList();
        masterFolderObj.getCmFolderList().getCmFolderData().
                forEach(index -> folderList.add(new ControlBindingObject(index.getName(), index.getFolder_Id())));
        masterFolderComboBox.setItems(folderList);
        if (masterFolderComboBox.getItems().size() >= 1) {
            masterFolderComboBox.getSelectionModel().select(0);
            setSelectedMasterFolder(masterFolderComboBox.getSelectionModel().getSelectedItem());
            loadSubFolderListOnTree(null);
        }
    }


    private void loadSubFolderListOnTree(String selectedFolderName) throws IOException {
        TreeItem<ControlBindingObject> item = loadFolderTreeItem(getSelectedMasterFolder());
        folderTree.setRoot(item);
        folderTree.setShowRoot(false);
        if (selectedFolderName == null) {
            folderTree.getSelectionModel().select(0);
        } else {
            TreeItem<ControlBindingObject> selectedFolderItem = item.getChildren().stream().
                    filter(index -> index.getValue().getName().equals(selectedFolderName))
                    .findAny()
                    .orElse(null);
            if (selectedFolderItem != null) {
                folderTree.getSelectionModel().select(selectedFolderItem);
            }
        }
        loadCampaignTable(null);
    }

    private void loadCampaignTable(String selectedCampaignId) throws IOException {
        String subFolderId = folderTree.getSelectionModel().getSelectedItems().get(0).getValue().getId();
        String statusId = statusListComboBox.getSelectionModel().getSelectedItem().getId() != null ?
                statusListComboBox.getSelectionModel().getSelectedItem().getId() : "";
        String nameSearch = campaignSearch.getText() != null ? campaignSearch.getText() : "";
        String urlForCampaignList = null;
        try {
            urlForCampaignList = SERVER_URL + "/cm/list?link_id=" + getLinkId() + "&folderid=" + subFolderId +
                    "&statuscd=" + statusId + "&cmname=" + URLEncoder.encode(nameSearch, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForCampList = getDataFromAPI(urlForCampaignList);
        lg(responseForCampList);
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
                        .filter(index -> index.getEntity_Id().equals(selectedCampaignId))
                        .findAny()
                        .orElse(null);
                if (cmDatas != null) {
                    campaignTableView.getSelectionModel().select(cmDatas);
                };
            }
//        campaignTableView.setColumnResizePolicy(campaignTableView.UNCONSTRAINED_RESIZE_POLICY);
            recordsNumber.setText(campListObj.getCmList().getCnt());
        } else if (campListObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            onLogout();
        }

    }

    private void addFolder(String folderName) throws IOException {
        String urlForAddFolder = null;
        try {
            urlForAddFolder = SERVER_URL + "/cm/ins/folder?link_id=" + getLinkId() +
                    "&parentfolderId=" + getSelectedMasterFolder().getId() + "&name=" + URLEncoder.encode(folderName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responeForAddFolder = getDataFromAPI(urlForAddFolder);
        Resultinfo resultinfo = gson.fromJson(responeForAddFolder, Resultinfo.class);
        if (resultinfo.getErrCd() == 0) {
            loadSubFolderListOnTree(folderName);
        } else if(resultinfo.getErrCd() == 9999) {
            onLogout();
        }
    }

    private void renameFolder(String newName, String folderId) throws IOException {
        String urlForRenameFolder = null;
        try {
            urlForRenameFolder = SERVER_URL + "/cm/update/folder_name?link_id=" + getLinkId() + "&folderid=" + folderId + "&name=" + URLEncoder.encode(newName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForRenameFolder = getDataFromAPI(urlForRenameFolder);
        Resultinfo resultinfo = gson.fromJson(responseForRenameFolder, Resultinfo.class);
        if (resultinfo.getErrCd() == 0) {
            loadSubFolderListOnTree(newName);
        } else if(resultinfo.getErrCd() == 9999) {
            onLogout();
        }
    }

    private void loadStatusList() throws IOException {
        String urlForStatusList = SERVER_URL + "/dbs/list/cd?link_id=" + getLinkId() + "&cdtype=1";
        String responseForStatusList = getDataFromAPI(urlForStatusList);
        StatusListModel statusListModel = gson.fromJson(responseForStatusList,  StatusListModel.class);
        if (statusListModel.getResultinfo().getErrCd() == 0) {
            ObservableList<ControlBindingObject> statusList = FXCollections.observableArrayList();
            statusListModel.getCdList().getCds().forEach(index -> statusList.add(new ControlBindingObject(index.getName(), index.getCd())));
            statusListComboBox.setItems(statusList);
            if (statusListComboBox.getItems().size() >= 1) {
                statusListComboBox.getSelectionModel().select(0);
            }
        } else if(statusListModel.getResultinfo().getErrCd() == 9999) {
            onLogout();
        }

    }

    private void changeCampaignStatus(String status) throws IOException {
        String campaignId = campaignTableView.getSelectionModel().getSelectedItem().getEntity_Id();
        String urlForChangeStatus = SERVER_URL + "/cm/change/status?link_id=" + getLinkId() + "&cm_id=" + campaignId + "&statuscd=" + status;
        String responseForChangeStatus = getDataFromAPI(urlForChangeStatus);
        Resultinfo resultinfo = gson.fromJson(responseForChangeStatus, Resultinfo.class);
        if (resultinfo.getErrCd() == 0) {
            loadCampaignTable(campaignId);
        } else if (resultinfo.getErrCd() == 9999) {
            onLogout();
        }
    }
}
