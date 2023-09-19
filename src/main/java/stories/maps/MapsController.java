package stories.maps;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
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
import start.MainApplication;
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
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Slider steamCustomColumnSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<List<VBox>> task = new Task<List<VBox>>() {
            @Override
            protected List<VBox> call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();
                List<VBox> mapBoxList = new ArrayList<VBox>();
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));

                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamCustomColumnSlider.getValue()));
                return mapBoxList;
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

            List<VBox> mapBoxList = task.getValue();
            for (int i=0; i<8; i++ ) {
                steamCustomMaps.getChildren().add(mapBoxList.get(i));
            }
            for (int i=8; i<16; i++ ) {
                steamOfficialMaps.getChildren().add(mapBoxList.get(i));
            }
            progressIndicator.setVisible(false);
        });
        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();

        Session.getInstance().getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                steamCustomColumnSliderOnMouseClicked();
            }
        });
    }

    @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsStackPane);
    }

    @FXML
    private void steamCustomColumnSliderOnMouseClicked() {
        progressIndicator.setVisible(true);

        Task<List<VBox>> task = new Task<List<VBox>>() {
            @Override
            protected List<VBox> call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();
                List<VBox> mapBoxList = new ArrayList<VBox>();
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, steamCustomColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, steamCustomColumnSlider.getValue()));
                return mapBoxList;
            }
        };

        task.setOnSucceeded(wse -> {
            List<VBox> mapBoxList = task.getValue();
            steamCustomMaps.getChildren().clear();
            for (int i=0; i<8; i++ ) {
                steamCustomMaps.getChildren().add(mapBoxList.get(i));
            }
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}
