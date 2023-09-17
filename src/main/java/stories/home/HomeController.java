package stories.home;

import dtos.MapDto;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Callback;
import nodes.Kf3MapBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ExampleMaps;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane basic;
    @FXML private StackPane homeStackPane;
    @FXML private Label gameTypeLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label lengthLabel;
    @FXML private Label serverNameLabel;
    @FXML private Label serverPasswordLabel;
    @FXML private Label selectedMapLabel;
    @FXML private Button setupMaps;
    @FXML private Button setupGameTypes;
    @FXML private Button setupDifficulties;
    @FXML private Button setupLengths;
    @FXML private GridPane logoGridPane;
    @FXML private GridPane combosGridPane;

    public HomeController() {
        super();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(homeStackPane);
            logoGridPane.getStyleClass().add("gridPane");
            combosGridPane.getStyleClass().add("gridPane");
            accordion.setExpandedPane(basic);

            Utils.loadTooltip(selectedMapLabel, "Selected map text");
            Utils.loadTooltip(gameTypeLabel, "Game Type text");
            Utils.loadTooltip(difficultyLabel, "Difficulty text");
            Utils.loadTooltip(lengthLabel, "Length text");
            Utils.loadTooltip(serverNameLabel, "Server name text");
            Utils.loadTooltip(serverPasswordLabel, "Server password text");
            Utils.loadTooltip(setupMaps, "Manage maps text");
            Utils.loadTooltip(setupGameTypes, "Manage game types text");
            Utils.loadTooltip(setupDifficulties, "Manage difficulties text");
            Utils.loadTooltip(setupLengths, "Manage lengths text");

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }

            ComboBox<MapDto> mapSelect = (ComboBox<MapDto>)selectedMapLabel.getGraphic();
            mapSelect.setVisibleRowCount((int) Math.round(Screen.getPrimary().getBounds().getHeight() / 241) - 1);

            ExampleMaps exampleMaps = new ExampleMaps();
            mapSelect.getItems().add(exampleMaps.getMapDto1());
            mapSelect.getItems().add(exampleMaps.getMapDto2());
            mapSelect.getItems().add(exampleMaps.getMapDto3());
            mapSelect.getItems().add(exampleMaps.getMapDto4());
            mapSelect.getItems().add(exampleMaps.getMapDto5());
            mapSelect.getItems().add(exampleMaps.getMapDto6());
            mapSelect.getSelectionModel().select(0);

            mapSelect.setCellFactory(new Callback<ListView<MapDto>, ListCell<MapDto>>() {
                @Override
                public ListCell<MapDto> call(ListView<MapDto> mapDtoListView) {

                    ListCell<MapDto> listCell = new ListCell<MapDto>() {
                        @Override
                        protected void updateItem(MapDto mapDto, boolean empty) {
                            super.updateItem(mapDto, empty);
                            if (mapDto != null) {
                                setGraphic(new Kf3MapBox(mapDto,false,false,false).getMapBox());
                                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            }
                        }
                    };
                    return listCell;

                }
            });

            mapSelect.setButtonCell(new ListCell<MapDto>() {
                @Override
                protected void updateItem(MapDto mapDto, boolean empty) {
                    super.updateItem(mapDto, empty);
                    if (mapDto != null) {
                        setGraphic(new Kf3MapBox(mapDto, false, false,false).getMapBox());
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                }
            });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void homeStackPaneOnMouseClicked() throws Exception {
        if (!combosGridPane.isHover() && !logoGridPane.isHover()) {
            Utils.setNextNodeBackground(homeStackPane);
        }
    }

    @FXML
    private void setupMapsOnMouseClicked() {
        Button menuMaps = (Button) Session.getInstance().getTemplate().getNamespace().get("menuMaps");
        Event.fireEvent(menuMaps, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuMaps.getLayoutX(), menuMaps.getLayoutY(), menuMaps.getLayoutX(), menuMaps.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }


    @FXML
    private void setupProfilesOnMouseClicked() {
        Session.getInstance().setSetupType("profiles");
        Button menuSetup = (Button) Session.getInstance().getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupGameTypesOnMouseClicked() {
        Session.getInstance().setSetupType("gameTypes");
        Button menuSetup = (Button) Session.getInstance().getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupDifficultiesOnMouseClicked() {
        Session.getInstance().setSetupType("difficulties");
        Button menuSetup = (Button) Session.getInstance().getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupLengthsOnMouseClicked() {
        Session.getInstance().setSetupType("lengths");
        Button menuSetup = (Button) Session.getInstance().getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    private void loadContent(String fxmlPath) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playVideoOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playVideoOnStartup"));
            if (playVideoOnStartup && Session.getInstance().getVideoPlayer().isAutoPlay()) {
                Session.getInstance().getVideoPlayer().dispose();
            }
            VBox templateContent = (VBox)((ScrollPane) Session.getInstance().getTemplate().getNamespace().get("content")).getContent();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlPath));
            content.setRoot(Session.getInstance().getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
 }
