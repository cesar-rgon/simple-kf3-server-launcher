package stories.help;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.awt.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    private static final Logger logger = LogManager.getLogger(HelpController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane github;
    @FXML private VBox helpVbox;
    @FXML private WebView githubWebPage;
    @FXML private WebView documentationWebPage;
    @FXML private WebView releasesWebPage;
    @FXML private WebView donationWebPage;
    @FXML private Label versionLabel;
    @FXML private Hyperlink aboutLink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(helpVbox);
            accordion.setExpandedPane(github);

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
            String applicationVersion = propertyService.getProperty("properties/config.properties", "prop.config.applicationVersion");
            versionLabel.setText("Application version: " + applicationVersion);

            WebEngine githubWebEngine = githubWebPage.getEngine();
            WebEngine documentationWebEngine = documentationWebPage.getEngine();
            WebEngine releasesWebEngine = releasesWebPage.getEngine();
            WebEngine donationWebEngine = donationWebPage.getEngine();

            putBlackBackgroundColor(githubWebEngine);
            putBlackBackgroundColor(documentationWebEngine);
            putBlackBackgroundColor(releasesWebEngine);
            putBlackBackgroundColor(donationWebEngine);

            githubWebEngine.load("https://github.com/cesar-rgon/simple-kf3-server-launcher");
            documentationWebEngine.load("https://github.com/cesar-rgon/simple-kf3-server-launcher/blob/main/README.md");
            releasesWebEngine.load("https://github.com/cesar-rgon/simple-kf3-server-launcher/releases");
            donationWebEngine.load("https://www.paypal.com/paypalme/cesarrgon");

            aboutLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/cesar-rgon"));
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void helpVboxOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(helpVbox);
    }

    private void putBlackBackgroundColor(WebEngine webEngine) throws Exception {
        // Put black color for background of the browser's page
        Field f = webEngine.getClass().getDeclaredField("page");
        f.setAccessible(true);
        com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
        page.setBackgroundColor((new java.awt.Color(0.0f, 0.0f, 0.0f, 1f)).getRGB());
    }
}
