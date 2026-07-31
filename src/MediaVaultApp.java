import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX entry point of the MediaVault program. This class assembles the three
 * layers of the MVC design and puts the result on screen: it creates the
 * Library and FileManager that make up the model side, creates the view, and
 * hands both to the controller which wires everything together.
 * <p>
 * The file name given to the FileManager here is only a starting value. Each
 * account has its own save file, so the controller repoints the FileManager at
 * the right file as soon as a user logs in or signs up.
 * </p>
 */
public class MediaVaultApp extends Application {

    /** Window width in pixels. */
    private static final double WINDOW_WIDTH = 1000;

    /** Window height in pixels. */
    private static final double WINDOW_HEIGHT = 650;

    /**
     * Builds the model, view, and controller, then shows the main window.
     * <p>
     * <b>Precondition:</b> primaryStage is supplied by the JavaFX runtime <br>
     * <b>Postcondition:</b> the MediaVault window is showing the login page
     * </p>
     *
     * @param primaryStage the primary stage supplied by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        // 1. Model
        Library library = new Library();
        FileManager fileManager = new FileManager("library_data.txt");

        // 2. View
        MediaVaultView view = new MediaVaultView();

        // 3. Controller wiring
        MediaVaultController controller =
                new MediaVaultController(library, view, fileManager);
        controller.initController();

        // 4. Stage setup
        Scene scene = new Scene(view.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("MediaVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments passed through to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}