package stories.setup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    private static final Logger logger = LogManager.getLogger(SetupController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane setupProfiles;
    @FXML private TitledPane setupGameTypes;
    @FXML private TitledPane setupDifficulties;
    @FXML private TitledPane setupLengths;
    @FXML private StackPane setupStackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(setupStackPane);
            if (Session.getInstance().getSetupType() != null) {
                switch (Session.getInstance().getSetupType()) {
                    case "gameTypes":
                        accordion.setExpandedPane(setupGameTypes);
                        break;
                    case "difficulties":
                        accordion.setExpandedPane(setupDifficulties);
                        break;
                    case "lengths":
                        accordion.setExpandedPane(setupLengths);
                        break;
                    default:
                        accordion.setExpandedPane(setupProfiles);
                }
            } else {
                accordion.setExpandedPane(setupProfiles);
            }
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
    private void setupStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(setupStackPane);
    }
}
