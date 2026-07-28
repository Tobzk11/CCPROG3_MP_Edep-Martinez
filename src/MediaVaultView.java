import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MediaVaultView extends AnchorPane {

    private Label titleLabel;
    private TextField usernameField;
    private Button enterButton;
    private VBox centerContainer;

    public MediaVaultView() {
        this.setPrefHeight(400.0);
        this.setPrefWidth(600.0);
        this.getStyleClass().add("main-bg");

        if (getClass().getResource("style.css") != null)
            this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        titleLabel = new Label("MediaVault");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.getStyleClass().add("title-label");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(25.0);
        usernameField.setPrefWidth(200.0);
        usernameField.setMaxWidth(Double.NEGATIVE_INFINITY); // Equivalent to maxWidth="-Infinity"
        usernameField.setFont(Font.font("Calibri", 12.0));

        enterButton = new Button("Enter");
        enterButton.setMnemonicParsing(false);
        enterButton.setTextFill(Color.WHITE);
        enterButton.getStyleClass().add("enter-button");

        centerContainer = new VBox(15.0);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPrefHeight(400.0);
        centerContainer.setPrefWidth(301.0);
        centerContainer.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

        AnchorPane.setLeftAnchor(centerContainer, 153.0);
        AnchorPane.setTopAnchor(centerContainer, 0.0);

        centerContainer.getChildren().addAll(titleLabel, usernameField, enterButton);

        this.getChildren().add(centerContainer);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public Button getEnterButton() {
        return enterButton;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }
}
