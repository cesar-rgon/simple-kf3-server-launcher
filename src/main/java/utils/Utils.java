package utils;

import dtos.MapDto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
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
        if (!file.exists()) {
            file = new File(Utils.class.getClassLoader().getResource("images/backgrounds").getPath());
        }

        List<URI> backgroundUriList = Files.walk(file.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toUri)
                .collect(Collectors.toList());

        int upperbound = backgroundUriList.size();
        Random rand = new Random();
        int int_random = rand.nextInt(upperbound);
        return backgroundUriList.get(int_random).toString();
    }

    public static void setNextNodeBackground(Node node) throws Exception {
        String urlActualBackground = node.getStyle().split("'")[1];

        File file = new File(System.getProperty("user.dir") + "/images/backgrounds/");
        if (!file.exists()) {
            file = new File(Utils.class.getClassLoader().getResource("images/backgrounds").getPath());
        }

        List<URI> backgroundUriList = Files.walk(file.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toUri)
                .collect(Collectors.toList());

        Optional<URI> uriActualBackgroundOptional = backgroundUriList.stream()
                .filter(uri -> {
                    return uri.toString().equals(urlActualBackground);
                })
                .findFirst();

        int indexActualBackground = 0;
        if (uriActualBackgroundOptional.isPresent()) {
            indexActualBackground = backgroundUriList.indexOf(uriActualBackgroundOptional.get());
            if (indexActualBackground < backgroundUriList.size()-1) {
                indexActualBackground++;
            } else {
                indexActualBackground = 0;
            }
        }

        node.setStyle("-fx-background-image: url('" + backgroundUriList.get(indexActualBackground) + "'); -fx-background-size: cover;");
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


    public static VBox createReducedMapBox(MapDto mapDto) {

        ImageView mapImage = new ImageView(
                new Image(mapDto.getUrlPhoto())
        );
        mapImage.setPreserveRatio(false);
        mapImage.setFitWidth(340);
        mapImage.setFitHeight(mapImage.getFitWidth() / 2);

        Label mapName = new Label(mapDto.getMapName());
        mapName.setMaxWidth(mapImage.getFitWidth());
        mapName.setId("mapName");
        if (!mapDto.isOfficial()) {
            mapName.setStyle("-fx-background-color: #37603a;");
        } else {
            mapName.setStyle("-fx-background-color: #8c4242;");
        }

        VBox labelVbox = new VBox(mapName);
        labelVbox.setId("labelVbox");
        labelVbox.setAlignment(Pos.CENTER);

        if (!mapDto.isOfficial()) {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #37603a;");
        } else {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #8c4242;");
        }

        VBox mapVbox = new VBox(mapImage, labelVbox);
        mapVbox.setId("mapVbox");
        mapVbox.setMaxWidth(mapImage.getFitWidth());
        mapVbox.setSpacing(10);

        return mapVbox;
    }


    public static void warningDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getProperty("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(header);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public static Optional<ButtonType> questionDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getProperty("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(header);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        return alert.showAndWait();
    }

    private void deleteVBox() {

    }
}
