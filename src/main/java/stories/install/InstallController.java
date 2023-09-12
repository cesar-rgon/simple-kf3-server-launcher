package stories.install;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class InstallController implements Initializable {

    private static final Logger logger = LogManager.getLogger(InstallController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane steam;
    @FXML private VBox installVbox;
    @FXML private TextField steamPathTextField;
    @FXML private TextField epicPathTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(installVbox);
            accordion.setExpandedPane(steam);

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }


    @FXML
    private void installVboxOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(installVbox);
    }

    @FXML
    private void exploreSteamPathOnMouseClicked() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Search root of the Path to KF3's server");
            File selectedDirectory = directoryChooser.showDialog(Session.getInstance().getPrimaryStage());
            if (selectedDirectory != null) {
                steamPathTextField.setText(selectedDirectory.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void exploreEpicPathOnMouseClicked() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Search root of the Path to KF3's server");
            File selectedDirectory = directoryChooser.showDialog(Session.getInstance().getPrimaryStage());
            if (selectedDirectory != null) {
             epicPathTextField.setText(selectedDirectory.getAbsolutePath());
            }
         } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
    }
    }
}
