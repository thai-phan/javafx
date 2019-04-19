package main.java.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.ControlBindingObject;
import main.java.Models.ModelObject.Resultinfo;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FolderSelectionController extends Main {
    @FXML
    private TextField newName;
    @FXML
    private TextField newDescription;
    @FXML
    private TreeView<ControlBindingObject> selectionFolderTree;

    private Stage currentStage;
    private String oldCampId;

    public void setOldCampId(String oldCampId) {
        this.oldCampId = oldCampId;
    }

    public String getOldCampId() {
        return oldCampId;
    }

    public void getSelectedCampNameAndDescription(String name, String description) {
        newName.setText("Copy Of " + name);
        newDescription.setText(description);
    }

    @FXML
    public void onDuplicateCampaign() {
        String newCampName = newName.getText();
        String newCampDescription = newDescription.getText();
        String newFolderId = selectionFolderTree.getSelectionModel().getSelectedItem().getValue().getId();
        String urlForDuplicateCampaign = null;
        try {
            urlForDuplicateCampaign = SERVER_URL + "/cm/copy?link_id=" + getLinkId() + "&old_cm_id=" + getOldCampId()
                    + "&name=" + URLEncoder.encode(newCampName, "utf-8") + "&desc=" + URLEncoder.encode(newCampDescription, "utf-8")
                    + "&folderid=" + newFolderId;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        lg(urlForDuplicateCampaign);
        String responseForDuplicateCampaign = getResponseFromAPI(urlForDuplicateCampaign);
        Resultinfo resultinfo = gson.fromJson(responseForDuplicateCampaign, Resultinfo.class);
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            currentStage = (Stage) selectionFolderTree.getScene().getWindow();
            currentStage.close();
        } else if (resultinfo.getErrCd() == API_CODE_LOGOUT) {

        }
        lg(responseForDuplicateCampaign);

    }

    @FXML
    public void onCancelDuplicateCampaign() {
        currentStage = (Stage) selectionFolderTree.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize() {
    }

    public void loadTreeFolderList(ComboBox<ControlBindingObject> masterFolder){
        TreeItem<ControlBindingObject> folderTreeRoot = new TreeItem<>();
        masterFolder.getItems()
                .forEach(index -> folderTreeRoot.getChildren().add(loadMasterFolderTreeItem(index)));
        selectionFolderTree.setRoot(folderTreeRoot);
        selectionFolderTree.setShowRoot(false);
    }

    private TreeItem<ControlBindingObject> loadMasterFolderTreeItem(ControlBindingObject index) {
        TreeItem<ControlBindingObject> masterFolder = null;
        try {
            masterFolder = loadFolderTreeItem(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return masterFolder;
    }
}
