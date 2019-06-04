package main.java.Controllers;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.ControlBindingObj;
import main.java.Models.ModelObject.Resultinfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicReference;


public class FolderSelectionController extends Main {
    @FXML
    private TextField newName;
    @FXML
    private TextField newDescription;
    @FXML
    private TreeView<ControlBindingObj> selectionFolderTree;
    @FXML
    private Button duplicateBtn;
    @FXML
    private VBox campaignInfo;

    private CampaignListController campaignListController;
    private Stage currentStage;
    private String oldCampId;
    private boolean isCopy;

    public void setIsCopy(boolean copy) {
        isCopy = copy;
    }

    public void setOldCampId(String oldCampId) {
        this.oldCampId = oldCampId;
    }

    private String getOldCampId() {
        return oldCampId;
    }

    void setCampaignListController(CampaignListController campaignListController) {
        this.campaignListController = campaignListController;
    }

    void getSelectedCampNameAndDescription(String name, String description) {
        if (isCopy) {
            newName.setText("Copy Of " + name);
            newDescription.setText(description);
        } else {
            campaignInfo.setVisible(false);
            campaignInfo.setManaged(false);
        }
    }

    @FXML
    public void onInteractCampaign() {
        String campName = newName.getText();
        String campDescription = newDescription.getText();
        if (selectionFolderTree.getSelectionModel().isEmpty() || !selectionFolderTree.getSelectionModel().getSelectedItem().isLeaf()) {
            createNotificationDialog("Please select sub folder", null, null);
            return;
        }
        String newFolderId = selectionFolderTree.getSelectionModel().getSelectedItem().getValue().getId();
        Task<String> newTask;
        AtomicReference<String> urlForInteractCampaign = new AtomicReference<>("");
        if (isCopy) {
            try {
                urlForInteractCampaign.set(SERVER_URL + "/cm/copy?link_id=" + linkId + "&old_cm_id=" + getOldCampId()
                        + "&name=" + URLEncoder.encode(campName, "utf-8") + "&desc=" + URLEncoder.encode(campDescription, "utf-8")
                        + "&folderid=" + newFolderId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            newTask = new Task<String>() {
                @Override
                public String call() {
                    return getResponseFromAPI(urlForInteractCampaign.get());

                }
            };
        } else {
            urlForInteractCampaign.set(SERVER_URL + "/cm/tofolder?link_id=" + linkId + "&cm_id=" + getOldCampId()
                    + "&folderid=" + newFolderId);
            newTask = new Task<String>() {
                @Override
                public String call() {
                    return getResponseFromAPI(urlForInteractCampaign.get());
                }
            };
        }
        loadingStage.show();
        newTask.setOnSucceeded(event -> {
            Resultinfo resultinfo = gson.fromJson((String) event.getSource().getValue(), Resultinfo.class);
            switch (resultinfo.getErrCd()) {
                case API_CODE_SUCCESS:
                    createNotificationDialog(SUCCESS_HEADER, null, null);
                    TreeItem<ControlBindingObj> parentFolder = selectionFolderTree.getSelectionModel().getSelectedItem().getParent();
                    TreeItem<ControlBindingObj> subFolder = selectionFolderTree.getSelectionModel().getSelectedItem();
                    CampaignListController.selectedMasterFolder = parentFolder.getValue();
                    CampaignListController.selectedSubFolder = subFolder.getValue().getName();
                    campaignListController.loadMasterFolderList();
                    currentStage = (Stage) selectionFolderTree.getScene().getWindow();
                    currentStage.close();
                    break;
                case API_CODE_LOGOUT:
                    logoutByExpireSession(urlForInteractCampaign.get());
                    break;
                default:
                    createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), urlForInteractCampaign.get());
                    break;
            }
            loadingStage.hide();
        });
        new Thread(newTask).start();
    }

    @FXML
    public void onCancelDuplicateCampaign(ActionEvent actionEvent) {
        currentStage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        currentStage.close();
    }

    public void initialize() {
        selectionFolderTree.setShowRoot(false);
        setTextFieldLength(newName, 40);
        setTextFieldLength(newDescription, 100);
        duplicateBtn.disableProperty().bind(Bindings.size(selectionFolderTree.getSelectionModel().getSelectedItems()).isEqualTo(0));
    }

    void loadTreeFolderList(ComboBox<ControlBindingObj> masterFolder){
        TreeItem<ControlBindingObj> folderTreeRoot = new TreeItem<>();
        masterFolder.getItems()
                .forEach(index -> folderTreeRoot.getChildren().add(loadFolderTreeItem(index)));
        selectionFolderTree.setRoot(folderTreeRoot);
    }
}
