package stories.home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane basic;
    @FXML private VBox homeVbox;

    public HomeController() {
        super();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(homeVbox);
            accordion.setExpandedPane(basic);

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup) {
                MainApplication.getMusicPlayer().setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void homeVboxOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(homeVbox);
    }
 }
