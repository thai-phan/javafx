package main.java.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import main.java.Main;

;import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommunicationManagerController extends Main {


    @FXML
    private ComboBox frequencyChoiceBox;
    @FXML
    private VBox dailyOptionList;
    @FXML
    private VBox monthlyOptionList;
    @FXML
    private VBox weeklyOptionList;
    @FXML
    private VBox yearlyOptionList;
    @FXML
    private TextField campaignName;
    @FXML
    private TextField campaignDescription;
    @FXML
    private DatePicker actlComStart;
    @FXML
    private DatePicker actlComEnd;
    @FXML
    private DatePicker planComStart;
    @FXML
    private DatePicker planComEnd;
    @FXML
    private DatePicker freqStart;
    @Override
    public void initialize() {
        configurationView();

        fakeData();
    }

    @FXML
    protected void saveCampaign(ActionEvent event) throws Exception {
        Parent communicationManagerRoot = FXMLLoader.load(getClass().getResource(SELECTION_DATA_SOURCE_FXML));
        getScene().setRoot(communicationManagerRoot);
    }

    @FXML
    protected void cancelCampaign(ActionEvent event) throws Exception {
        setScene(CAMPAIGN_LIST_FXML);
    }

    private void configurationView() {
        weeklyOptionList.setVisible(false);
        monthlyOptionList.setVisible(false);
        yearlyOptionList.setVisible(false);
        changeDateTimeToKrType(actlComStart);
        changeDateTimeToKrType(actlComEnd);
        changeDateTimeToKrType(planComStart);
        changeDateTimeToKrType(planComEnd);
        changeDateTimeToKrType(freqStart);
        setTextFieldLength(campaignName, 100);
        setTextFieldLength(campaignDescription, 100);


    }

    private void changeDateTimeToKrType(DatePicker datePicker) {
        String pattern = "yyyy/MM/dd";
        datePicker.setPromptText(pattern.toLowerCase());
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format((date));
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });
    }


    public void fakeData() {
        String st[] = {"Daily", "Weekly", "Monthly", "Yearly"};
        frequencyChoiceBox.getItems().addAll(st);
        frequencyChoiceBox.getSelectionModel().select(0);
        frequencyChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                String choiceValue = frequencyChoiceBox.getItems().get((Integer) newValue);
//                switch (choiceValue) {
//                    case "Daily": {
//                        System.out.println("case1");
//                        dailyOptionList.setVisible(true);
//                        weeklyOptionList.setVisible(false);
//                        monthlyOptionList.setVisible(false);
//                        yearlyOptionList.setVisible(false);
//                        break;
//                    }
//                    case "Weekly": {
//                        dailyOptionList.setVisible(false);
//                        weeklyOptionList.setVisible(true);
//                        monthlyOptionList.setVisible(false);
//                        yearlyOptionList.setVisible(false);
//                        break;
//                    }
//                    case "Monthly": {
//                        dailyOptionList.setVisible(false);
//                        weeklyOptionList.setVisible(false);
//                        monthlyOptionList.setVisible(true);
//                        yearlyOptionList.setVisible(false);
//                        break;
//                    }
//                    case "Yearly": {
//                        dailyOptionList.setVisible(false);
//                        weeklyOptionList.setVisible(false);
//                        monthlyOptionList.setVisible(false);
//                        yearlyOptionList.setVisible(true);
//                        break;
//                    }
//                    default: {
//                    }
//                }
            }
        });
    }
    @FXML
    public void choice() {
        System.out.println(frequencyChoiceBox.getValue());
    }
}
