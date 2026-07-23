import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.*;

public class MediaVaultView extends Application {

    private String username;
    private String fontFam;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500, Color.BLACK);

        Image icon = new Image("icon.jpg"); // images
        Image bgImage = new Image("main.png");

        ImageView bgImg = new ImageView(bgImage); // background image
        bgImg.setFitHeight(500);
        bgImg.setFitWidth(500);

        primaryStage.getIcons().add(icon); // root settings
        primaryStage.setTitle("MediaVault");
        primaryStage.setResizable(false);

        Font custom = Font.loadFont(getClass().getResourceAsStream("ByteBounce.ttf"), 75); // title
        fontFam = custom.getFamily();
        Text title = new Text();
        title.setText("MediaVault");
        title.setX(110);
        title.setY(200);
        title.setFont(custom);
        title.setFill(Color.WHITE);

        TextField user = new TextField();
        user.setPromptText("Username");
        user.setLayoutX(175);
        user.setLayoutY(225);

        Button enter = new Button("Submit");
        enter.setLayoutX(220);
        enter.setLayoutY(260);
        enter.setFont(Font.font(fontFam, 15));

        Label warning = new Label();
        warning.setLayoutX(178);
        warning.setLayoutY(290);
        warning.setFont(Font.font(fontFam, 13));
        warning.setTextFill(Color.RED);

        enter.setOnAction(event ->
        {String input = user.getText();
        if (input.trim().isEmpty())
            warning.setText("Please enter a valid username");
        else {
            username = input;
            // next stage
        }
        });

        root.getChildren().addAll(bgImg, title, user, enter, warning);
        primaryStage.setScene(scene);
        primaryStage.show();  // must be last line
    }
}
