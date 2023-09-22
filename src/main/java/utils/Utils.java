package utils;

import dtos.MapDto;
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

    public static VBox createMapBox(MapDto mapDto, boolean showExtended, double columns) {
        // Image
        ImageView mapImage = new ImageView(
                new Image(mapDto.getUrlPhoto())
        );
        mapImage.setPreserveRatio(false);

        double screenWidth = Session.getInstance().getPrimaryStage().getWidth();

        mapImage.setFitWidth((screenWidth - (36 * columns) - 190) / columns);
        mapImage.setFitHeight(mapImage.getFitWidth() / 2);

        // Map namel
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

        if (showExtended && !mapDto.isOfficial()) {
            Label downloadedLabel = null;
            if (mapDto.isDownloaded()) {
                downloadedLabel = new Label("DOWNLOADED");
                downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 5; -fx-border-color: gold; -fx-border-radius: 5; -fx-font-weight: bold; -fx-background-color:#28392a;");
            } else {
                downloadedLabel = new Label("NOT DOWNLOADED");
                downloadedLabel.setStyle("-fx-text-fill: #e8e8e8; -fx-padding: 5; -fx-border-color: #e8e8e8; -fx-border-radius: 5; -fx-font-weight: bold; -fx-background-color:#6e2828;");
            }
            downloadedLabel.setId("downloadedLabel");
            labelVbox.getChildren().add(downloadedLabel);
        }

        if (showExtended) {
            Label mapReleaseDate = new Label("Release date: " + mapDto.getReleaseDate());
            mapReleaseDate.setId("mapReleaseDate");
            mapReleaseDate.setMaxWidth(mapImage.getFitWidth());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Label mapImportedDate = new Label("Imported date: " + mapDto.getImportedDate().format(formatter));
            mapImportedDate.setMaxWidth(mapImage.getFitWidth());
            mapImportedDate.setId("mapImportedDate");

            if (!mapDto.isOfficial()) {
                Label idWorkshop = new Label("id Workshop: " + mapDto.getIdWorkShop());
                idWorkshop.setId("idWorkshop");
                idWorkshop.setMaxWidth(mapImage.getFitWidth());
                idWorkshop.setStyle("-fx-padding: 10 0 0 0;");
                labelVbox.getChildren().add(idWorkshop);
            } else {
                mapReleaseDate.setStyle("-fx-padding: 10 0 0 0;");
            }
            labelVbox.getChildren().add(mapReleaseDate);
            labelVbox.getChildren().add(mapImportedDate);
        }

        if (!mapDto.isOfficial()) {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #37603a;");
        } else {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #8c4242;");
        }

        if (showExtended) {
            // Actions
            ImageView runServerIcon = new ImageView(
                    new Image(Utils.class.getClassLoader().getResourceAsStream("images/run.png"))
            );
            runServerIcon.setPreserveRatio(true);
            runServerIcon.setFitWidth(32);

            Button runServerButton = new Button();
            runServerButton.setGraphic(runServerIcon);

            ImageView informationIcon = new ImageView(
                    new Image(Utils.class.getClassLoader().getResourceAsStream("images/information.png"))
            );
            informationIcon.setPreserveRatio(true);
            informationIcon.setFitWidth(32);

            Button informationButton = new Button();
            informationButton.setGraphic(informationIcon);
            informationButton.setStyle("-fx-padding: 0 10 0 0");

            FlowPane actionsFlowPane = null;
            if (!mapDto.isOfficial()) {
                ImageView editMapIcon = new ImageView(
                        new Image(Utils.class.getClassLoader().getResourceAsStream("images/edit.png"))
                );
                editMapIcon.setPreserveRatio(true);
                editMapIcon.setFitWidth(32);

                Button editMapButton = new Button();
                editMapButton.setGraphic(editMapIcon);

                ImageView deteteMapIcon = new ImageView(
                        new Image(Utils.class.getClassLoader().getResourceAsStream("images/delete.png"))
                );
                deteteMapIcon.setPreserveRatio(true);
                deteteMapIcon.setFitWidth(32);

                Button deteteMapButton = new Button();
                deteteMapButton.setGraphic(deteteMapIcon);
                CheckBox selected = new CheckBox();
                selected.setId("selected");

                actionsFlowPane = new FlowPane(runServerButton, editMapButton, deteteMapButton, informationButton, selected);
            } else {
                actionsFlowPane = new FlowPane(runServerButton, informationButton);
            }
            actionsFlowPane.setId("actionsFlowPane");
            actionsFlowPane.setPadding(new Insets(0,0,5,0));
            actionsFlowPane.setAlignment(Pos.CENTER);
            labelVbox.getChildren().add(actionsFlowPane);
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
}
