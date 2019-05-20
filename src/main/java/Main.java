package main.java;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.*;
import javafx.util.StringConverter;
import main.java.Controllers.LoginController;
import main.java.Models.MasterFolderModel;
import main.java.Models.ModelObject.ControlBindingObj;
import main.java.Models.ModelObject.Resultinfo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;


public class Main extends Application {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    protected static final String CAMPAIGN_LIST_FXML = "/src/main/resources/fxml/campaignList.fxml";
    protected static final String COMMUNICATION_MANAGER_FXML = "/src/main/resources/fxml/communicationManager.fxml";
    private static final String LOGIN_FXML = "/src/main/resources/fxml/login.fxml";
    protected static final String FOLDER_SELECTION = "/src/main/resources/fxml/folderSelection.fxml";
    protected static final String EXPLAIN_LIST = "/src/main/resources/fxml/explainList.fxml";
    protected static final String SCHEDULE_DATE = "/src/main/resources/fxml/scheduleDate.fxml";
    private static final String LOADING = "/src/main/resources/fxml/loading.fxml";
    protected static final String PASSWORD = "/src/main/resources/fxml/password.fxml";
    protected static final String CONFIGURATION = "/src/main/resources/fxml/configuration.fxml";


    protected static final int API_CODE_SUCCESS = 0;
    protected static final int API_CODE_LOGOUT = 9999;
    private static final String SESSION_EXPIRE_HEADER = "Session Expired";
    private static final String SESSION_EXPIRE_CONTENT = "Please re-login";
    protected static final String ERROR_HEADER = "Error";
    protected static final String SUCCESS_HEADER = "Action Success";
    protected static final String ALERT_HEADER = "Alert";

    protected final static Logger logger = Logger.getLogger(Main.class);


    private static String TITLE = "";
    private static String ICON = "";
    public static String SERVER_URL = "";
    protected static String linkId = "";
    protected static String currentUsername;
    protected static String currentPassword;
    public static Stage stage;
    public static Scene scene;
    protected static Properties prop;
    protected static Stage loadingStage;

    public Main() {
        prop=new Properties();
        try {
            FileInputStream log4jProperty =  new FileInputStream("log4j.properties");
            PropertyConfigurator.configure(log4jProperty);
            FileInputStream config= new FileInputStream("config.properties");
            prop.load(config);
            SERVER_URL = prop.getProperty("SERVER_URL");
            TITLE = prop.getProperty("TITLE");
            ICON = prop.getProperty("ICON");
            config.close();
        } catch (IOException e) {
            e.printStackTrace();
            createNotificationDialog(e.toString(), null, null);
        }
    }

    public void initialize() {
    }

    private void createLoadingWindow() throws IOException {
        Parent loadingRoot = FXMLLoader.load(getClass().getResource(LOADING));
        loadingStage = new Stage();
        loadingStage.setScene(new Scene(loadingRoot));
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        loadingStage.initModality(Modality.APPLICATION_MODAL);
    }

    protected static void setTextFieldLength(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
    @Override
    public void stop() {
    }

    protected void notificationForAction(Resultinfo resultinfo, String url) {
        if (resultinfo.getErrCd() == API_CODE_SUCCESS) {
            createNotificationDialog(SUCCESS_HEADER, null, url);
        } else if(resultinfo.getErrCd() == API_CODE_LOGOUT){
            logoutByExpireSession(url);
        } else {
            createNotificationDialog(ERROR_HEADER, resultinfo.getErrString(), url);
        }
    }

    protected void setSceneByView(String resourceName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourceName));
        scene.setRoot(root);
    }

    protected void lg(String a) {
        System.out.println(a);
    }

    public static Gson gson = new Gson();

    protected void logoutByExpireSession(String url) {
        createNotificationDialog(Main.SESSION_EXPIRE_HEADER, Main.SESSION_EXPIRE_CONTENT, url);
        try {
            logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void logout() throws IOException {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setX((primScreenBounds.getWidth() - WIDTH) / 2);
        stage.setY((primScreenBounds.getHeight() - HEIGHT) / 2);
        setSceneByView(LOGIN_FXML);
    }

    protected void changeDateTimeToKrTypeAndDisableEditor(DatePicker datePicker) {
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
        datePicker.setEditable(false);
    }

    protected void createNotificationDialog(String header, String content, String url) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        if(header.equals(ERROR_HEADER)){
            String api = url != null ? url : "";
            logger.error("Error: " + content + "API: " + api);
            stage.getIcons().add(
                    new Image(this.getClass().getResource("/src/main/resources/images/error.png").toString()));
        } else {
            stage.getIcons().add(
                    new Image(this.getClass().getResource("/src/main/resources/images/rename.png").toString()));
        }

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/src/main/resources/css/main.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogFolder");
        alert.setTitle("Notification Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public TreeItem<ControlBindingObj> loadFolderTreeItem(ControlBindingObj masterFolder) {
        String urlForSubFolder = SERVER_URL + "/cm/list/folder?link_id=" + linkId +"&parentid=" + masterFolder.getId();
        String responseForSubFolder = getResponseFromAPI(urlForSubFolder);
        MasterFolderModel subFolderObj = gson.fromJson(responseForSubFolder, MasterFolderModel.class);
        if (subFolderObj.getResultinfo().getErrCd() == API_CODE_SUCCESS && subFolderObj.getCmFolderList().getCmFolderData().size() > 0) {
            TreeItem<ControlBindingObj> item = new TreeItem<>(masterFolder);
            subFolderObj.getCmFolderList().getCmFolderData().
                    forEach(index -> item.getChildren().add(new TreeItem<>(
                            new ControlBindingObj(index.getDisplay_Name(), index.getFolder_Id()))));
            return item;
        } else if (subFolderObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            Platform.runLater(() -> {
                try {
                    logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return null;
        } else if (subFolderObj.getResultinfo().getErrCd() != API_CODE_SUCCESS && subFolderObj.getResultinfo().getErrCd() != API_CODE_LOGOUT){
            Platform.runLater(() -> createNotificationDialog(ERROR_HEADER, subFolderObj.getResultinfo().getErrString(), urlForSubFolder));
            return null;
        } else {
            return null;
        }
    }

    protected String getResponseFromAPI(String url) {
        try {
//            lg(url);
            BufferedReader in;
            if (url.matches("^https://.+")) {
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                };

                // Install the all-trusting trust manager
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            } else {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            Platform.runLater(() -> createNotificationDialog(ERROR_HEADER, e.getMessage(), null));
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            createLoadingWindow();
            stage = primaryStage;
            stage.getIcons().add(new Image(ICON));
            stage.setTitle(TITLE);
            stage.setOnCloseRequest(event -> {
                Alert alert = createAlert("Confirm to close window");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    System.exit(0);
                } else {
                    event.consume();
                }
            });
            createLoginWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Alert createAlert(String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(header);
        alert.setContentText(null);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/src/main/resources/css/main.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogFolder");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image(this.getClass().getResource("/src/main/resources/images/confirm.png").toString()));
        return alert;
    }

    private void createLoginWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
        Region root = loader.load();
        LoginController loginController = loader.getController();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        loginController.setKeyBinding(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
