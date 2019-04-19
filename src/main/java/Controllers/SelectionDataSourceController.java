package main.java.Controllers;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import main.java.Main;
import main.java.Models.DatabaseListModel;
import main.java.Models.ViewListModel;


public class SelectionDataSourceController extends Main {
    @FXML
    private ComboBox databaseListComboBox;
    @FXML
    private ComboBox viewListComboBox;
    Gson gson = new Gson();
    @Override
    public void initialize() {

        loadDataFromAPI();
    }
    @FXML
    protected void saveCampaign(ActionEvent event) throws Exception {
        setSceneByView(CAMPAIGN_LIST_FXML);
    }

    private void loadDataFromAPI() {

    }

}
