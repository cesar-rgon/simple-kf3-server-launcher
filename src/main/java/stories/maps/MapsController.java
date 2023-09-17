package stories.maps;

import dtos.MapDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import nodes.Kf3MapBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ExampleMaps;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        ExampleMaps exampleMaps = new ExampleMaps();

        steamCustomMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto1()).getMapBox());
        steamCustomMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto2()).getMapBox());
        steamCustomMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto3()).getMapBox());
        steamOfficialMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto4()).getMapBox());
        steamOfficialMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto5()).getMapBox());
        steamOfficialMaps.getChildren().add(new Kf3MapBox(exampleMaps.getMapDto6()).getMapBox());
    }

    @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsStackPane);
    }
}
