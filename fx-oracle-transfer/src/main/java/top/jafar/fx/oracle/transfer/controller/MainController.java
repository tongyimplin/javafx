package top.jafar.fx.oracle.transfer.controller;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import top.jafar.fx.oracle.transfer.model.DataSourceModel;
import top.jafar.fx.oracle.transfer.util.Constants;
import top.jafar.fx.oracle.transfer.util.FileHelper;
import top.jafar.fx.oracle.transfer.util.TransferHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainController extends AbsController {

    @FXML private ChoiceBox<String> wSourceDB;
    @FXML private ChoiceBox<String> wTargetDB;
    @FXML private TextArea wConsole;

    private List<String> allDbList = new ArrayList<>();
    private ObservableList<String> sourceList = FXCollections.observableArrayList();
    private ObservableList<String> targetList = FXCollections.observableArrayList();
    private StringBuffer consoleBuffer = new StringBuffer();

    @Override
    public void init() {
        super.init();
        Constants.DATASOURCE_LIST.forEach(t -> {
            allDbList.add(t.getName());
        });
        sourceList.addAll(allDbList);
        targetList.addAll(allDbList);
        wSourceDB.setItems(sourceList);
        wTargetDB.setItems(targetList);

        ReadOnlyIntegerProperty readOnlyIntegerProperty = wSourceDB.getSelectionModel().selectedIndexProperty();
        readOnlyIntegerProperty.addListener((ObservableValue<? extends Number> ov, Number oldValue, Number newVal) -> {
            String value = allDbList.get(newVal.intValue());
            targetList.clear();
            allDbList.forEach(t -> {
                if (!t.equals(value)) {
                    targetList.add(t);
                }
            });
        });
    }

    @FXML
    protected void gotoDatasourceManager(ActionEvent event) throws IOException {
        gotoPage("DatabaseManagerController.fxml");
    }

    @FXML
    protected void prepareHandler(ActionEvent event) {
        String sourceDB = wSourceDB.getSelectionModel().getSelectedItem();
        String targetDB = wTargetDB.getSelectionModel().getSelectedItem();
        println("准备将数据从 ["+sourceDB+"] 迁入到 ["+targetDB+"]");
        ObservableList<DataSourceModel> datasourceList = Constants.DATASOURCE_LIST;
        DataSourceModel sourceModel = null;
        DataSourceModel targetModel = null;
        for (DataSourceModel dataSourceModel : datasourceList) {
            if (dataSourceModel.getName().equals(sourceDB)) {
                sourceModel = dataSourceModel;
            }
            if (dataSourceModel.getName().equals(targetDB)) {
                targetModel = dataSourceModel;
            }
        }
        TransferHelper sourceHelper = new TransferHelper(sourceModel);
        TransferHelper targetHelper = new TransferHelper(targetModel);
        try(FileHelper sqlLogHelper = FileHelper.getWritableFile("sql-log");) {
            println("SQL日志文件: "+sqlLogHelper.getLogPath());
            List<String> allTables = sourceHelper.listAllTables();
            int allTableSize = allTables.size();
            println("查询到共计"+allTableSize+"张表, "+allTables);
            // 获取建表语句
            /*allTables.forEach(name -> {
                try {
//                    println("获取建表SQL： "+name);
//                    String createTableSQL = sourceHelper.fetchCreateTableSQL(name);
//                    println(createTableSQL);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });*/
            // 获取序列
            List<String> sequences = sourceHelper.listAllSequences();
            println("查询到共计"+sequences.size()+"条序列, "+sequences);
            // 获取sequence值
            sequences.stream().forEach(sequenceName -> {
                try {
                    int curVal = sourceHelper.fetchSequenceValue(sequenceName);
                    // 生产对应的创建SQL
                    String seqDDL = String.format("create sequence %s increment by 1 start with %d;", sequenceName, curVal).toUpperCase();
                    // 插入序列
                    if (targetHelper.existSequence(sequenceName)) {
                        // 如果存在则删除sequence
                        String seqDropDDL = String.format("drop sequence %s", sequenceName).toUpperCase();
                        sqlLogHelper.append(seqDropDDL+";");
                        targetHelper.execSequence(seqDropDDL);
                    }
                    sqlLogHelper.append(seqDDL+";");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            println(e.getMessage());
        }
    }

    protected void println(String msg) {
        consoleBuffer.append("[")
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .append("] ")
                .append(msg)
                .append("\r\n");
        wConsole.setText(consoleBuffer.toString());
        wConsole.setScrollTop(Double.MAX_VALUE);
    }
}
