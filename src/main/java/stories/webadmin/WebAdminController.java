package stories.webadmin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class WebAdminController implements Initializable {

    private static final Logger logger = LogManager.getLogger(WebAdminController.class);

    @FXML private WebView webAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
        WebEngine webEngine = webAdmin.getEngine();

        // Put black color for background of the browser's page
        Field f = webEngine.getClass().getDeclaredField("page");
        f.setAccessible(true);
        com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
        page.setBackgroundColor((new java.awt.Color(0.0f, 0.0f, 0.0f, 1f)).getRGB());

        webEngine.load("https://nodecraft.com/support/games/killing-floor-2/how-to-enable-and-access-your-web-admin-for-your-killing-floor-2-dedicated-server");

        } catch (Exception e) {
            String message = "The WebAdmin page can not be loaded!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
