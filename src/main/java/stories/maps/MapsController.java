package stories.maps;

import dtos.MapDto;
import enums.EnumSortedMapsCriteria;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ExampleMaps;
import pojos.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapsController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapsController.class);

    @FXML private AnchorPane mapsAnchorPane;
    @FXML private FlowPane steamCustomMapsFlowPane;
    @FXML private FlowPane steamOfficialMapsFlowPane;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Slider columnSlider;
    @FXML private TabPane mapsTabPane;
    @FXML private HBox menuHbox;
    @FXML private CheckBox selectAllSteamCustomMapsCheckBox;
    @FXML private CheckBox selectAllEpicCustomMapsCheckBox;
    @FXML private Tab steamCustomMapsTab;
    @FXML private TextField searchMapsTextField;
    @FXML private VBox mapsVBox;
    @FXML private MenuItem orderMapsByName;
    @FXML private MenuItem orderMapsByReleaseDate;
    @FXML private MenuItem orderMapsByImportedDate;
    @FXML private MenuItem orderMapsByDownloadState;


    private List<VBox> customMapBoxList;
    private List<VBox> officialMapBoxList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            mapsTabPane.setMinHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            mapsTabPane.setMaxHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            Utils.setNodeBackground(mapsAnchorPane);
            menuHbox.getStyleClass().add("hbox");

            PropertyService propertyService = new PropertyServiceImpl();
            boolean playMusicOnStartup = Boolean.parseBoolean(propertyService.getProperty("properties/config.properties", "prop.config.playMusicOnStartup"));
            if (playMusicOnStartup && !Session.getInstance().getMusicPlayer().isAutoPlay()) {
                Session.getInstance().getMusicPlayer().setAutoPlay(true);
            }
            columnSliderOnMouseClicked();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        Session.getInstance().getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (!newSceneWidth.equals(oldSceneWidth)) {
                    columnSliderOnMouseClicked();
                }
            }
        });
    }

     @FXML
    private void mapsStackPaneOnMouseClicked() throws Exception {
        Utils.setNextNodeBackground(mapsAnchorPane);
    }


    @FXML
    private void columnSliderOnMouseClicked() {
        progressIndicator.setVisible(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ExampleMaps exampleMaps = new ExampleMaps();

                if (customMapBoxList == null) {
                    customMapBoxList = new ArrayList<VBox>();
                } else if (!customMapBoxList.isEmpty()) {
                    customMapBoxList.clear();
                }
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto1(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto2(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto3(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto1(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto2(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto3(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto1(), columnSlider.getValue()));
                customMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto2(), columnSlider.getValue()));

                if (officialMapBoxList == null) {
                    officialMapBoxList = new ArrayList<VBox>();
                } else if (!officialMapBoxList.isEmpty()) {
                    officialMapBoxList.clear();
                }
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto4(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto5(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto6(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto4(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto5(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto6(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto4(), columnSlider.getValue()));
                officialMapBoxList.add(createExtendedMapBox(exampleMaps.getMapDto5(), columnSlider.getValue()));

                return null;
            }
        };

        task.setOnSucceeded(wse -> {
            orderMapsByNameOnAction();

            // To force to refresh the screen
            mapsTabPane.setMinHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);
            mapsTabPane.setMaxHeight(Session.getInstance().getPrimaryStage().getHeight() - 180);

            progressIndicator.setVisible(false);
        });

        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void selectAllSteamCustomMapsOnMouseClicked() {
        ObservableList<Node> steamCustomMapBoxList = steamCustomMapsFlowPane.getChildren();
        for (Node mapBoxNode: steamCustomMapBoxList) {
            try {
                Node labelVbox = ((VBox)mapBoxNode).getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
                Node actionsFlowPane = ((VBox)labelVbox).getChildren().stream().filter(flowPane -> "actionsFlowPane".equals(flowPane.getId())).findFirst().orElseGet(null);
                Node selected = ((FlowPane)actionsFlowPane).getChildren().stream().filter(checkbox -> "selected".equals(checkbox.getId())).findFirst().orElseGet(null);
                ((CheckBox)selected).setSelected(selectAllSteamCustomMapsCheckBox.isSelected());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @FXML
    private void selectAllEpicCustomMapsOnMouseClicked() {
    }

    @FXML
    private void searchMapsOnKeyReleased() {
        // Steam Custom Maps
        List<Node> filteredSteamCustomMapBoxList = customMapBoxList.stream().filter(mapBoxNode -> {
            try {
                Node labelVbox = ((VBox)mapBoxNode).getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
                Node mapName = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node downloadedLabel = ((VBox)labelVbox).getChildren().stream().filter(label -> "downloadedLabel".equals(label.getId())).findFirst().orElseGet(null);
                Node mapReleaseDate = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                Node mapImportedDate = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                Node idWorkshop = ((VBox)labelVbox).getChildren().stream().filter(label -> "idWorkshop".equals(label.getId())).findFirst().orElseGet(null);

                String mapNameText = StringUtils.upperCase(((Label)mapName).getText());
                String downloadedText = StringUtils.upperCase(((Label)downloadedLabel).getText());
                String mapReleaseDateText = StringUtils.upperCase(((Label)mapReleaseDate).getText());
                String mapImportedDateText = StringUtils.upperCase(((Label)mapImportedDate).getText());
                String idWorkshopText = StringUtils.upperCase(((Label)idWorkshop).getText());

                if (mapNameText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        downloadedText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        mapReleaseDateText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        mapImportedDateText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        idWorkshopText.contains(StringUtils.upperCase(searchMapsTextField.getText()))) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }).collect(Collectors.toList());

        steamCustomMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().addAll(filteredSteamCustomMapBoxList);

        // Steam Official Maps
        List<Node> filteredSteamOfficialMapBoxList = officialMapBoxList.stream().filter(mapBoxNode -> {
            try {
                Node labelVbox = ((VBox)mapBoxNode).getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
                Node mapName = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node mapReleaseDate = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                Node mapImportedDate = ((VBox)labelVbox).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);

                String mapNameText = StringUtils.upperCase(((Label)mapName).getText());
                String mapReleaseDateText = StringUtils.upperCase(((Label)mapReleaseDate).getText());
                String mapImportedDateText = StringUtils.upperCase(((Label)mapImportedDate).getText());

                if (mapNameText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        mapReleaseDateText.contains(StringUtils.upperCase(searchMapsTextField.getText())) ||
                        mapImportedDateText.contains(StringUtils.upperCase(searchMapsTextField.getText()))) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }).collect(Collectors.toList());

        steamOfficialMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().addAll(filteredSteamOfficialMapBoxList);
    }

    @FXML
    private void moveMenuVerticalOnMouseClicked() {
        List<Node> mapsVBoxBackup = new ArrayList<>(mapsVBox.getChildren());
        mapsVBox.getChildren().clear();
        if (mapsVBoxBackup.get(0).getId().equals("mapsTabPane")) {
            mapsVBox.getChildren().add(mapsVBoxBackup.stream().filter(n -> n.getId().equals("menuHbox")).findFirst().orElseGet(null));
            mapsVBox.getChildren().add(mapsVBoxBackup.stream().filter(n -> n.getId().equals("mapsTabPane")).findFirst().orElseGet(null));
        } else {
            mapsVBox.getChildren().add(mapsVBoxBackup.stream().filter(n -> n.getId().equals("mapsTabPane")).findFirst().orElseGet(null));
            mapsVBox.getChildren().add(mapsVBoxBackup.stream().filter(n -> n.getId().equals("menuHbox")).findFirst().orElseGet(null));
        }
    }

    @FXML
    private void moveMenuHorizontalOnMouseClicked() {
        if (Pos.CENTER.equals(mapsVBox.getAlignment())) {
            mapsVBox.setAlignment(Pos.CENTER_RIGHT);
            return;
        }

        if (Pos.CENTER_RIGHT.equals(mapsVBox.getAlignment())) {
            mapsVBox.setAlignment(Pos.CENTER_LEFT);
            return;
        }

        if (Pos.CENTER_LEFT.equals(mapsVBox.getAlignment())) {
            mapsVBox.setAlignment(Pos.CENTER);
            return;
        }
    }


    @FXML
    private void orderMapsByNameOnAction() {

        if (EnumSortedMapsCriteria.NAME_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            customMapBoxList = customMapBoxList.stream().sorted((vbox1, vbox2) -> {
                Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                return mapName1.getText().compareTo(mapName2.getText());
            }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream().sorted((vbox1, vbox2) -> {
                Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                return mapName1.getText().compareTo(mapName2.getText());
            }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_ASC);
        } else {
            customMapBoxList = customMapBoxList.stream().sorted((vbox1, vbox2) -> {
                Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                return mapName2.getText().compareTo(mapName1.getText());
            }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream().sorted((vbox1, vbox2) -> {
                Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                Label mapName2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                return mapName2.getText().compareTo(mapName1.getText());
            }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.NAME_DESC);
        }

        steamCustomMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().addAll(customMapBoxList);
        steamOfficialMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().addAll(officialMapBoxList);

    }

    @FXML
    private void orderMapsByReleaseDateOnAction() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (EnumSortedMapsCriteria.RELEASE_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText1 = mapReleaseDateLabel1.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate1 = LocalDate.parse(mapReleaseDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText2 = mapReleaseDateLabel2.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate2 = LocalDate.parse(mapReleaseDateText2, formatter);
                        return mapReleaseDate1.compareTo(mapReleaseDate2);
                    }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText1 = mapReleaseDateLabel1.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate1 = LocalDate.parse(mapReleaseDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText2 = mapReleaseDateLabel2.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate2 = LocalDate.parse(mapReleaseDateText2, formatter);
                        return mapReleaseDate1.compareTo(mapReleaseDate2);
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_ASC);
        } else {

            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText1 = mapReleaseDateLabel1.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate1 = LocalDate.parse(mapReleaseDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText2 = mapReleaseDateLabel2.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate2 = LocalDate.parse(mapReleaseDateText2, formatter);
                        return mapReleaseDate2.compareTo(mapReleaseDate1);
                    }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText1 = mapReleaseDateLabel1.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate1 = LocalDate.parse(mapReleaseDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapReleaseDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapReleaseDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapReleaseDateText2 = mapReleaseDateLabel2.getText().replace("Release date: ", "");
                        LocalDate mapReleaseDate2 = LocalDate.parse(mapReleaseDateText2, formatter);
                        return mapReleaseDate2.compareTo(mapReleaseDate1);
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.RELEASE_DATE_DESC);
        }

        steamCustomMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().addAll(customMapBoxList);
        steamOfficialMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().addAll(officialMapBoxList);
    }

    @FXML
    private void orderMapsByImportedDateOnAction() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (EnumSortedMapsCriteria.IMPORTED_DATE_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText1 = mapImportedDateLabel1.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate1 = LocalDateTime.parse(mapImportedDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText2 = mapImportedDateLabel2.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate2 = LocalDateTime.parse(mapImportedDateText2, formatter);
                        return mapImportedDate1.compareTo(mapImportedDate2);
                    }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText1 = mapImportedDateLabel1.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate1 = LocalDateTime.parse(mapImportedDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText2 = mapImportedDateLabel2.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate2 = LocalDateTime.parse(mapImportedDateText2, formatter);
                        return mapImportedDate1.compareTo(mapImportedDate2);
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_ASC);

        } else {

            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText1 = mapImportedDateLabel1.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate1 = LocalDateTime.parse(mapImportedDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText2 = mapImportedDateLabel2.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate2 = LocalDateTime.parse(mapImportedDateText2, formatter);
                        return mapImportedDate2.compareTo(mapImportedDate1);
                    }).collect(Collectors.toList());

            officialMapBoxList = officialMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText1 = mapImportedDateLabel1.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate1 = LocalDateTime.parse(mapImportedDateText1, formatter);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label mapImportedDateLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "mapImportedDate".equals(label.getId())).findFirst().orElseGet(null);
                        String mapImportedDateText2 = mapImportedDateLabel2.getText().replace("Imported date: ", "");
                        LocalDateTime mapImportedDate2 = LocalDateTime.parse(mapImportedDateText2, formatter);
                        return mapImportedDate2.compareTo(mapImportedDate1);
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.IMPORTED_DATE_DESC);
        }

        steamCustomMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().addAll(customMapBoxList);
        steamOfficialMapsFlowPane.getChildren().clear();
        steamOfficialMapsFlowPane.getChildren().addAll(officialMapBoxList);
    }

    @FXML
    private void orderMapsByDownloadStateOnAction() {

        if (EnumSortedMapsCriteria.DOWNLOAD_DESC.equals(Session.getInstance().getSortedMapsCriteria())) {
            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label downloadedLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "downloadedLabel".equals(label.getId())).findFirst().orElseGet(null);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label downloadedLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "downloadedLabel".equals(label.getId())).findFirst().orElseGet(null);
                        return downloadedLabel1.getText().compareTo(downloadedLabel2.getText());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_ASC);
        } else {

            customMapBoxList = customMapBoxList.stream()
                    .sorted((vbox1, vbox2) -> {
                        Node labelVbox1 = vbox1.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label downloadedLabel1 = (Label)((VBox)labelVbox1).getChildren().stream().filter(label -> "downloadedLabel".equals(label.getId())).findFirst().orElseGet(null);
                        Node labelVbox2 = vbox2.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);
                        Label downloadedLabel2 = (Label)((VBox)labelVbox2).getChildren().stream().filter(label -> "downloadedLabel".equals(label.getId())).findFirst().orElseGet(null);
                        return downloadedLabel2.getText().compareTo(downloadedLabel1.getText());
                    }).collect(Collectors.toList());

            Session.getInstance().setSortedMapsCriteria(EnumSortedMapsCriteria.DOWNLOAD_DESC);
        }

        steamCustomMapsFlowPane.getChildren().clear();
        steamCustomMapsFlowPane.getChildren().addAll(customMapBoxList);
    }

    @FXML
    private void mapsDeleteMenuOnAction() {
        List<VBox> selectedCustomMapBoxList = customMapBoxList.stream().filter(vbox -> {
            Node labelVbox = vbox.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
            Node actionsFlowPane = ((VBox)labelVbox).getChildren().stream().filter(label -> "actionsFlowPane".equals(label.getId())).findFirst().orElseGet(null);
            CheckBox selected = (CheckBox)((FlowPane)actionsFlowPane).getChildren().stream().filter(label -> "selected".equals(label.getId())).findFirst().orElseGet(null);
            return selected.isSelected();
        }).collect(Collectors.toList());

        if (selectedCustomMapBoxList.isEmpty()) {
            logger.warn("No selected maps/mods to delete. You must select at least one item to be deleted");
            String headerText = "No selected maps/mods";
            String contentText = "You must select at least one item to do the operation";
            Utils.warningDialog(headerText, contentText);
        } else {
            StringBuffer message = new StringBuffer();
            selectedCustomMapBoxList.forEach(vbox -> {
                Node labelVbox = vbox.getChildren().stream().filter(vBox -> "labelVbox".equals(vBox.getId())).findFirst().orElseGet(null);;
                Label mapName = (Label)((VBox)labelVbox).getChildren().stream().filter(label -> "mapName".equals(label.getId())).findFirst().orElseGet(null);
                message.append(mapName.getText() + "\n");
            });
            String question = "Are you sure that you want to delete next maps/mods?";
            Optional<ButtonType> result = Utils.questionDialog(question, message.toString());
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                customMapBoxList.removeAll(selectedCustomMapBoxList);
                steamCustomMapsFlowPane.getChildren().removeAll(selectedCustomMapBoxList);
            }
        }
    }


    public VBox createExtendedMapBox(MapDto mapDto, double columns) {
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

        if (!mapDto.isOfficial()) {
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

        if (!mapDto.isOfficial()) {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #37603a;");
        } else {
            labelVbox.setStyle("-fx-padding: 5 0 0 0; -fx-background-color: #8c4242;");
        }


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

        VBox mapVbox = new VBox(mapImage, labelVbox);
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
            deteteMapButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String question = "Are you sure that you want to delete selected map / mod?";
                    Optional<ButtonType> result = Utils.questionDialog(question, mapDto.getMapName());
                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        customMapBoxList.remove(mapVbox);
                        steamCustomMapsFlowPane.getChildren().remove(mapVbox);
                    }
                }
            });

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

        mapVbox.setId("mapVbox");
        mapVbox.setMaxWidth(mapImage.getFitWidth());
        mapVbox.setSpacing(10);

        return mapVbox;
    }

}
