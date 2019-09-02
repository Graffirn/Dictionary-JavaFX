import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.speech.EngineException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("decor.css").toExternalForm());
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("img/dicIcon.png"))));
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    try {
                        Speech.synthesizer.deallocate();
                    } catch (EngineException e) {
                        e.printStackTrace();
                    }
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
