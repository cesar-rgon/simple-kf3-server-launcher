package stories.intro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import start.MainApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable {

    @FXML
    private MediaView introVideo;

    public IntroController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MediaPlayer mediaPlayer = introVideo.getMediaPlayer();
        mediaPlayer.setOnEndOfMedia(this::loadHomeContent);
        mediaPlayer.setOnError(this::loadHomeContent);
    }

    private void loadHomeContent() {
        try {
            VBox templateContent = (VBox)((ScrollPane)MainApplication.getTemplate().getNamespace().get("content")).getContent();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            // Utils.errorDialog(e.getMessage(), e);
        }
    }
}
