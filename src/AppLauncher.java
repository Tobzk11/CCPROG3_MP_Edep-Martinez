/**
 * Plain launcher for MediaVault. Starting the program from a class that does
 * not itself extend Application lets it run from a normal classpath build
 * without the JavaFX runtime complaining about missing module components.
 */
public class AppLauncher {

    /**
     * Hands control straight to MediaVaultApp.
     *
     * @param args command line arguments passed through to MediaVaultApp
     */
    public static void main(String[] args) {
        MediaVaultApp.main(args);
    }
}