import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Entry point of the MediaVault program. Its only job is to hand control to
 * the JavaFX runtime, which then constructs MediaVaultView and calls its
 * start method. The view is responsible for creating the Library, the
 * FileManager, and the MediaVaultController that ties them together.
 */
public class MediaVaultApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MediaVaultView view = new MediaVaultView();
        new MediaVaultController(view);

        Scene scene = new Scene(view);
        primaryStage.setTitle("MediaVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}