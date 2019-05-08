package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.Main;
import main.java.Models.ExplainViewModel;
import main.java.Models.ModelObject.ExplainTxts;

import java.io.IOException;

public class ExplainListController extends Main {
    @FXML
    private TableView<ExplainTxts> explainTable;
    @FXML
    private TableColumn<ExplainTxts, String> explainClm;

    private String currentView;
    private String currentDatabase;

    @FXML
    public void onCloseExplainTable(ActionEvent actionEvent) {
        Stage currentWindow = (Stage) ((Node)actionEvent.getTarget()).getScene().getWindow();
        currentWindow.close();
    }

    public void initialize() {
        explainClm.setCellValueFactory(new PropertyValueFactory<>("explainTxt"));
    }

    public void initTableExplain(String dbName, String viewName) throws IOException {
        this.currentDatabase = dbName;
        this.currentView = viewName;
        loadDataForExplainTable();
    }

    private void loadDataForExplainTable() throws IOException {
        String urlForExplainList = SERVER_URL + "/dbs/explain/view?link_id=" + linkId + "&db_nm=" + currentDatabase + "&view_nm=" + currentView;
        String response = getResponseFromAPI(urlForExplainList);
        ExplainViewModel explainViewObj = gson.fromJson(response, ExplainViewModel.class);
        if (explainViewObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && explainViewObj.getResultList().getExplainTxts().size() > 0) {
            ObservableList<ExplainTxts> explainListObs = FXCollections.observableArrayList();
            explainViewObj.getResultList().getExplainTxts().forEach(index -> explainListObs.add(new ExplainTxts(index.getString(), index.getExplainTxt())));
            explainTable.setItems(explainListObs);
        } else if(explainViewObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logoutByExpireSession(urlForExplainList);
        } else if(explainViewObj.getResultinfo().getErrCd() != API_CODE_SUCCESS) {
            createNotificationDialog(ERROR_HEADER, explainViewObj.getResultinfo().getErrString(), urlForExplainList);
        }
    }
}
