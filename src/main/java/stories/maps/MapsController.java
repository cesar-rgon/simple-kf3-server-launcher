package stories.maps;

import dtos.MapDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MapsController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsController.class);

    @FXML private Accordion accordion;
    @FXML private TitledPane steamCustomMapsTitledPane;
    @FXML private StackPane mapsStackPane;
    @FXML private FlowPane steamCustomMaps;
    @FXML private FlowPane steamOfficialMaps;
    @FXML private FlowPane epicCustomMaps;
    @FXML private FlowPane epicOfficialMaps;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Utils.setNodeBackground(mapsStackPane);
            accordion.setExpandedPane(steamCustomMapsTitledPane);

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        // Example of map
        MapDto mapDto1 = new MapDto(
                "First map",
                "https://steamuserimages-a.akamaihd.net/ugc/939436531242760934/7DD78A5049EA3DD0BF48963EA11FCE12E629178E/?imw=268&imh=268&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true",
                LocalDate.of(2018,3,9),
                LocalDateTime.now(),
                "KF-Ashes\n" +
                        "\n" +
                        "credits:\n" +
                        "silent hill konami\n" +
                        "killing floor\n" +
                        "monolith\n" +
                        "\n" +
                        "just stay away from dark alissa..\n" +
                        "havent told you everything,play the map to find out more :)\n" +
                        "\n" +
                        "Update:20/10/18\n" +
                        "added Endless mode Support in array\n" +
                        "new trader kismet sequences(to suit endless mode)\n" +
                        "fixed trader radious\n" +
                        "added trigger on piano @ cathedral traders\n" +
                        "blocking volumes\n" +
                        "fixed expliots\n" +
                        "fixed blood splatts and resolutions\n" +
                        "added triggers ,fixed navigation,kfpathnodes\n" +
                        "fixed physics assets on models\n" +
                        "fixed materails\n" +
                        "rendered some meshes for better optimization\n" +
                        "added random player starts\n" +
                        "\n" +
                        "\n" +
                        "working clock with hands\n" +
                        "5 traders\n" +
                        "events\n" +
                        "custom models\n" +
                        "custom particles and effects\n" +
                        "ashe's and snow shockwave\n" +
                        "custom sounds\n" +
                        "ambiance\n" +
                        "collectabiles\n" +
                        "portals\n" +
                        "teleporters(to traders)\n" +
                        "own boss spawn point\n" +
                        "end credits\n" +
                        "\n" +
                        "original authors and map can be found here:\n" +
                        "http://steamcommunity.com/sharedfiles/filedetails/?\n" +
                        "\n" +
                        "id=302202036&searchtext=ashes\n" +
                        "http://steamcommunity.com/id/Seigneurrat/\n" +
                        "http://steamcommunity.com/id/DannyManPc/\n" +
                        "\n" +
                        "\n" +
                        "good luck and ty for your support",
                false,
                "KF-Ashes",
                false,
                       12345678L
        );
        MapDto mapDto2 = new MapDto(
                "Second map",
                "https://steamuserimages-a.akamaihd.net/ugc/495766099594954649/4531DB07AAD3E141EED990144A4E5D09288A903A/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false",
                LocalDate.of(2016,3,12),
                LocalDateTime.now(),
                "Rev up those fryers!\n" +
                        "\n" +
                        "Zeds have taken over the city beneath Bikini Atoll and only you and your underwater fish friends can stop them!\n",
                true,
                "KF-BikiniAtoll",
                false,
                23456789L
        );
        MapDto mapDto3 = new MapDto(
                "Third map",
                "https://steamuserimages-a.akamaihd.net/ugc/910157763642009480/3438560E01B662EE605AA25ECE9BAF542C142BBF/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false",
                LocalDate.of(2016,7,28),
                LocalDateTime.now(),
                "img src=\"https://i.imgur.com/zBxs9vb.jpg\" /><br><a class=\"bb_link\" href=\"http://steamcommunity.com/workshop/filedetails/?id=743776156\" target=\"_blank\" rel=\"\" ><img src=\"https://i.imgur.com/TD9Ii7N.jpg\" /></a><br><img src=\"https://i.imgur.com/RYcQ6Nv.jpg\" /><br><b><u>Note:</u></b><br>The Forum link will remain dead while I am in the process of consolidating all my maps into a single mega thread.",
                false,
                "KF-West London",
                false,
                34567890L
        );


        steamCustomMaps.getChildren().add(createMapBox(mapDto1));
        steamCustomMaps.getChildren().add(createMapBox(mapDto2));
        steamCustomMaps.getChildren().add(createMapBox(mapDto3));
    }

    private VBox createMapBox(MapDto mapDto) {

        ImageView mapImage = new ImageView(
            new Image(mapDto.getUrlPhoto())
        );
        mapImage.setPreserveRatio(false);
        mapImage.setFitWidth(300);
        mapImage.setFitHeight(150);

        Label downloadedLabel = null;
        if (mapDto.isDownloaded()) {
            downloadedLabel = new Label("DOWNLOADED");
            downloadedLabel.setStyle("-fx-text-fill: gold; -fx-padding: 5; -fx-border-color: gold; -fx-border-radius: 5; -fx-background-color: #242323;");
        } else {
            downloadedLabel = new Label("NOT DOWNLOADED");
            downloadedLabel.setStyle("-fx-text-fill: #ff7777; -fx-padding: 5; -fx-border-color: #ff7777; -fx-border-radius: 5; -fx-background-color: #242323;");
        }

        StackPane imagePane = new StackPane(mapImage, downloadedLabel);
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

        HBox actionsHbox = new HBox(runServerButton, editMapButton, deteteMapButton, informationButton);
        actionsHbox.setPadding(new Insets(0,0,5,0));
        actionsHbox.setAlignment(Pos.CENTER);
        actionsHbox.setSpacing(20);

        Label mapName = new Label(mapDto.getMapName());
        mapName.setMaxWidth(mapImage.getFitWidth());
        mapName.setId("mapName");

        Label idWorkshop = new Label("id Workshop: " + mapDto.getIdWorkShop());
        idWorkshop.setMaxWidth(mapImage.getFitWidth());

        Label mapReleaseDate = new Label("Release date: " + mapDto.getReleaseDate());
        mapReleaseDate.setMaxWidth(mapImage.getFitWidth());

        Label mapImportedDate = new Label("Imported date: " + mapDto.getImportedDate());
        mapImportedDate.setMaxWidth(mapImage.getFitWidth());
        mapImportedDate.setId("mapImportedDate");

        VBox labelVbox = new VBox(mapName, idWorkshop, mapReleaseDate, mapImportedDate);
        labelVbox.setId("labelVbox");

        TextArea mapDescription = new TextArea("Description: " + mapDto.getMapDescription());
        mapDescription.setWrapText(true);
        mapDescription.setEditable(false);
        mapDescription.setPrefHeight(120);

        VBox mapVbox = new VBox(imagePane, labelVbox, mapDescription, actionsHbox);
        mapVbox.setId("mapVbox");
        mapVbox.setPrefWidth(mapImage.getFitWidth());
        mapVbox.setSpacing(10);

        return mapVbox;
    }

    @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsStackPane);
    }
}
