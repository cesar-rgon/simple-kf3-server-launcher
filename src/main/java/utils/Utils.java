package utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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

    public static void setNodeBackground(Node node) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        boolean chooseRandomBackgroundImage = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.chooseRandomBackgroundImage"));
        if (chooseRandomBackgroundImage) {
            node.setStyle("-fx-background-image: url('" + chooseRandomBackgroundFileUrl() + "'); -fx-background-size: cover;");
        } else {
            String noRandomBackgroundImage = propertyService.getProperty("properties/config.properties", "prop.config.noRandomBackgroundImage");
            node.setStyle("-fx-background-image: url('" + Objects.requireNonNull(Utils.class.getClassLoader().getResource(noRandomBackgroundImage)).toExternalForm() + "'); -fx-background-size: cover;");
        }
    }

    private static String chooseRandomBackgroundFileUrl() throws Exception {

        File file = new File(System.getProperty("user.dir") + "/images/backgrounds/");
        String backgroundsFolderPath = StringUtils.EMPTY;
        if (file.exists()) {
            backgroundsFolderPath = System.getProperty("user.dir") + "/images/backgrounds/";
        } else {
            backgroundsFolderPath = Objects.requireNonNull(Utils.class.getClassLoader().getResource("images/backgrounds")).getPath().split(":")[1];
        }
        List<String> backgroundPathList = Files.walk(Paths.get(backgroundsFolderPath))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());

        int upperbound = backgroundPathList.size();
        Random rand = new Random();
        int int_random = rand.nextInt(upperbound);
        return "file:" + backgroundPathList.get(int_random).replace("\\","/");
    }

    public static void setNextNodeBackground(Node node) throws Exception {
        String urlActualBackground = node.getStyle().split("'")[1];

        File file = new File(System.getProperty("user.dir") + "/images/backgrounds/");
        String backgroundsFolderPath = StringUtils.EMPTY;
        if (file.exists()) {
            backgroundsFolderPath = System.getProperty("user.dir") + "/images/backgrounds/";
        } else {
            backgroundsFolderPath = Objects.requireNonNull(Utils.class.getClassLoader().getResource("images/backgrounds")).getPath().split(":")[1];
        }

        List<String> backgroundPathList = Files.walk(Paths.get(backgroundsFolderPath))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());

        Optional<String> pathActualBackgroundOptional = backgroundPathList.stream()
                .filter(strPath -> {
                    String url = "file:" + strPath.replace("\\","/");
                    return url.equals(urlActualBackground);
                })
                .findFirst();

        int indexActualBackground = 0;
        if (pathActualBackgroundOptional.isPresent()) {
            indexActualBackground = backgroundPathList.indexOf(pathActualBackgroundOptional.get());
            if (indexActualBackground < backgroundPathList.size()-1) {
                indexActualBackground++;
            } else {
                indexActualBackground = 0;
            }
        }

        node.setStyle("-fx-background-image: url('file:" + backgroundPathList.get(indexActualBackground).replace("\\","/") + "'); -fx-background-size: cover;");
    }

    public static void loadTooltip(Node node, String text) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        double tooltipDuration = Double.parseDouble(
                propertyService.getProperty("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.seconds(0.5));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(node, tooltip);
    }
}
