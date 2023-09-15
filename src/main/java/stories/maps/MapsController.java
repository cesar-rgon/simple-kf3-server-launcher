package stories.maps;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
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

public class MapsController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane steamCustomMapsTitledPane;
    @FXML private StackPane mapsStackPane;
    @FXML private FlowPane steamCustomMaps;
    @FXML private FlowPane steamOfficialMaps;
    @FXML private FlowPane epicCustomMaps;
    @FXML private FlowPane epicOfficialMaps;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(mapsStackPane);
            accordion.setExpandedPane(steamCustomMapsTitledPane);
            steamCustomMaps.getStyleClass().add("flowPane");
            steamOfficialMaps.getStyleClass().add("flowPane");
            epicCustomMaps.getStyleClass().add("flowPane");
            epicOfficialMaps.getStyleClass().add("flowPane");

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
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsStackPane);
    }
}
