package ua.trasa;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static String icoImage = "/images/031.png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource(icoImage).toExternalForm()));
        primaryStage.setTitle("Trasa");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
