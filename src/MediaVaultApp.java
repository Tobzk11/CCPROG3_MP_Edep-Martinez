import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainview.fxml"));

        stage.setScene(new Scene(loader.load()));
        stage.setTitle("MediaVault");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}