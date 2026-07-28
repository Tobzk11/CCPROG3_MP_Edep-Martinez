import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainMenuController {

    // ==========================================
    // 1. FXML UI COMPONENTS
    // ==========================================
    @FXML private TableView<MediaEntry> tableView;
    @FXML private TableColumn<MediaEntry, String> titleColumn;
    @FXML private TableColumn<MediaEntry, String> statusColumn;
    @FXML private TableColumn<MediaEntry, Integer> ratingColumn;
    @FXML private TableColumn<MediaEntry, String> reviewColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;

    @FXML private Button removeButton;
    @FXML private Button updateStatusButton;
    @FXML private Button rateReviewButton;

    @FXML private Spinner<Integer> ratingSpinner; // or whatever your FXML fx:id is
    @FXML private TextArea reviewTextArea;       // or TextField

    // Data backing the table
    private ObservableList<MediaEntry> mediaList = FXCollections.observableArrayList();

    // ==========================================
    // 2. INITIALIZATION
    // ==========================================
    @FXML
    public void initialize() {
        // A. Map table columns to getter methods in MediaEntry (e.g., getTitle())
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("review"));

        // B. Bind list to TableView
        tableView.setItems(mediaList);

        // C. Populate Filter Dropdown
        filterComboBox.getItems().addAll("All", "Watching", "Completed", "Plan to Watch");
        filterComboBox.getSelectionModel().selectFirst();

        // D. Disable selection-dependent buttons when no row is clicked
        var hasSelection = tableView.getSelectionModel().selectedItemProperty().isNotNull();
        removeButton.disableProperty().bind(hasSelection.not());
        updateStatusButton.disableProperty().bind(hasSelection.not());
        rateReviewButton.disableProperty().bind(hasSelection.not());
    }

    // ==========================================
    // 3. EVENT HANDLERS (Matching your VIEW setters)
    // ==========================================

    @FXML
    private void handleAddEntry() {
        // Open modal dialog for adding a new entry
        // (Load AddEntryView.fxml or trigger custom dialog)
    }

    @FXML
    private void handleRemoveEntry() {
        MediaEntry selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Remove \"" + selected.getTitle() + "\"?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mediaList.remove(selected);
            }
        });
    }

    @FXML
    private void handleUpdateStatus() {
        MediaEntry selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Choice Dialog populated directly with Status enum values
        ChoiceDialog<Status> dialog = new ChoiceDialog<>(selected.getStatus(), Status.values());
        dialog.setTitle("Update Status");
        dialog.setHeaderText("Change status for: " + selected.getTitle());
        dialog.setContentText("Select new status:");

        dialog.showAndWait().ifPresent(newStatus -> {
            selected.setStatus(newStatus);
            tableView.refresh(); // Refresh table view to reflect updated status
        });
    }

    @FXML
    private void handleRateAndReview() {
        MediaEntry selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Hard validation check: Must be COMPLETED to rate/review
        if (selected.getStatus() != MediaStatus.COMPLETED) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Action Blocked",
                    "You can only rate and review entries that are marked as 'Completed'!"
            );
            return;
        }

        // Commit any active manual text edits in the spinner before reading
        if (ratingSpinner.isEditable()) {
            ratingSpinner.increment(0);
        }

        // Extract values directly from FXML controls
        int rating = ratingSpinner.getValue();
        String review = reviewTextArea.getText();

        // Update model and refresh UI
        selected.setRating(rating);
        selected.setReview(review);
        tableView.refresh();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            tableView.setItems(mediaList);
            return;
        }

        // Filter list by title
        ObservableList<MediaEntry> filtered = mediaList.filtered(
                item -> item.getTitle().toLowerCase().contains(query.toLowerCase())
        );
        tableView.setItems(filtered);
    }

    @FXML
    private void handleFilter() {
        String selectedCategory = filterComboBox.getValue();
        if ("All".equals(selectedCategory) || selectedCategory == null) {
            tableView.setItems(mediaList);
            return;
        }

        ObservableList<MediaEntry> filtered = mediaList.filtered(
                item -> selectedCategory.equalsIgnoreCase(item.getStatus())
        );
        tableView.setItems(filtered);
    }

    @FXML
    private void handleSave() {
        // Serialize or save 'mediaList' to file/database
        showAlert(Alert.AlertType.INFORMATION, "Save", "Data saved successfully!");
    }

    @FXML
    private void handleLoad() {
        // Load saved data into 'mediaList'
        showAlert(Alert.AlertType.INFORMATION, "Load", "Data loaded successfully!");
    }

    // Helper method for alerts
    private void showAlert(Alert.AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
