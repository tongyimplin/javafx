package top.jafar.javafx.demo.demo01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXML TableView Example");
        Pane myPane = FXMLLoader.load(loadResource("fxml/Main.fxml"));
        Scene scene = new Scene(myPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static URL loadResource(String path) {
        ClassLoader classLoader = MainApp.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        System.out.println("加载到["+path+"]: "+resource);
        return resource;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
