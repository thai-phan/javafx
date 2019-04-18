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
        setScene(CAMPAIGN_LIST_FXML);
    }

    private void loadDataFromAPI() {
        loadTableList();
    }

    private void loadTableList() {
        String url = SERVER_URL + "/dbs/list/dbname?link_id=" + getLinkId();
        String responseForDatabaseList = getDataFromAPI(url);
        DatabaseListModel databaseListModel = gson.fromJson(responseForDatabaseList, DatabaseListModel.class);
        ObservableList<String> databaseList = FXCollections.observableArrayList();
        databaseListModel.getResultListTable().getDbNames().forEach(index -> databaseList.add(index.getDbname()));
        databaseListComboBox.setItems(databaseList);
        if (databaseListComboBox.getItems().size() >= 1) {
            databaseListComboBox.getSelectionModel().select(0);
            String selectedDBName = databaseListComboBox.getSelectionModel().getSelectedItem().toString();
            loadViewListByDatabaseName(selectedDBName);
            databaseListComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                loadViewListByDatabaseName(newVal.toString());
            });
        }
    }
    private void loadViewListByDatabaseName(String databaseName) {
        String urlForViewList = SERVER_URL + "/dbs/list/view?link_id=" + getLinkId() + "&dbname=" + databaseName;
        String responseForViewList = getDataFromAPI(urlForViewList);
        ViewListModel viewListModel = gson.fromJson(responseForViewList, ViewListModel.class);
        ObservableList<String> viewList = FXCollections.observableArrayList();
        viewListModel.getResultList().getSetViews().forEach(index -> viewList.add(index.getTableName()));
        viewListComboBox.setItems(viewList);
        viewListComboBox.getSelectionModel().select(0);
    }
}
