package main.java.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ModelObject.ControlBindingObject;

import java.io.IOException;


public class FolderSelectionController extends Main {
    @FXML
    private TreeView<ControlBindingObject> selectionFolderTree;

    @FXML
    private void onDuplicateCampaign() {
        String urlForDuplicateCampaign = "";
        String responseForDuplicateCampaign = getDataFromAPI(urlForDuplicateCampaign);

    }

    @FXML
    private void onCancelDuplicateCampaign() {
        Stage stage = (Stage) selectionFolderTree.getScene().getWindow();
        stage.close();
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
