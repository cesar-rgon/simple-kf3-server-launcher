package stories.maps;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ExampleMaps;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    @FXML ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<List<VBox>> task = new Task<List<VBox>>() {
            @Override
            protected List<VBox> call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();
                List<VBox> kf3MapBoxList = new ArrayList<VBox>();
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true));
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true));
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true));
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true));
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true));
                kf3MapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true));
                return kf3MapBoxList;
            }
        };

        task.setOnSucceeded(wse -> {
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

            List<VBox> kf3MapBoxList = task.getValue();
            steamCustomMaps.getChildren().add(kf3MapBoxList.get(0));
            steamCustomMaps.getChildren().add(kf3MapBoxList.get(1));
            steamCustomMaps.getChildren().add(kf3MapBoxList.get(2));
            steamOfficialMaps.getChildren().add(kf3MapBoxList.get(3));
            steamOfficialMaps.getChildren().add(kf3MapBoxList.get(4));
            steamOfficialMaps.getChildren().add(kf3MapBoxList.get(5));

            progressIndicator.setVisible(false);
        });
        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsStackPane);
    }

}
