package stories.intro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    private static final Logger logger = LogManager.getLogger(IntroController.class);

    @FXML private MediaView introVideo;

    public IntroController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playVideoOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playVideoOnStartup"));
            if (playVideoOnStartup) {
                MediaPlayer videoPlayer = introVideo.getMediaPlayer();
                videoPlayer.setOnEndOfMedia(this::loadHomeContent);
                videoPlayer.setOnError(this::loadHomeContent);
                videoPlayer.play();
            } else {
                loadHomeContent();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    private void loadHomeContent() {
        try {
            introVideo.getMediaPlayer().dispose();
            VBox templateContent = (VBox)((ScrollPane)MainApplication.getTemplate().getNamespace().get("content")).getContent();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void onMouseClickedStopPlaying() {
        introVideo.getMediaPlayer().stop();
        loadHomeContent();
    }
}
