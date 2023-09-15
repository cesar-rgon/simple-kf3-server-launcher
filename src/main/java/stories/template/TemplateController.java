package stories.template;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
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
    @FXML private Button menuSetup;
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
        menuSetup.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/home.fxml");
    }

    @FXML
    private void menuWebAdminonMouseClicked() {
        menuWebAdmin.setDisable(true);
        menuHome.setDisable(false);
        menuMaps.setDisable(false);
        menuInstall.setDisable(false);
        menuSetup.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/webadmin.fxml");
    }

    @FXML
    private void menuMapsonMouseClicked() {
        menuMaps.setDisable(true);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        menuInstall.setDisable(false);
        menuSetup.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/maps.fxml");
    }

    @FXML
    private void menuInstallonMouseClicked() {
        menuInstall.setDisable(true);
        menuSetup.setDisable(false);
        menuMaps.setDisable(false);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/install.fxml");
    }

    @FXML
    private void menuSetupOnMouseClicked() {
        menuSetup.setDisable(true);
        menuInstall.setDisable(false);
        menuMaps.setDisable(false);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        menuHelp.setDisable(false);
        loadContent("/views/setup.fxml");
    }

    @FXML
    private void menuHelponMouseClicked() {
        menuHelp.setDisable(true);
        menuInstall.setDisable(false);
        menuSetup.setDisable(false);
        menuMaps.setDisable(false);
        menuHome.setDisable(false);
        menuWebAdmin.setDisable(false);
        loadContent("/views/help.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playVideoOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playVideoOnStartup"));
            if (playVideoOnStartup && Session.getInstance().getVideoPlayer().isAutoPlay()) {
                Session.getInstance().getVideoPlayer().dispose();
            }
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlPath));
            content.setRoot(Session.getInstance().getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
