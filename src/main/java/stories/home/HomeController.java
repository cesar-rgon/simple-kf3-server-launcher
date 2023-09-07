package stories.home;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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

    public HomeController() {
        super();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(homeVbox);
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
            if (playMusicOnStartup && !MainApplication.getMusicPlayer().isAutoPlay()) {
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

    @FXML
    private void setupMapsOnMouseClicked() {
        Button menuMaps = (Button) MainApplication.getTemplate().getNamespace().get("menuMaps");
        Event.fireEvent(menuMaps, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuMaps.getLayoutX(), menuMaps.getLayoutY(), menuMaps.getLayoutX(), menuMaps.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupGameTypesOnMouseClicked() {
        Button menuSetup = (Button) MainApplication.getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupDifficultiesOnMouseClicked() {
        Button menuSetup = (Button) MainApplication.getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    @FXML
    private void setupLengthsOnMouseClicked() {
        Button menuSetup = (Button) MainApplication.getTemplate().getNamespace().get("menuSetup");
        Event.fireEvent(menuSetup, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                menuSetup.getLayoutX(), menuSetup.getLayoutY(), menuSetup.getLayoutX(), menuSetup.getLayoutY(), MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }

    private void loadContent(String fxmlPath) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playVideoOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playVideoOnStartup"));
            if (playVideoOnStartup && MainApplication.getVideoPlayer().isAutoPlay()) {
                MainApplication.getVideoPlayer().dispose();
            }
            VBox templateContent = (VBox)((ScrollPane) MainApplication.getTemplate().getNamespace().get("content")).getContent();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlPath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
 }
