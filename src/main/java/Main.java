package main.java;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.Models.MasterFolderModel;
import main.java.Models.ModelObject.ControlBindingObj;

import java.io.*;
import java.net.*;
import java.util.Properties;


public class Main extends Application {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final String CAMPAIGN_LIST_FXML = "/fxml/campaignList.fxml";
    public static final String COMMUNICATION_MANAGER_FXML = "/fxml/communicationManager.fxml";
    public static final String LOGIN_FXML = "/fxml/login.fxml";
    public static final String FOLDER_SELECTION = "/fxml/folderSelection.fxml";
    public static final int API_CODE_SUCCESS = 0;
    public static final int API_CODE_LOGOUT = 9999;
    public static final String SESSION_EXPIRE_HEADER = "Session Expired";
    public static final String SESSION_EXPIRE_CONTENT = "Please re-login";
    public static final String ERROR_HEADER = "Error";
    public static final String SUCCESS_TITLE = "Action Success";


    public static String TITLE = "";
    public static String ICON = "";
    public static String SERVER_URL = "";
    public static String linkId = "";
    public static Stage window;
    public static Scene scene;
    public static Properties prop;

    public Main() {
        prop=new Properties();
        try {
            FileInputStream config= new FileInputStream("config.properties");
            prop.load(config);
            SERVER_URL = prop.getProperty("SERVER_URL");
            TITLE = prop.getProperty("TITLE");
            ICON = prop.getProperty("ICON");
            config.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void initialize() {
    }

    public static void setTextFieldLength(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    public void stop() {}

    protected void setSceneByView(String resourceName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourceName));
        scene.setRoot(root);
    }

    public void lg(String a) {
        System.out.println(a);
    }

    public static Gson gson = new Gson();

    public void logoutByExpireSession(String header, String content) throws IOException {
        createNotificationDialog(header, content);
        logout();
    }

    public void logout() throws IOException {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        window.setWidth(WIDTH);
        window.setHeight(HEIGHT);
        window.setX((primScreenBounds.getWidth() - WIDTH) / 2);
        window.setY((primScreenBounds.getHeight() - HEIGHT) / 2);
        setSceneByView(LOGIN_FXML);
    }

    public void createNotificationDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public TreeItem<ControlBindingObj> loadFolderTreeItem(ControlBindingObj masterFolder) throws IOException {
        String urlForSubFolder = SERVER_URL + "/cm/list/folder?link_id=" + linkId +"&parentid=" + masterFolder.getId();
        String responseForSubFolder = getResponseFromAPI(urlForSubFolder);
        MasterFolderModel subFolderObj = gson.fromJson(responseForSubFolder, MasterFolderModel.class);
        if (subFolderObj.getResultinfo().getErrCd() == 0) {
            TreeItem<ControlBindingObj> item = new TreeItem<>(masterFolder);
            subFolderObj.getCmFolderList().getCmFolderData().
                    forEach(index -> item.getChildren().add(new TreeItem<>(
                            new ControlBindingObj(index.getDisplay_Name(), index.getFolder_Id()))));
            return item;
        } else if (subFolderObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            logout();
            return null;
        } else {
            createNotificationDialog(ERROR_HEADER, subFolderObj.getResultinfo().getErrString());
            return null;
        }
    }

    protected String getResponseFromAPI(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            createNotificationDialog(e.getMessage(), null);
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            window = primaryStage;
            window.getIcons().add(new Image(ICON));
            window.setTitle(TITLE);
            createLoginWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createLoginWindow() throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource(LOGIN_FXML));
        scene = new Scene(loginRoot, WIDTH, HEIGHT);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
