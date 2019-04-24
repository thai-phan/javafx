package main.java.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.ControlBindingObj;
import main.java.Models.ModelObject.Resultinfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FolderSelectionController extends Main {
    @FXML
    private TextField newName;
    @FXML
    private TextField newDescription;
    @FXML
    private TreeView<ControlBindingObj> selectionFolderTree;

    private CampaignListController campaignListController;
    private Stage currentStage;
    private String oldCampId;

    public void setOldCampId(String oldCampId) {
        this.oldCampId = oldCampId;
    }

    public String getOldCampId() {
        return oldCampId;
    }

    public void setCampaignListController(CampaignListController campaignListController) {
        this.campaignListController = campaignListController;
    }

    public CampaignListController getCampaignListController() {
        return campaignListController;
    }

    public void getSelectedCampNameAndDescription(String name, String description) {
        newName.setText("Copy Of " + name);
        newDescription.setText(description);
    }

    @FXML
    public void onDuplicateCampaign() throws IOException {
        String newCampName = newName.getText();
        String newCampDescription = newDescription.getText();
        String newFolderId = selectionFolderTree.getSelectionModel().getSelectedItem().getValue().getId();
        String urlForDuplicateCampaign = null;
        try {
            urlForDuplicateCampaign = SERVER_URL + "/cm/copy?link_id=" + linkId + "&old_cm_id=" + getOldCampId()
                    + "&name=" + URLEncoder.encode(newCampName, "utf-8") + "&desc=" + URLEncoder.encode(newCampDescription, "utf-8")
                    + "&folderid=" + newFolderId;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseForDuplicateCampaign = getResponseFromAPI(urlForDuplicateCampaign);
        Resultinfo resultinfo = gson.fromJson(responseForDuplicateCampaign, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            currentStage = (Stage) selectionFolderTree.getScene().getWindow();
            ControlBindingObj selectedMasterFolder = selectionFolderTree.getSelectionModel().getSelectedItem().getParent().getValue();
            ControlBindingObj selectedSubFolder = selectionFolderTree.getSelectionModel().getSelectedItem().getValue();
            campaignListController.loadMasterFolderList(selectedMasterFolder, selectedSubFolder, newCampName);
            currentStage.close();
        } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(SESSION_EXPIRE_HEADER, SESSION_EXPIRE_CONTENT);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString());
        }
    }

    @FXML
    public void onCancelDuplicateCampaign() {
        currentStage = (Stage) selectionFolderTree.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize() {
    }

    public void loadTreeFolderList(ComboBox<ControlBindingObj> masterFolder){
        TreeItem<ControlBindingObj> folderTreeRoot = new TreeItem<>();
        masterFolder.getItems()
                .forEach(index -> folderTreeRoot.getChildren().add(loadMasterFolderTreeItem(index)));
        selectionFolderTree.setRoot(folderTreeRoot);
        selectionFolderTree.setShowRoot(false);
    }

    private TreeItem<ControlBindingObj> loadMasterFolderTreeItem(ControlBindingObj index) {
        TreeItem<ControlBindingObj> masterFolder = null;
        try {
            masterFolder = loadFolderTreeItem(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return masterFolder;
    }
}
