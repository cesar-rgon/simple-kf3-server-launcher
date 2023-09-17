package nodes;

import dtos.MapDto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Kf3MapBox {

    private final VBox mapBox;

    public Kf3MapBox(MapDto mapDto) {
        this(mapDto, true, true, true);
    }

    public Kf3MapBox(MapDto mapDto, boolean showDescription, boolean showActions, boolean showDownloaded) {
        super();
        ImageView mapImage = new ImageView(
                new Image(mapDto.getUrlPhoto())
        );
        mapImage.setPreserveRatio(false);
        mapImage.setFitWidth(340);
        mapImage.setFitHeight(170);

        Label downloadedLabel = null;
        if (mapDto.isDownloaded()) {
            downloadedLabel = new Label("DOWNLOADED");
            downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 5; -fx-border-color: gold; -fx-border-radius: 5; -fx-background-color: #242323;");
        } else {
            downloadedLabel = new Label("NOT DOWNLOADED");
            downloadedLabel.setStyle("-fx-text-fill: #ff7777; -fx-padding: 5; -fx-border-color: #ff7777; -fx-border-radius: 5; -fx-background-color: #242323;");
        }

        StackPane imagePane = null;
        if (showDownloaded) {
            imagePane = new StackPane(mapImage, downloadedLabel);
        } else {
            imagePane = new StackPane(mapImage);
        }
        imagePane.setAlignment(Pos.TOP_RIGHT);
        StackPane.setMargin(downloadedLabel, new Insets(5, 5, 0, 0));

        ImageView runServerIcon = new ImageView(
                new Image(getClass().getClassLoader().getResourceAsStream("images/run.png"))
        );
        runServerIcon.setPreserveRatio(true);
        runServerIcon.setFitWidth(32);

        Button runServerButton = new Button();
        runServerButton.setGraphic(runServerIcon);

        ImageView informationIcon = new ImageView(
                new Image(getClass().getClassLoader().getResourceAsStream("images/information.png"))
        );
        informationIcon.setPreserveRatio(true);
        informationIcon.setFitWidth(32);

        Button informationButton = new Button();
        informationButton.setGraphic(informationIcon);

        Label mapName = new Label(mapDto.getMapName());
        mapName.setMaxWidth(mapImage.getFitWidth());
        mapName.setId("mapName");

        Label mapReleaseDate = new Label("Release date: " + mapDto.getReleaseDate());
        mapReleaseDate.setMaxWidth(mapImage.getFitWidth());

        Label mapImportedDate = new Label("Imported date: " + mapDto.getImportedDate());
        mapImportedDate.setMaxWidth(mapImage.getFitWidth());
        mapImportedDate.setId("mapImportedDate");

        VBox labelVbox = null;
        if (!mapDto.isOfficial()) {
            Label idWorkshop = new Label("id Workshop: " + mapDto.getIdWorkShop());
            idWorkshop.setMaxWidth(mapImage.getFitWidth());
            labelVbox = new VBox(mapName, idWorkshop, mapReleaseDate, mapImportedDate);
            labelVbox.setStyle("-fx-background-color: #37603a;");
        } else {
            labelVbox = new VBox(mapName, mapReleaseDate, mapImportedDate);
            labelVbox.setStyle("-fx-background-color: #8c4242;");
        }
        labelVbox.setId("labelVbox");

        HBox actionsHbox = null;
        if (!mapDto.isOfficial()) {
            ImageView editMapIcon = new ImageView(
                    new Image(getClass().getClassLoader().getResourceAsStream("images/edit.png"))
            );
            editMapIcon.setPreserveRatio(true);
            editMapIcon.setFitWidth(32);

            Button editMapButton = new Button();
            editMapButton.setGraphic(editMapIcon);

            ImageView deteteMapIcon = new ImageView(
                    new Image(getClass().getClassLoader().getResourceAsStream("images/delete.png"))
            );
            deteteMapIcon.setPreserveRatio(true);
            deteteMapIcon.setFitWidth(32);

            Button deteteMapButton = new Button();
            deteteMapButton.setGraphic(deteteMapIcon);

            actionsHbox = new HBox(runServerButton, editMapButton, deteteMapButton, informationButton);
        } else {
            actionsHbox = new HBox(runServerButton, informationButton);
        }
        actionsHbox.setPadding(new Insets(0,0,5,0));
        actionsHbox.setAlignment(Pos.CENTER);
        actionsHbox.setSpacing(20);

        TextArea mapDescription = new TextArea("Description: " + mapDto.getMapDescription());
        mapDescription.setWrapText(true);
        mapDescription.setEditable(false);
        mapDescription.setPrefHeight(120);

        VBox mapVbox = null;
        if (showDescription && showActions) {
            mapVbox = new VBox(imagePane, labelVbox, mapDescription, actionsHbox);
        } else if (!showDescription && !showActions) {
            mapVbox = new VBox(imagePane, labelVbox);
        } else if (showDescription) {
            mapVbox = new VBox(imagePane, labelVbox, mapDescription);
        } else {
            mapVbox = new VBox(imagePane, labelVbox, actionsHbox);
        }
        mapVbox.setId("mapVbox");
        mapVbox.setMaxWidth(340);
        mapVbox.setSpacing(10);

        this.mapBox = mapVbox;
    }

    public VBox getMapBox() {
        return mapBox;
    }

}
