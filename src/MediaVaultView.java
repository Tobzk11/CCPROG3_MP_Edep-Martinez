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
import java.util.function.Consumer;

/**
 * The JavaFX view for MediaVault. This is the presentation layer of the MVC
 * design: it owns every on-screen control and is the only class that imports
 * JavaFX. It never touches Library, MediaEntry, Movie, TVSeries, VideoGame,
 * FileManager, or MediaStatus directly. Everything it shows arrives as a
 * String from the controller, and everything the user types leaves as a String
 * through a getter.
 * <p>
 * The view holds three pages stacked on top of one another: a login page, a
 * sign-up page, and the main library page. Only one is visible at a time, and
 * the controller switches between them through loginPage, signupPage,
 * mainPage, and swapFront.
 * </p>
 * <p>
 * The view is not the JavaFX Application class. MediaVaultApp launches the
 * program, builds this view, and asks it for its root node through getRoot.
 * </p>
 */

public class MediaVaultView {

    private final StackPane rootStack;

    // login
    private final VBox loginPane;
    private final TextField loginUsernameField;
    private final Button enterButton;
    private final Label signupLabel;
    private final Button signupButton;
    private final Label loginStatusLabel;

    private final VBox signupPane;
    private final TextField signupUsernameField;
    private final Button signupEnterButton;
    private final Label loginLabel;
    private final Button loginButton;
    private final Label signupStatusLabel;

    private final BorderPane mainPane;

    // main
    private final ComboBox<String> typeComboBox;
    private final TextField titleField;
    private final TextField genreField;
    private final ComboBox<String> statusComboBox;

    // movie
    private final TextField directorField;
    private final TextField durationField;
    private final TextField releaseYearField;

    // tv
    private final TextField totalEpisodesField;
    private final TextField watchedEpisodesField;
    private final TextField seasonCountField;

    // video game
    private final TextField platformField;
    private final TextField requiredSpecsField;
    private final TextField developerField;
    private final TextField hoursPlayedField;

    // rate
    private final TextField ratingField;
    private final TextArea reviewField;

    // filters
    private final ComboBox<String> filterTypeComboBox;
    private final ComboBox<String> filterStatusComboBox;
    private final TextField searchField;

    private final Button addButton;
    private final Button removeButton;
    private final Button updateStatusButton;
    private final Button rateButton;
    private final Button filterButton;
    private final Button searchButton;
    private final Button saveButton;
    private final Button loadButton;

    // display
    private final ListView<String> entryListView;
    private final TextArea detailsArea;
    private final Label messageLabel;
    private final Label statisticsLabel;

    /**
     * Builds every control the interface needs and arranges them into the
     * three stacked pages. No handlers are attached here; the controller
     * registers those afterwards through the setter methods.
     * <p>
     * <b>Postcondition:</b> all controls exist and the login page is the only
     * visible page
     * </p>
     */
    public MediaVaultView() {
        rootStack = new StackPane();

        // login
        loginPane = new VBox(15.0);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        loginPane.setMaxSize(300, 250);

        Label titleLabel = new Label("MediaVault Login");
        titleLabel.setFont(new Font(20.0));

        loginUsernameField = new TextField();
        loginUsernameField.setMaxWidth(150.0);
        loginUsernameField.setPrefHeight(25.0);
        loginUsernameField.setPromptText("Username");

        enterButton = new Button("Enter");
        enterButton.setMnemonicParsing(false);

        signupLabel = new Label("Click here to make a new account: ");

        signupButton = new Button("Sign up");

        loginStatusLabel = new Label();

        loginPane.getChildren().addAll(titleLabel, loginUsernameField, enterButton, loginStatusLabel, signupLabel, signupButton);

        // sign up
        signupPane = new VBox(15.0);
        signupPane.setAlignment(Pos.CENTER);
        signupPane.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        signupPane.setMaxSize(300, 250);

        Label signupTitleLabel = new Label("MediaVault Sign Up");
        signupTitleLabel.setFont(new Font(20.0));

        signupUsernameField = new TextField();
        signupUsernameField.setMaxWidth(150.0);
        signupUsernameField.setPrefHeight(25.0);
        signupUsernameField.setPromptText("Username");

        signupEnterButton = new Button("Enter");
        signupEnterButton.setMnemonicParsing(false);

        loginLabel = new Label("Click here if you have an existing account");

        loginButton = new Button("Login");

        signupStatusLabel = new Label();

        signupPane.getChildren().addAll(signupTitleLabel, signupUsernameField, signupEnterButton, loginLabel, loginButton, signupStatusLabel);

        // main
        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        // yop
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        saveButton = new Button("Save & Exit");
        loadButton = new Button("Load");
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
                saveButton, loadButton, new Separator(), searchField, searchButton, new Separator(),
                new Label("Type:"), filterTypeComboBox,
                new Label("Status:"), filterStatusComboBox,
                filterButton
        );
        mainPane.setTop(topBar);

        // center
        entryListView = new ListView<>();
        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setPromptText("Select an entry to view details...");

        SplitPane centerSplit = new SplitPane(entryListView, detailsArea);
        centerSplit.setDividerPositions(0.6);
        mainPane.setCenter(centerSplit);

        // left
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

        // right
        VBox rateBox = new VBox();
        rateBox.setPadding(new Insets(10, 10, 10, 10));
        rateBox.setPrefWidth(260);
        rateBox.setSpacing(20);
        Label ratingLabel = new Label("Rating (1 - 10)");
        ratingLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        ratingField = new TextField();
        Label reviewLabel = new Label("Review");
        reviewLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        reviewField = new TextArea();
        reviewField.setPrefRowCount(3);
        rateButton = new Button("Rate & Review");

        rateBox.getChildren().addAll(ratingLabel, ratingField, reviewLabel, reviewField, rateButton);

        mainPane.setRight(rateBox);

        // bottom
        VBox bottomBox = new VBox(5);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));
        messageLabel = new Label("Ready.");
        messageLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        statisticsLabel = new Label("Total Entries: 0");

        bottomBox.getChildren().addAll(new Separator(), messageLabel, statisticsLabel);
        mainPane.setBottom(bottomBox);

        rootStack.getChildren().addAll(loginPane, signupPane, mainPane);
        setSectionVisible(loginPane, true);
        setSectionVisible(signupPane, false);
        setSectionVisible(mainPane, false);
    }

    /**
     * Shows or hides a section of the form, keeping its layout space in step
     * with its visibility so hidden sections do not leave gaps behind.
     *
     * @param node    the section to show or hide
     * @param visible true to show the section, false to hide it
     */
    private void setSectionVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    /**
     * Returns the root node of the whole interface so that MediaVaultApp can
     * put it inside a Scene.
     *
     * @return the root container holding all three pages
     */
    public StackPane getRoot() {
        return rootStack;
    }

    // login methods
    /**
     * Shows the login page and hides the other two.
     * <p>
     * <b>Postcondition:</b> only the login page is visible
     * </p>
     */
    public void loginPage() {
        setSectionVisible(loginPane, true);
        setSectionVisible(signupPane, false);
        setSectionVisible(mainPane, false);
    }

    /**
     * Shows the sign-up page and hides the other two.
     * <p>
     * <b>Postcondition:</b> only the sign-up page is visible
     * </p>
     */
    public void signupPage() {
        setSectionVisible(loginPane, false);
        setSectionVisible(signupPane, true);
        setSectionVisible(mainPane, false);
    }

    /**
     * Shows the main library page and hides the login and sign-up pages.
     * <p>
     * <b>Postcondition:</b> only the main page is visible
     * </p>
     */
    public void mainPage() {
        setSectionVisible(loginPane, false);
        setSectionVisible(signupPane, false);
        setSectionVisible(mainPane, true);
    }

    /**
     * Switches between the login page and the sign-up page, whichever of the
     * two is currently showing. Keeping this decision inside the view means
     * the controller never has to touch a JavaFX node itself.
     * <p>
     * <b>Postcondition:</b> the login and sign-up pages have swapped
     * visibility and both status messages are cleared
     * </p>
     */
    public void swapFront() {
        boolean loginShowing = loginPane.isVisible();

        if (loginShowing) {
            signupPage();
        } else {
            loginPage();
        }

        loginStatusLabel.setText("");
        signupStatusLabel.setText("");
    }
    /**
     * Registers the handler run when the login Enter button is pressed.
     *
     * @param handler the controller's login handler
     */
    public void setLoginHandler(EventHandler<ActionEvent> handler) {
        enterButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Sign up link button is pressed.
     *
     * @param handler the controller's page-swap handler
     */
    public void swapLoginSignup(EventHandler<ActionEvent> handler) {
        signupButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the sign-up Enter button is pressed.
     *
     * @param handler the controller's sign-up handler
     */
    public void setSignupHandler(EventHandler<ActionEvent> handler) {
        signupEnterButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Login link button is pressed.
     *
     * @param handler the controller's page-swap handler
     */
    public void swapSignupLogin(EventHandler<ActionEvent> handler) {
        loginButton.setOnAction(handler);
    }

    /** @return the username typed on the login page */
    public String getLoginUsername() {
        return loginUsernameField.getText();
    }

    /** @return the username typed on the sign-up page */
    public String getSignupUsername() {
        return signupUsernameField.getText();
    }

    /**
     * Shows a status message on the login page.
     *
     * @param text the message to display
     */
    public void setLoginStatus(String text) {
        loginStatusLabel.setText(text);
    }

    /**
     * Shows a status message on the sign-up page.
     *
     * @param text the message to display
     */
    public void setSignupStatus(String text) {
        signupStatusLabel.setText(text);
    }

    /**
     * Empties both username fields and both status messages, so that logging
     * out does not leave the previous user's name on screen.
     * <p>
     * <b>Postcondition:</b> the login and sign-up pages are blank
     * </p>
     */
    public void clearLoginFields() {
        loginUsernameField.clear();
        signupUsernameField.clear();
        loginStatusLabel.setText("");
        signupStatusLabel.setText("");
    }

    /**
     * Registers the handler run when the Add Entry button is pressed.
     *
     * @param handler the controller's add-entry handler
     */
    public void setAddEntryHandler(EventHandler<ActionEvent> handler) {
        addButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Remove Selected button is pressed.
     *
     * @param handler the controller's remove-entry handler
     */
    public void setRemoveEntryHandler(EventHandler<ActionEvent> handler) {
        removeButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Update Status button is pressed.
     *
     * @param handler the controller's update-status handler
     */
    public void setUpdateStatusHandler(EventHandler<ActionEvent> handler) {
        updateStatusButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Rate &amp; Review button is pressed.
     *
     * @param handler the controller's rate-and-review handler
     */
    public void setRateAndReviewHandler(EventHandler<ActionEvent> handler) {
        rateButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Filter button is pressed.
     *
     * @param handler the controller's filter handler
     */
    public void setFilterHandler(EventHandler<ActionEvent> handler) {
        filterButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Search button is pressed.
     *
     * @param handler the controller's search handler
     */
    public void setSearchHandler(EventHandler<ActionEvent> handler) {
        searchButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Save and Log Out button is pressed.
     *
     * @param handler the controller's save handler
     */
    public void setSaveHandler(EventHandler<ActionEvent> handler) {
        saveButton.setOnAction(handler);
    }

    /**
     * Registers the handler run when the Reload Saved button is pressed.
     *
     * @param handler the controller's load handler
     */
    public void setLoadHandler(EventHandler<ActionEvent> handler) {
        loadButton.setOnAction(handler);
    }

    /**
     * Registers what should happen when the user highlights a different row in
     * the library list. The view passes out the plain title of the selected
     * entry, so the controller never has to know how a row is formatted.
     * <p>
     * <b>Postcondition:</b> the given action runs on every selection change
     * </p>
     *
     * @param action the action to run with the selected entry's title
     */
    public void setEntrySelectedHandler(Consumer<String> action) {
        entryListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> action.accept(extractTitle(newVal)));
    }

    // =====================================================================
    // INPUT GETTERS
    // =====================================================================

    /** @return the media type selected in the add-entry form, or null */
    public String getSelectedType() {
        return typeComboBox.getValue();
    }

    /** @return the text currently in the title field */
    public String getTitleInput() {
        return titleField.getText();
    }

    /** @return the text currently in the genre field */
    public String getGenreInput() {
        return genreField.getText();
    }

    /** @return the status selected in the add-entry form, or null */
    public String getSelectedStatus() {
        return statusComboBox.getValue();
    }

    /** @return the text in the Movie director field */
    public String getDirectorInput() {
        return directorField.getText();
    }

    /** @return the text in the Movie duration field */
    public String getDurationInput() {
        return durationField.getText();
    }

    /** @return the text in the Movie release-year field */
    public String getReleaseYearInput() {
        return releaseYearField.getText();
    }

    /** @return the text in the TV series total-episodes field */
    public String getTotalEpisodesInput() {
        return totalEpisodesField.getText();
    }

    /** @return the text in the TV series watched-episodes field */
    public String getWatchedEpisodesInput() {
        return watchedEpisodesField.getText();
    }

    /** @return the text in the TV series season-count field */
    public String getSeasonCountInput() {
        return seasonCountField.getText();
    }

    /** @return the text in the video game platform field */
    public String getPlatformInput() {
        return platformField.getText();
    }

    /** @return the text in the video game required-specs field */
    public String getRequiredSpecsInput() {
        return requiredSpecsField.getText();
    }

    /** @return the text in the video game developer field */
    public String getDeveloperInput() {
        return developerField.getText();
    }

    /** @return the text in the video game hours-played field */
    public String getHoursPlayedInput() {
        return hoursPlayedField.getText();
    }

    /** @return the text in the rating field */
    public String getRatingInput() {
        return ratingField.getText();
    }

    /** @return the text in the review field */
    public String getReviewInput() {
        return reviewField.getText();
    }

    /** @return the type chosen in the filter bar */
    public String getFilterType() {
        return filterTypeComboBox.getValue();
    }

    /** @return the status chosen in the filter bar */
    public String getFilterStatus() {
        return filterStatusComboBox.getValue();
    }

    /** @return the keyword typed in the search field */
    public String getSearchKeyword() {
        return searchField.getText();
    }

    /**
     * Returns the plain title of the entry currently highlighted in the list,
     * with the type, status, and rating decoration stripped off.
     *
     * @return the selected entry's title, or an empty String if none is
     *         selected
     */
    public String getSelectedEntryTitle() {
        return extractTitle(entryListView.getSelectionModel().getSelectedItem());
    }

    /**
     * Pulls the plain title out of a formatted list row. A row looks like
     * {@code [Movie] Some Title - COMPLETED - 9/10}, so the type label is
     * dropped from the front and the status and rating are dropped from the
     * back. The two trailing fields are located from the end of the row rather
     * than the front, so a title that itself contains the separator, such as
     * {@code Mission - Impossible}, still comes back whole.
     *
     * @param text the formatted row text, which may be null
     * @return the plain title, or an empty String if the row is null or empty
     */
    private String extractTitle(String text) {
        if (text == null)
             text = "";
        else {
            int closingBracket = text.indexOf("] ");
            if (closingBracket != -1) {
                text = text.substring(closingBracket + 2).trim();
                int parenthesis = text.indexOf(" (");
                if (parenthesis != -1)
                    text = text.substring(0, parenthesis).trim();
            }
        }
        return text;
    }

    /**
     * Replaces the contents of the library list with the given display lines.
     * The controller has already turned each entry into a one-line String, so
     * the view never sees a model object.
     *
     * @param entries the display lines to show, one per entry
     */
    public void displayEntries(ArrayList<String> entries) { entryListView.getItems().setAll(entries); }

    /**
     * Shows the full details of a single entry in the details area.
     *
     * @param details the pre-formatted detail text produced by the model
     */
    public void showEntryDetails(String details) { detailsArea.setText(details); }

    /**
     * Shows a short status or error message at the bottom of the main page.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Shows the library summary line at the bottom of the main page.
     *
     * @param summary the summary text produced by the model
     */
    public void showStatistics(String summary) {
        statisticsLabel.setText(summary);
    }

    /**
     * Clears the data-entry fields after a successful add or rating so the
     * form is ready for the next entry.
     * <p>
     * <b>Postcondition:</b> every entry, rating, and review field is empty
     * </p>
     */
    public void clearInputFields() {
        titleField.clear(); genreField.clear(); directorField.clear();
        durationField.clear(); releaseYearField.clear(); totalEpisodesField.clear();
        watchedEpisodesField.clear(); seasonCountField.clear(); platformField.clear();
        requiredSpecsField.clear(); developerField.clear(); hoursPlayedField.clear();
        ratingField.clear(); reviewField.clear();
    }
}
