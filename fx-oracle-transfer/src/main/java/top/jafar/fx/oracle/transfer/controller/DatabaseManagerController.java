package top.jafar.fx.oracle.transfer.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import top.jafar.fx.oracle.transfer.model.DataSourceModel;
import top.jafar.fx.oracle.transfer.util.Constants;
import top.jafar.fx.oracle.transfer.util.PersistantHelper;
import top.jafar.fx.oracle.transfer.util.TransferHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManagerController extends AbsController {
    @FXML private TextField wDatabaseName;
    @FXML private TextField wIp;
    @FXML private TextField wPort;
    @FXML private TextField wSid;
    @FXML private TextField wUsername;
    @FXML private TextField wPass;
    @FXML private TableView<DataSourceModel> wTableView;
    @FXML private Label wTestLog;
    private boolean isConnecting = false;
    private int selectedIndex = -1;

    private ObservableList<DataSourceModel> dataSourceModels = Constants.DATASOURCE_LIST;

    @Override
    public void init() {
        wTableView.setItems(dataSourceModels);
    }

    @FXML
    protected void connectionTest(ActionEvent event) {
        // 查看选中的链接
        DataSourceModel formData = getFormData();
        if (formData == null) {
            return;
        }
        if (isConnecting) {
            return;
        }
        isConnecting = true;
        // 测试数据库连接
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // 获取选择的链接
                Platform.runLater(() -> wTestLog.setText("连接中，请稍后..."));
                try {
                    TransferHelper transferHelper = new TransferHelper(formData);
                    transferHelper.ping();
                    Platform.runLater(() -> {
                        wTestLog.setText("连接成功!");
                        wTestLog.setTextFill(Paint.valueOf("#09eb14"));
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        wTestLog.setText("连接失败："+e.getMessage());
                        wTestLog.setTextFill(Paint.valueOf("#ff0000"));
                    });
                    e.printStackTrace();
                }
                isConnecting = false;
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    protected void addNewHandler(ActionEvent event) {
        selectedIndex = -1;
        wDatabaseName.setText("");
        wIp.setText("");
        wPort.setText("");
        wSid.setText("");
        wUsername.setText("");
        wPass.setText("");
    }

    @FXML
    protected void focusedDatasourceHandler(MouseEvent event) {
        TableView.TableViewSelectionModel<DataSourceModel> selectionModel = wTableView.getSelectionModel();
        selectedIndex = selectionModel.getSelectedIndex();
        DataSourceModel selectedItem = selectionModel.getSelectedItem();
        if (selectedItem != null) {
            wDatabaseName.setText(selectedItem.getName());
            wIp.setText(selectedItem.getIp());
            wPort.setText(selectedItem.getPort()+"");
            wSid.setText(selectedItem.getSid());
            wUsername.setText(selectedItem.getUsername());
            wPass.setText(selectedItem.getPassword());
        }else {
            System.out.println("未检测到选中的列");
        }
    }

    @FXML
    protected void deleteDatasource(ActionEvent event) {
        TableView.TableViewSelectionModel<DataSourceModel> selectionModel = wTableView.getSelectionModel();
        dataSourceModels.remove(selectionModel.getFocusedIndex());
    }

    @FXML
    protected void gotoMain(ActionEvent event) throws IOException {
        gotoPage("MainController.fxml");
    }

    @FXML protected void submitDatasource(ActionEvent e) {
        DataSourceModel formData = getFormData();

        // 加入列表
        if (selectedIndex > -1) {
            dataSourceModels.set(selectedIndex, formData);
            Constants.DATASOURCE_LIST.set(selectedIndex, formData);
        }else {
            dataSourceModels.add(formData);
        }
        PersistantHelper.saveDatasource(dataSourceModels);
    }

    private DataSourceModel getFormData() {
        try {
            String databaseName = wDatabaseName.getText();
            if (databaseName.isEmpty()) {
                throw new RuntimeException("请输入数据库名称!");
            }
            String ip = wIp.getText();
            if (ip.isEmpty()) {
                throw new RuntimeException("请输入IP地址!");
            }
            int port = Integer.parseInt(wPort.getText());
            String sid = wSid.getText();
            if (sid.isEmpty()) {
                throw new RuntimeException("请输入SID!");
            }
            String username = wUsername.getText();
            if (username.isEmpty()) {
                throw new RuntimeException("请输入用户名!");
            }
            String pass = wPass.getText();
            if (pass.isEmpty()) {
                throw new RuntimeException("请输入密码!");
            }
            DataSourceModel dataSource = new DataSourceModel();
            dataSource.setName(databaseName);
            dataSource.setIp(ip);
            dataSource.setPort(port);
            dataSource.setSid(sid);
            dataSource.setUsername(username);
            dataSource.setPassword(pass);
            return dataSource;
        } catch (Exception e1) {
            e1.printStackTrace();
            error(e1.getMessage());
        }
        return null;
    }

}
