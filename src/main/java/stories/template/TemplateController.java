package stories.template;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class TemplateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(TemplateController.class);

    @FXML private Button menuHome;
    @FXML private Button menuWebAdmin;
    @FXML private Button menuMaps;
    @FXML private Button menuInstall;
    @FXML private Button menuHelp;

    public TemplateController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void menuHomeonMouseClicked() {
        menuHome.setDisable(true);
        menuWebAdmin.setDisable(false);
        menuMaps.setDisable(false);
        menuInstall.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/home.fxml");
    }

    @FXML
    private void menuWebAdminonMouseClicked() {
        menuWebAdmin.setDisable(true);
        menuHome.setDisable(false);
        menuMaps.setDisable(false);
        menuInstall.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/webadmin.fxml");
    }

    @FXML
    private void menuMapsonMouseClicked() {
        menuMaps.setDisable(true);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        menuInstall.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/maps.fxml");
    }

    @FXML
    private void menuInstallonMouseClicked() {
        menuInstall.setDisable(true);
        menuMaps.setDisable(false);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/install.fxml");
    }

    @FXML
    private void menuHelponMouseClicked() {
        menuHelp.setDisable(true);
        menuInstall.setDisable(false);
        menuMaps.setDisable(false);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        loadContent("/views/help.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            VBox templateContent = (VBox)((ScrollPane) MainApplication.getTemplate().getNamespace().get("content")).getContent();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlPath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
