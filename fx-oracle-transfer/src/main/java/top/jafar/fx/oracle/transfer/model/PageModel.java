package top.jafar.fx.oracle.transfer.model;

import javafx.scene.Scene;
import top.jafar.fx.oracle.transfer.controller.AbsController;

public class PageModel {
    private Scene scene;
    private AbsController controller;

    public PageModel(Scene scene, AbsController controller) {
        this.scene = scene;
        this.controller = controller;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public AbsController getController() {
        return controller;
    }

    public void setController(AbsController controller) {
        this.controller = controller;
    }
}
