import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MediaVaultApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Model Instantiation
        Library library = new Library();
        FileManager fileManager = new FileManager("library_data.txt");

        // 2. View Instantiation
        MediaVaultView view = new MediaVaultView();

        // 3. Controller Wiring
        MediaVaultController controller = new MediaVaultController(library, view, fileManager);
        controller.initController();

        // 4. Stage Setup
        Scene scene = new Scene(view.getRoot(), 1000, 650);
        primaryStage.setTitle("MediaVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
