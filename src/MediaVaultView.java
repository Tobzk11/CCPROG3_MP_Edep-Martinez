import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.*;

public class MediaVaultView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500, Color.BLACK);

        Image icon = new Image("6192518233113691.jpg"); // images
        Image bgImage = new Image("main.png");

        ImageView bgImg = new ImageView(bgImage); // background image
        bgImg.setFitHeight(500);
        bgImg.setFitWidth(500);

        primaryStage.getIcons().add(icon); // root settings
        primaryStage.setTitle("MediaVault");
        primaryStage.setResizable(false);

        Font custom = Font.loadFont(getClass().getResourceAsStream("ByteBounce.ttf"), 75); // title
        Text title = new Text();
        title.setText("MediaVault");
        title.setX(110);
        title.setY(200);
        title.setFont(custom);
        title.setFill(Color.WHITE);

        root.getChildren().addAll(bgImg, title);
        primaryStage.setScene(scene);
        primaryStage.show();  // must be last line
    }
}
