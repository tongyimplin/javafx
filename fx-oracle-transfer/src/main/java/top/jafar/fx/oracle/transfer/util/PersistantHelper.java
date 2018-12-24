package top.jafar.fx.oracle.transfer.util;

import com.alibaba.fastjson.JSON;
import top.jafar.fx.oracle.transfer.model.DataSourceModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistantHelper {

    static File CONFIG_FILE = new File(System.getProperty("user.home") + "/fx-oracle-transfer/config.conf");

    public static List<DataSourceModel> loadDatasource() {
        if (!CONFIG_FILE.exists()) {
            // 如果不存在，创建新的
            File parentFile = CONFIG_FILE.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
                Logger.log("创建目录: "+parentFile);
            }
            try {
                CONFIG_FILE.createNewFile();
                Logger.log("创建新文件: "+CONFIG_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try(
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(CONFIG_FILE), "utf-8");
            BufferedReader reader = new BufferedReader(inputStream)
        ){
            StringBuffer buffer = new StringBuffer();
            reader.lines().forEach(s -> buffer.append(s));
            Logger.log("读取到配置: "+buffer.toString());
            List<DataSourceModel> list = JSON.parseArray(buffer.toString(), DataSourceModel.class);
            Logger.log("反序列化: "+ list);
            return list;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    public static void saveDatasource(Object obj) {
        try(
                FileWriter writer = new FileWriter(CONFIG_FILE)
                ) {
            String s = JSON.toJSONString(obj);
            writer.write(s);
            Logger.log("保存成功: "+s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
