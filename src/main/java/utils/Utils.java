package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

    public static void errorDialog(String headerText, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String applicationTitle = StringUtils.EMPTY;
        String[] minimumResolution = {};
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            applicationTitle = propertyService.getProperty("properties/config.properties", "prop.config.applicationTitle");
            minimumResolution = propertyService.getProperty("properties/config.properties", "prop.config.errorDialogResolution.minimum").split("x");
        } catch (Exception ex) {
            minimumResolution[0] = "600";
            minimumResolution[1] = "400";
        }

        alert.setTitle(applicationTitle);
        alert.setHeaderText(headerText);

        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setContent(textArea);
            alert.getDialogPane().setMinWidth(Double.parseDouble(minimumResolution[0]));
            alert.getDialogPane().setMinHeight(Double.parseDouble(minimumResolution[1]));
        }
        alert.setResizable(true);
        alert.showAndWait();
    }

}
