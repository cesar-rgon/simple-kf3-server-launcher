package stories.maps;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    @FXML private AnchorPane mapsAnchorPane;
    @FXML private FlowPane steamCustomMaps;
    @FXML private FlowPane steamOfficialMaps;
    @FXML private FlowPane epicCustomMaps;
    @FXML private FlowPane epicOfficialMaps;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Slider steamCustomColumnSlider;
    @FXML private Slider steamOfficialColumnSlider;
    @FXML private ScrollPane steamCustomScrollPane;
    @FXML private ScrollPane steamOfficialScrollPane;

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

                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                return mapBoxList;
            }
        };

        task.setOnSucceeded(wse -> {
            try {
                Utils.setNodeBackground(mapsAnchorPane);
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
            steamCustomScrollPane.setPrefHeight(getCustomScrollPaneHeight());

            for (int i=8; i<16; i++ ) {
                steamOfficialMaps.getChildren().add(mapBoxList.get(i));
            }
            steamOfficialScrollPane.setPrefHeight(getOfficialScrollPaneHeight());

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
                steamOfficialColumnSliderOnMouseClicked();
            }
        });
    }

    private double getCustomScrollPaneHeight() {
        double rows = Math.ceil(steamCustomMaps.getChildren().size() / steamCustomColumnSlider.getValue());
        double imageHeight = ((ImageView)((VBox)steamCustomMaps.getChildren().get(0)).getChildren().get(0)).getFitHeight();
        double estimatedHeight = 20 + (rows * (imageHeight + 330));

        if (estimatedHeight > (Session.getInstance().getPrimaryStage().getHeight() - 300)) {
            return Session.getInstance().getPrimaryStage().getHeight() - 300;
        } else {
            return estimatedHeight;
        }
    }

    private double getOfficialScrollPaneHeight() {
        double rows = Math.ceil(steamCustomMaps.getChildren().size() / steamOfficialColumnSlider.getValue());
        double imageHeight = ((ImageView)((VBox)steamOfficialMaps.getChildren().get(0)).getChildren().get(0)).getFitHeight();
        double estimatedHeight = (rows * (imageHeight + 310)) - 80;

        if (estimatedHeight > (Session.getInstance().getPrimaryStage().getHeight() - 400)) {
            return Session.getInstance().getPrimaryStage().getHeight() - 400;
        } else {
            return estimatedHeight;
        }
    }

    @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsAnchorPane);
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
            steamCustomScrollPane.setPrefHeight(getCustomScrollPaneHeight());
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }


    @FXML
    private void steamOfficialColumnSliderOnMouseClicked() {
        progressIndicator.setVisible(true);

        Task<List<VBox>> task = new Task<List<VBox>>() {
            @Override
            protected List<VBox> call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();
                List<VBox> mapBoxList = new ArrayList<VBox>();
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, steamOfficialColumnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, steamOfficialColumnSlider.getValue()));
                return mapBoxList;
            }
        };

        task.setOnSucceeded(wse -> {
            List<VBox> mapBoxList = task.getValue();
            steamOfficialMaps.getChildren().clear();
            for (int i=0; i<8; i++ ) {
                steamOfficialMaps.getChildren().add(mapBoxList.get(i));
            }
            steamOfficialScrollPane.setPrefHeight(getOfficialScrollPaneHeight());
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}
