package bot;

/**
 * Created by Keith Tsang on 29/03/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerGUI extends Application {

    private PlayerGUIController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));

        Parent root = loader.load();
        controller = loader.getController();

        Scene newScene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setTitle("Texas Hold'em");
        newStage.setScene(newScene);
        newStage.show();

        BotParser parser = new BotParser(controller);
        Thread t = new Thread(new Runnable() {
            public void run() {
                parser.run();
            }
        });
        t.start();
    }
}
