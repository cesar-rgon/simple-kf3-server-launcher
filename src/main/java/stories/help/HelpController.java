package stories.help;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import stories.install.InstallController;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HelpController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane steam;
    @FXML private VBox helpVbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(helpVbox);
            accordion.setExpandedPane(steam);

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !MainApplication.getMusicPlayer().isAutoPlay()) {
                MainApplication.getMusicPlayer().setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void helpVboxOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(helpVbox);
    }
}
