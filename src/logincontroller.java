import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class logincontroller {

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();

        // 1. Logic Check (Example: Username must not be empty and at least 3 characters)
        if (username != null && username.trim().length() >= 3) {
            // Valid entry -> Switch to Main Menu View
            switchToMenuView(event);
        } else {
            // Invalid entry -> Show error message
            errorLabel.setText("Invalid username! Must be at least 3 characters.");
        }
    }

    private void switchToMenuView(ActionEvent event) {
        try {
            // Load the new FXML layout for the main menu
            Parent menuRoot = FXMLLoader.load(getClass().getResource("menuview.fxml"));

            // Get the current window (Stage) from the button click event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(menuRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load main menu.");
        }
    }
}
