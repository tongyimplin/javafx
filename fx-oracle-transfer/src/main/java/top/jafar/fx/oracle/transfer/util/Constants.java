package top.jafar.fx.oracle.transfer.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import top.jafar.fx.oracle.transfer.model.DataSourceModel;

import java.util.List;

public class Constants {

    // 数据源
    public static ObservableList<DataSourceModel> DATASOURCE_LIST = FXCollections.observableArrayList();

    static {
        List<DataSourceModel> list = PersistantHelper.loadDatasource();
        DATASOURCE_LIST.addAll(list);
    }
}
