package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static FXMLLoader template;

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Simple Killing Floor 3 Server Launcher");
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(700);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(700);

        Font.loadFont(getClass().getClassLoader().getResource("fonts/KillingFont.otf").toExternalForm(), 13);

        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader intro = new FXMLLoader(getClass().getResource("/views/intro.fxml"));
        intro.setRoot(template.getNamespace().get("content"));
        intro.load();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static FXMLLoader getTemplate() {
        return template;
    }
}
