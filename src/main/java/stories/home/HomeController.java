package stories.home;

import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HomeController.class);
    private final MediaPlayer mediaPlayer;

    public HomeController() {
        super();
        mediaPlayer = new MediaPlayer(new Media(getClass().getClassLoader().getResource("music/kf3-theme.mp4").toExternalForm()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup) {
                mediaPlayer.setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
