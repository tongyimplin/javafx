package top.jafar.fx.oracle.transfer.model;

import java.util.List;

public class TableModel {
    // 表名
    private String name;
    // 建表语句
    private String ddlSQL;
    // 数据插入语句
    private List<String> dmlSQLs;
}
