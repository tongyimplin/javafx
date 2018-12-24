package top.jafar.fx.oracle.transfer.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {


    /**
     * 加载资源
     * @param path
     * @return
     */
    public static URL loadResource(String path) {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new RuntimeException("未加载到该资源: " + path);
        }
        return resource;
    }

    public static URL loadControllerResource(String path) {
        return loadResource("fxml/"+path);
    }

    public static InputStream loadInputStream(String path) {
        InputStream resourceAsStream = ResourceLoader.class.getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new RuntimeException("未加载到该资源: " + path);
        }
        return resourceAsStream;
    }

    public static InputStream loadControllerInputStream(String controllerName) {
        return loadInputStream("fxml/" + controllerName);
    }
}
