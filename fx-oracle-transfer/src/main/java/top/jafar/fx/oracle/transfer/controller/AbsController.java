package top.jafar.fx.oracle.transfer.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import top.jafar.fx.oracle.transfer.model.BundleModel;
import top.jafar.fx.oracle.transfer.model.PageModel;
import top.jafar.fx.oracle.transfer.util.ResourceLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AbsController implements Initializable {

    protected Stage stage;
    protected BundleModel bundle = new BundleModel();

    public void init() {}

    protected void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    protected void warn(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg);
        alert.showAndWait();
    }

    protected void error(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    protected static FXMLLoader getFXMLLoader(String controllerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ResourceLoader.loadControllerResource(controllerName));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        return fxmlLoader;
    }


    public static PageModel load(Stage stage, String controllerName) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(controllerName);
        Scene scene = new Scene(fxmlLoader.load());
        AbsController controller = fxmlLoader.getController();
        controller.setStage(stage);
        return new PageModel(scene, controller);
    }

    public void gotoPage(String pageName) throws IOException {
        PageModel page = load(stage, pageName);
        page.getController().setBundle(bundle);
        stage.setScene(page.getScene());
        stage.sizeToScene();
        page.getController().init();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public BundleModel getBundle() {
        return bundle;
    }

    public void setBundle(BundleModel bundle) {
        this.bundle = bundle;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
