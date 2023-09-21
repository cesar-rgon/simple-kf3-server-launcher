package stories.maps;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class MapsController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsController.class);

    @FXML private AnchorPane mapsAnchorPane;
    @FXML private FlowPane steamCustomMapsFlowPane;
    @FXML private FlowPane steamOfficialMapsFlowPane;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Slider columnSlider;
    @FXML private TabPane mapsTabPane;
    @FXML private HBox menuHbox;
    @FXML private CheckBox selectAllSteamCustomMapsCheckBox;
    @FXML private CheckBox selectAllEpicCustomMapsCheckBox;
    @FXML private Tab steamCustomMapsTab;
    @FXML private TextField searchTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            mapsTabPane.setMinHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            mapsTabPane.setMaxHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            Utils.setNodeBackground(mapsAnchorPane);
            menuHbox.getStyleClass().add("hbox");
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
            columnSliderOnMouseClicked();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        Session.getInstance().getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                columnSliderOnMouseClicked();
            }
        });
    }

     @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsAnchorPane);
    }


    @FXML
    private void columnSliderOnMouseClicked() {
        progressIndicator.setVisible(true);

        Task<List<VBox>> task = new Task<List<VBox>>() {
            @Override
            protected List<VBox> call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();
                List<VBox> mapBoxList = new ArrayList<VBox>();

                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto3(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto1(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto2(), true, columnSlider.getValue()));

                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto6(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto4(), true, columnSlider.getValue()));
                mapBoxList.add(Utils.createMapBox(exampleMaps.getMapDto5(), true, columnSlider.getValue()));
                return mapBoxList;
            }
        };

        task.setOnSucceeded(wse -> {
            List<VBox> mapBoxList = task.getValue();
            steamCustomMapsFlowPane.getChildren().clear();
            for (int i=0; i<8; i++ ) {
                steamCustomMapsFlowPane.getChildren().add(mapBoxList.get(i));
            }

            steamOfficialMapsFlowPane.getChildren().clear();
            for (int i=8; i<16; i++ ) {
                steamOfficialMapsFlowPane.getChildren().add(mapBoxList.get(i));
            }

            // To force to refresh the screen
            mapsTabPane.setMinHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            mapsTabPane.setMaxHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);

            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void selectAllSteamCustomMapsOnMouseClicked() {
        ObservableList<Node> steamCustomMapBoxList = steamCustomMapsFlowPane.getChildren();
        for (Node mapBoxNode: steamCustomMapBoxList) {
            try {
                Node labelVbox = ((VBox)mapBoxNode).getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
                Node actionsFlowPane = ((VBox)labelVbox).getChildren().stream().filter(flowPane -> "actionsFlowPane".equals(flowPane.getId())).findFirst().orElseGet(null);
                Node selected = ((FlowPane)actionsFlowPane).getChildren().stream().filter(checkbox -> "selected".equals(checkbox.getId())).findFirst().orElseGet(null);
                ((CheckBox)selected).setSelected(selectAllSteamCustomMapsCheckBox.isSelected());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @FXML
    private void selectAllEpicCustomMapsOnMouseClicked() {

    }

}
