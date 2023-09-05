package start;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

public class MainApplication extends Application {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);
    private static FXMLLoader template;
    private static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getClassLoader().getResource("fonts/KillingFont.otf").toExternalForm(), 13);
        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader intro = new FXMLLoader(getClass().getResource("/views/intro.fxml"));
        intro.setRoot(template.getNamespace().get("content"));
        intro.load();

        PropertyService propertyService = new PropertyServiceImpl();
        String applicationTitle = propertyService.getProperty("properties/config.properties", "prop.config.applicationTitle");
        String applicationVersion = propertyService.getProperty("properties/config.properties", "prop.config.applicationVersion");
        String[] minimumResolution = propertyService.getProperty("properties/config.properties", "prop.config.applicationResolution.minimum").split("x");
        String[] actualResolution = propertyService.getProperty("properties/config.properties", "prop.config.applicationResolution.lastModified").split("x");
        boolean applicationMaximized = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.applicationMaximized"));

        primaryStage.setTitle(applicationTitle + " " + applicationVersion);
        primaryStage.setMinWidth(Double.parseDouble(minimumResolution[0]));
        primaryStage.setMinHeight(Double.parseDouble(minimumResolution[1]));
        primaryStage.setWidth(Double.parseDouble(actualResolution[0]));
        primaryStage.setHeight(Double.parseDouble(actualResolution[1]));
        primaryStage.setMaximized(applicationMaximized);
        primaryStage.setScene(scene);
        primaryStage.show();
        MainApplication.primaryStage = primaryStage;

        primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                String isMaximized = String.valueOf(t1.booleanValue());
                try {
                    propertyService.setProperty("properties/config.properties", "prop.config.applicationMaximized", isMaximized);
                } catch (Exception e) {
                    String message = "Error setting maximized value in config.properties file";
                    logger.error(message, e);
                    Utils.errorDialog(message, e);
                }
            }
        });
    }

    @Override
    public void stop() throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.applicationResolution.lastModified", primaryStage.getWidth() + "x" + primaryStage.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static FXMLLoader getTemplate() {
        return template;
    }
}
