package main.java.Controllers;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    @FXML
    private Button duplicateBtn;
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
    public void onDuplicateCampaign() {
        String newCampName = newName.getText();
        String newCampDescription = newDescription.getText();
        if (selectionFolderTree.getSelectionModel().isEmpty() || !selectionFolderTree.getSelectionModel().getSelectedItem().isLeaf()) {
            createNotificationDialog("Please select sub folder", null, null);
            return;
        }
        String newFolderId = selectionFolderTree.getSelectionModel().getSelectedItem().getValue().getId();
        String urlForDuplicateCampaign = null;
        try {
            urlForDuplicateCampaign = SERVER_URL + "/cm/copy?link_id=" + linkId + "&old_cm_id=" + getOldCampId()
                    + "&name=" + URLEncoder.encode(newCampName, "utf-8") + "&desc=" + URLEncoder.encode(newCampDescription, "utf-8")
                    + "&folderid=" + newFolderId;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalUrlForDuplicateCampaign = urlForDuplicateCampaign;
        Task<String> createTask = new Task<String>() {
            @Override
            public String call() throws IOException {
                return getResponseFromAPI(finalUrlForDuplicateCampaign);

            }
        };
        loadingStage.show();
        createTask.setOnSucceeded(event -> {
            Resultinfo resultinfo = gson.fromJson((String) event.getSource().getValue(), Resultinfo.class);
            if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
                TreeItem<ControlBindingObj> parentFolder = selectionFolderTree.getSelectionModel().getSelectedItem().getParent();
                TreeItem<ControlBindingObj> subFolder = selectionFolderTree.getSelectionModel().getSelectedItem();
                campaignListController.selectedMasterFolder = parentFolder.getValue();
                campaignListController.selectedSubFolder = subFolder.getValue().getName();
                campaignListController.loadMasterFolderList();
                currentStage = (Stage) selectionFolderTree.getScene().getWindow();
                currentStage.close();
            } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {
                try {
                    logoutByExpireSession(finalUrlForDuplicateCampaign);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), finalUrlForDuplicateCampaign);
            }
            loadingStage.hide();
        });

        new Thread(createTask).start();

    }

    @FXML
    public void onCancelDuplicateCampaign(ActionEvent actionEvent) {
        currentStage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        currentStage.close();
    }

    public void initialize() {
        setTextFieldLength(newName, 40);
        setTextFieldLength(newDescription, 100);
        duplicateBtn.disableProperty().bind(Bindings.size(selectionFolderTree.getSelectionModel().getSelectedItems()).isEqualTo(0));
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
