package test.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
class LoginTest {
    String LOGIN_FXML = "/fxml/login.fxml";
    Scene scene;
    Button loginBtn;
    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
        Region root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void should_contain_login_button() {
        loginBtn = (Button) scene.lookup("#loginButton");
        FxAssert.verifyThat(loginBtn, LabeledMatchers.hasText("Sign In"));
    }

    @Test
    void should_move_to_campaign_list() {
        FxAssert.verifyThat(loginBtn, LabeledMatchers.hasText("Sign In"));
    }
}
