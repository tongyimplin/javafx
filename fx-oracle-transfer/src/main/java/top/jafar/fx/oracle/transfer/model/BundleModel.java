package top.jafar.fx.oracle.transfer.model;

import java.util.HashMap;
import java.util.Map;

public class BundleModel {
    private Map<String, Object> parameterMap = new HashMap<>();

    public void putExtras(String key, Object value) {
        parameterMap.put(key, value);
    }

    public boolean containsKey(String key) {
        return parameterMap.containsKey(key);
    }

    public <T> T getExtra(String key) {
        return (T) parameterMap.get(key);
    }

}
