package main.java;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.Models.MasterFolderModel;
import main.java.Models.ModelObject.ControlBindingObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class Main extends Application {
    public static final String TITLE = "JavaFx";
    public static final String ICON = "/images/icons8-java-96.png";
    public static final String SERVER_URL = "http://192.168.1.81:8180/rest";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final String CAMPAIGN_LIST_FXML = "/fxml/campaignList.fxml";
    public static final String COMMUNICATION_MANAGER_FXML = "/fxml/communicationManager.fxml";
    public static final String LOGIN_FXML = "/fxml/login.fxml";
    public static final String SELECTION_DATA_SOURCE_FXML = "/fxml/selectionDataSource.fxml";
    public static final String FOLDER_SELECTION = "/fxml/folderSelection.fxml";
    public static final int API_CODE_SUCCESS = 0;
    public static final int API_CODE_LOGOUT = 9999;


    private static String linkId = "";
    private static Stage window;
    private static Scene scene;

    public static Stage getWindow() {
        return window;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public static Scene getScene() {
        return scene;
    }
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
    public static String getLinkId() { return linkId; }

    public void initialize() {
    }

    public static void setTextFieldLength(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new  ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    public void stop() {}

    protected void setSceneByView(String resourceName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourceName));
        getScene().setRoot(root);
    }

    public void lg(String a) {
        System.out.println(a);
    }

    public static Gson gson = new Gson();

    public void logoutByExpireSession() throws IOException {
        // dialog
        logout();
    }

    public void logout() throws IOException {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        getWindow().setWidth(WIDTH);
        getWindow().setHeight(HEIGHT);
        getWindow().setX((primScreenBounds.getWidth() - WIDTH) / 2);
        getWindow().setY((primScreenBounds.getHeight() - HEIGHT) / 2);
        setSceneByView(LOGIN_FXML);
    }
    public TreeItem<ControlBindingObject> loadFolderTreeItem(ControlBindingObject masterFolder) throws IOException {
        String urlForSubFolder = SERVER_URL + "/cm/list/folder?link_id=" + getLinkId() +"&parentid=" + masterFolder.getId();
        String responseForSubFolder = getResponseFromAPI(urlForSubFolder);
        MasterFolderModel subFolderObj = gson.fromJson(responseForSubFolder, MasterFolderModel.class);
        if (subFolderObj.getResultinfo().getErrCd() == 0) {
            TreeItem<ControlBindingObject> item = new TreeItem<>(masterFolder);
            subFolderObj.getCmFolderList().getCmFolderData().
                    forEach(index -> item.getChildren().add(new TreeItem<>(
                            new ControlBindingObject(index.getDisplay_Name(), index.getFolder_Id()))));
            return item;
        } else if (subFolderObj.getResultinfo().getErrCd() == API_CODE_LOGOUT) {
            setSceneByView(LOGIN_FXML);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            getWindow().setWidth(WIDTH);
            getWindow().setHeight(HEIGHT);
            getWindow().setX((primScreenBounds.getWidth() - WIDTH) / 2);
            getWindow().setY((primScreenBounds.getHeight() - HEIGHT) / 2);
            return null;
        }
        return null;
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
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        setScene(new Scene(loginRoot, WIDTH, HEIGHT));
        getWindow().setScene(getScene());
        getWindow().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
