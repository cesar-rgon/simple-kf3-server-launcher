package pojos;

import javafx.fxml.FXMLLoader;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Session {

    private static Session instance = null;
    private FXMLLoader template;
    private Stage primaryStage;
    private MediaPlayer musicPlayer;
    private MediaPlayer videoPlayer;
    private String setupType;


    private Session() {
        super();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public FXMLLoader getTemplate() {
        return template;
    }

    public void setTemplate(FXMLLoader template) {
        this.template = template;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(MediaPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public MediaPlayer getVideoPlayer() {
        return videoPlayer;
    }

    public void setVideoPlayer(MediaPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    public String getSetupType() {
        return setupType;
    }

    public void setSetupType(String setupType) {
        this.setupType = setupType;
    }
}
