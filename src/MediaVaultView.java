/**
 * Entry point of the MediaVault program. Its only job is to hand control to
 * the JavaFX runtime, which then constructs MediaVaultView and calls its
 * start method. The view is responsible for creating the Library, the
 * FileManager, and the MediaVaultController that ties them together.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class MediaVaultView {

    private final StackPane rootStack; // Container to hold both Login and Main UI

    // --- Login Controls ---
    private final VBox loginPane;
    private final TextField loginUsernameField;
    private final Button enterButton;
    private final Label loginStatusLabel;

    // --- Main App Layout ---
    private final BorderPane mainPane;

    // --- Input Controls (Main App) ---
    private final ComboBox<String> typeComboBox;
    private final TextField titleField;
    private final TextField genreField;
    private final ComboBox<String> statusComboBox;

    // Type-Specific Fields
    private final TextField directorField;
    private final TextField durationField;
    private final TextField releaseYearField;
    private final TextField totalEpisodesField;
    private final TextField watchedEpisodesField;
    private final TextField seasonCountField;
    private final TextField platformField;
    private final TextField requiredSpecsField;
    private final TextField developerField;
    private final TextField hoursPlayedField;

    // Rating & Review Fields
    private final TextField ratingField;
    private final TextArea reviewField;

    // Filter & Search Controls
    private final ComboBox<String> filterTypeComboBox;
    private final ComboBox<String> filterStatusComboBox;
    private final TextField searchField;

    // --- Buttons ---
    private final Button addButton;
    private final Button removeButton;
    private final Button updateStatusButton;
    private final Button rateButton;
    private final Button filterButton;
    private final Button searchButton;
    private final Button saveButton;
    // private final Button loadButton;

    // --- Displays ---
    private final ListView<String> entryListView;
    // private final TextArea detailsArea;
    private final Label messageLabel;
    private final Label statisticsLabel;

    public MediaVaultView() {
        rootStack = new StackPane();

        // login
        loginPane = new VBox(15.0);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        loginPane.setMaxSize(300, 250);

        Label titleLabel = new Label("MediaVault");
        titleLabel.setFont(new Font(20.0));

        loginUsernameField = new TextField();
        loginUsernameField.setMaxWidth(150.0);
        loginUsernameField.setPrefHeight(25.0);
        loginUsernameField.setPromptText("Username");

        enterButton = new Button("Enter");
        enterButton.setMnemonicParsing(false);

        loginStatusLabel = new Label();

        loginPane.getChildren().addAll(titleLabel, loginUsernameField, enterButton, loginStatusLabel);

        // ===================================================================
        // 2. MAIN APP UI
        // ===================================================================
        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setVisible(false);

        // Top Toolbar
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        saveButton = new Button("Save and Exit");
        // loadButton = new Button("Load");
        searchField = new TextField();
        searchField.setPromptText("Search keyword...");
        searchButton = new Button("Search");

        filterTypeComboBox = new ComboBox<>();
        filterTypeComboBox.getItems().addAll("None", "Movie", "TV Series", "Video Game");
        filterTypeComboBox.setValue("None");

        filterStatusComboBox = new ComboBox<>();
        filterStatusComboBox.getItems().addAll("None", "Planned", "In Progress", "Completed");
        filterStatusComboBox.setValue("None");

        filterButton = new Button("Filter");

        topBar.getChildren().addAll(
                saveButton, // loadButton,
                new Separator(),
                searchField, searchButton, new Separator(),
                new Label("Type:"), filterTypeComboBox,
                new Label("Status:"), filterStatusComboBox,
                filterButton
        );
        mainPane.setTop(topBar);

        // Center Lists
        entryListView = new ListView<>();
        /* detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPromptText("Select an entry to view details...");

        SplitPane centerSplit = new SplitPane(entryListView, detailsArea);
        centerSplit.setDividerPositions(0.6); */
        mainPane.setCenter(entryListView);

        // Left Form
        VBox formBox = new VBox(8);
        formBox.setPadding(new Insets(0, 10, 0, 0));
        formBox.setPrefWidth(260);

        Label formTitle = new Label("Entry Management");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        VBox movieBox = new VBox(8);
        movieBox.setPadding(new Insets(0, 10, 0, 0));
        movieBox.setPrefWidth(260);
        Label movieFields = new Label("Movie");
        movieFields.setFont(Font.font("System", FontWeight.BOLD, 10));
        directorField = new TextField();
        directorField.setPromptText("Director");
        durationField = new TextField();
        durationField.setPromptText("Duration (minutes)");
        releaseYearField = new TextField();
        releaseYearField.setPromptText("Release Year");
        movieBox.getChildren().addAll(movieFields, directorField, durationField, releaseYearField);
        setSectionVisible(movieBox, false);

        VBox tvBox = new VBox(8);
        tvBox.setPadding(new Insets(0, 10, 0, 0));
        tvBox.setPrefWidth(260);
        Label tvFields = new Label("TV Series");
        tvFields.setFont(Font.font("System", FontWeight.BOLD, 10));
        totalEpisodesField = new TextField();
        totalEpisodesField.setPromptText("Total Episodes");
        watchedEpisodesField = new TextField();
        watchedEpisodesField.setPromptText("Watched Episodes");
        seasonCountField = new TextField();
        seasonCountField.setPromptText("Seasons");
        tvBox.getChildren().addAll(tvFields, totalEpisodesField, watchedEpisodesField, seasonCountField);
        setSectionVisible(tvBox, false);

        VBox gameBox = new VBox(8);
        gameBox.setPadding(new Insets(0, 10, 0, 0));
        gameBox.setPrefWidth(260);
        Label gameFields = new Label("Video Game");
        gameFields.setFont(Font.font("System", FontWeight.BOLD, 10));
        platformField = new TextField();
        platformField.setPromptText("Platform");
        requiredSpecsField = new TextField();
        requiredSpecsField.setPromptText("Required Specs");
        developerField = new TextField();
        developerField.setPromptText("Developer");
        hoursPlayedField = new TextField();
        hoursPlayedField.setPromptText("Hours Played");
        gameBox.getChildren().addAll(gameFields, platformField, requiredSpecsField, developerField, hoursPlayedField);
        setSectionVisible(gameBox, false);

        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Movie", "TV Series", "Video Game");
        typeComboBox.setPromptText("Select Type");
        typeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            setSectionVisible(movieBox, "Movie".equals(newVal));
            setSectionVisible(tvBox, "TV Series".equals(newVal));
            setSectionVisible(gameBox, "Video Game".equals(newVal));
        });

        VBox entryBox = new VBox();
        entryBox.setPadding(new Insets(0, 0, 0, 0));
        entryBox.setPrefWidth(260);
        entryBox.getChildren().addAll(typeComboBox, movieBox, tvBox, gameBox);

        titleField = new TextField();
        titleField.setPromptText("Title");
        genreField = new TextField();
        genreField.setPromptText("Genre");

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Planned", "In Progress", "Completed");
        statusComboBox.setPromptText("Select Status");

        addButton = new Button("Add Entry");
        removeButton = new Button("Remove Selected");
        updateStatusButton = new Button("Update Status");

        formBox.getChildren().addAll(
                formTitle, titleField, genreField, statusComboBox, new Separator(), entryBox,
                addButton, removeButton, updateStatusButton
        );

        ScrollPane formScroll = new ScrollPane(formBox);
        formScroll.setFitToWidth(true);
        mainPane.setLeft(formScroll);

        VBox rateBox = new VBox();
        rateBox.setPadding(new Insets(10, 10, 10, 10));
        rateBox.setPrefWidth(260);
        rateBox.setSpacing(20);
        Label ratingLabel = new Label("Rating (1 - 10)");
        ratingLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        mainPane.setLeft(formScroll);
        ratingField = new TextField();
        Label reviewLabel = new Label("Review");
        reviewLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        reviewField = new TextArea();
        reviewField.setPrefRowCount(3);
        rateButton = new Button("Rate & Review");

        rateBox.getChildren().addAll(ratingLabel, ratingField, reviewLabel, reviewField, rateButton);

        mainPane.setRight(rateBox);

        // Bottom Status
        VBox bottomBox = new VBox(5);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));
        messageLabel = new Label("Ready.");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statisticsLabel = new Label("Total Entries: 0");

        bottomBox.getChildren().addAll(new Separator(), messageLabel, statisticsLabel);
        mainPane.setBottom(bottomBox);

        // Add both views to stack root
        rootStack.getChildren().addAll(loginPane, mainPane);
    }

    private void setSectionVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    public StackPane getRoot() {
        return rootStack;
    }

    // --- Login Handlers & Getters ---
    public void setEnterHandler(EventHandler<ActionEvent> handler) {
        enterButton.setOnAction(handler);
    }

    public String getLoginUsername() {
        return loginUsernameField.getText();
    }

    public void setLoginStatus(String text) {
        loginStatusLabel.setText(text);
    }

    public void switchToLogin() {
        loginPane.setVisible(true);
        mainPane.setVisible(false);
    }

    public void switchToMainApp() {
        loginPane.setVisible(false);
        mainPane.setVisible(true);
    }

    // --- Main Handlers ---
    public void setAddEntryHandler(EventHandler<ActionEvent> handler) { addButton.setOnAction(handler); }
    public void setRemoveEntryHandler(EventHandler<ActionEvent> handler) { removeButton.setOnAction(handler); }
    public void setUpdateStatusHandler(EventHandler<ActionEvent> handler) { updateStatusButton.setOnAction(handler); }
    public void setRateAndReviewHandler(EventHandler<ActionEvent> handler) { rateButton.setOnAction(handler); }
    public void setFilterHandler(EventHandler<ActionEvent> handler) { filterButton.setOnAction(handler); }
    public void setSearchHandler(EventHandler<ActionEvent> handler) { searchButton.setOnAction(handler); }
    public void setSaveHandler(EventHandler<ActionEvent> handler) { saveButton.setOnAction(handler); }
    // public void setLoadHandler(EventHandler<ActionEvent> handler) { loadButton.setOnAction(handler); }

    // --- Getters ---
    public String getSelectedType() { return typeComboBox.getValue(); }
    public String getTitleInput() { return titleField.getText(); }
    public String getGenreInput() { return genreField.getText(); }
    public String getSelectedStatus() { return statusComboBox.getValue(); }

    public String getDirectorInput() { return directorField.getText(); }
    public String getDurationInput() { return durationField.getText(); }
    public String getReleaseYearInput() { return releaseYearField.getText(); }

    public String getTotalEpisodesInput() { return totalEpisodesField.getText(); }
    public String getWatchedEpisodesInput() { return watchedEpisodesField.getText(); }
    public String getSeasonCountInput() { return seasonCountField.getText(); }

    public String getPlatformInput() { return platformField.getText(); }
    public String getRequiredSpecsInput() { return requiredSpecsField.getText(); }
    public String getDeveloperInput() { return developerField.getText(); }
    public String getHoursPlayedInput() { return hoursPlayedField.getText(); }

    public String getRatingInput() { return ratingField.getText(); }
    public String getReviewInput() { return reviewField.getText(); }

    public String getFilterType() { return filterTypeComboBox.getValue(); }
    public String getFilterStatus() { return filterStatusComboBox.getValue(); }
    public String getSearchKeyword() { return searchField.getText(); }

    public String getSelectedEntryTitle() {
        String selected = entryListView.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isEmpty()) return "";
        int closingBracket = selected.indexOf(']');
        int dashIndex = selected.indexOf(" - ");
        if (closingBracket != -1 && dashIndex != -1 && dashIndex > closingBracket) {
            return selected.substring(closingBracket + 2, dashIndex).trim();
        }
        return selected;
    }

    public void displayEntries(ArrayList<String> entries) { entryListView.getItems().setAll(entries); }
    // public void showEntryDetails(String details) { detailsArea.setText(details); }
    public void showMessage(String message) { messageLabel.setText(message); }
    public void showStatistics(String summary) { statisticsLabel.setText(summary); }

    public void clearInputFields() {
        titleField.clear(); genreField.clear(); directorField.clear();
        durationField.clear(); releaseYearField.clear(); totalEpisodesField.clear();
        watchedEpisodesField.clear(); seasonCountField.clear(); platformField.clear();
        requiredSpecsField.clear(); developerField.clear(); hoursPlayedField.clear();
        ratingField.clear(); reviewField.clear();
    }
}
