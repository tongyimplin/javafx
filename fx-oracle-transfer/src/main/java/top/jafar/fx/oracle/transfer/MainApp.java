package top.jafar.fx.oracle.transfer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import top.jafar.fx.oracle.transfer.controller.AbsController;
import top.jafar.fx.oracle.transfer.controller.DatabaseManagerController;
import top.jafar.fx.oracle.transfer.model.PageModel;
import top.jafar.fx.oracle.transfer.util.ResourceLoader;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("oracle迁移");
        PageModel page = AbsController.load(primaryStage, "MainController.fxml");
        page.getController().init();
        primaryStage.setScene(page.getScene());
        primaryStage.show();
    }
}
