package pojos;

import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ExampleMapBoxes {

    private List<VBox> customMapBoxList;
    private List<VBox> officialMapBoxList;

    public ExampleMapBoxes() {
        super();
        customMapBoxList = new ArrayList<>();
        officialMapBoxList = new ArrayList<>();
    }

    public List<VBox> getCustomMapBoxList() {
        return customMapBoxList;
    }

    public void setCustomMapBoxList(List<VBox> customMapBoxList) {
        this.customMapBoxList = customMapBoxList;
    }

    public List<VBox> getOfficialMapBoxList() {
        return officialMapBoxList;
    }

    public void setOfficialMapBoxList(List<VBox> officialMapBoxList) {
        this.officialMapBoxList = officialMapBoxList;
    }
}
