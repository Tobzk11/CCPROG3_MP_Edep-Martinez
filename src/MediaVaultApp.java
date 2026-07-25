import javafx.application.Application;

/**
 * Entry point of the MediaVault program. Its only job is to hand control to
 * the JavaFX runtime, which then constructs MediaVaultView and calls its
 * start method. The view is responsible for creating the Library, the
 * FileManager, and the MediaVaultController that ties them together.
 */
public class MediaVaultApp {

    /**
     * Launches the JavaFX application.
     * <p>
     * <b>Postcondition:</b> the JavaFX runtime has started and the MediaVault
     * window is displayed
     * </p>
     *
     * @param args command line arguments passed through to JavaFX
     */
    public static void main(String[] args) {
        Application.launch(MediaVaultView.class, args);
    }
}