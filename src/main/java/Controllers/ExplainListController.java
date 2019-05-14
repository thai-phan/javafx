package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

    void initTableExplain(String dbName, String viewName) {
        this.currentDatabase = dbName;
        this.currentView = viewName;
        loadDataForExplainTable();
    }

    private void loadDataForExplainTable() {
        String urlForExplainList = SERVER_URL + "/dbs/explain/view?link_id=" + linkId + "&db_nm=" + currentDatabase + "&view_nm=" + currentView;

        Task<String> newTask = new Task<String>() {
            @Override
            public String call() {
                return getResponseFromAPI(urlForExplainList);
            }
        };
        loadingStage.show();

        newTask.setOnSucceeded(response -> {
            String responseForFrequencyList = (String) response.getSource().getValue();
            ExplainViewModel explainViewObj = gson.fromJson(responseForFrequencyList, ExplainViewModel.class);
            if (explainViewObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && explainViewObj.getResultList().getExplainTxts().size() > 0) {
                ObservableList<ExplainTxts> explainListObs = FXCollections.observableArrayList();
                explainViewObj.getResultList().getExplainTxts().forEach(index -> explainListObs.add(new ExplainTxts(index.getString(), index.getExplainTxt())));
                explainTable.setItems(explainListObs);
            } else if(explainViewObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
                logoutByExpireSession(urlForExplainList);
            } else if(explainViewObj.getResultinfo().getErrCd() != API_CODE_SUCCESS) {
                createNotificationDialog(ERROR_HEADER, explainViewObj.getResultinfo().getErrString(), urlForExplainList);
            }
            loadingStage.hide();
        });

        new Thread(newTask).start();
    }
}
