package ua.trasa;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ua.trasa.javaclass.servisClass.AlertAndInform;

public class Main extends Application {
    AlertAndInform inform = new AlertAndInform();
    public static String icoImage = "/images/trasa_img.png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource(icoImage).toExternalForm()));
        primaryStage.setTitle("Trasa");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->
        {
            inform.title = "Вихід з програми";
            inform.hd = "Закрити програму?";
            inform.ct = "Всі незбережені дані буде втрачено";
            inform.confirmation(event);
        });
}

    public static void main(String[] args) {
        launch(args);
    }
}
