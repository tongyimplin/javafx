package top.jafar.fx.oracle.transfer.util;

import top.jafar.fx.oracle.transfer.model.DataSourceModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferHelper {
    private DataSourceModel dataSource;
    private Connection connection;

    public TransferHelper(DataSourceModel dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 测试数据库是否通
     * @return
     */
    public boolean ping() {
        try(Connection conn = getConnection();
            Statement statement = conn.createStatement();) {
            return statement.execute("select 'x' from dual");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库连接
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            // 未初始化，初始化链接
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String jdbcUrl = String.format("jdbc:oracle:thin:@//%s:%d/%s", dataSource.getIp(), dataSource.getPort(), dataSource.getSid());
            connection = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());
        }
        return connection;
    }

    // 获取表列表
    public List<String> listAllTables() throws SQLException, ClassNotFoundException {
        List<String> allTables = new ArrayList<>();
        String sql = "select table_name from user_tables";
        Connection conn = getConnection();
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            while (resultSet.next()) {
                allTables.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allTables;
    }

    // 获取建表SQL
    public String fetchCreateTableSQL(String tableName) throws SQLException, ClassNotFoundException {
        String sql = "select dbms_metadata.get_ddl('TABLE', '"+tableName.toUpperCase()+"') as ddl_sql from dual";
        System.out.println("SQL: "+sql);
        Connection conn = getConnection();
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            if (resultSet.next()) {
                String ddlSql = resultSet.getString("ddl_sql");
                if (ddlSql != null) {
                    return ddlSql.replace("\""+dataSource.getUsername().toUpperCase()+"\".", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取序列列表
    public List<String> listAllSequences() throws SQLException, ClassNotFoundException {
        String sql = "select SEQUENCE_NAME from dba_sequences where sequence_owner='"+dataSource.getUsername().toUpperCase()+"'";
        System.out.println("SQL: "+sql);
        Connection conn = getConnection();
        List<String> list = new ArrayList<>();
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            while (resultSet.next()) {
                String sequenceName = resultSet.getString("SEQUENCE_NAME");
                list.add(sequenceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 获取当前序列的值
    public int fetchSequenceValue(String sequenceName) throws SQLException, ClassNotFoundException {
        String sql = "select " + sequenceName + ".nextval as val from dual";
        System.out.println("SQL: "+sql);
        Connection conn = getConnection();
        int val = 1;
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            if (resultSet.next()) {
                val = resultSet.getInt("val");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    // 检测序列是否存在
    public boolean existSequence(String sequenceName) throws SQLException, ClassNotFoundException {
        String sql = String.format("select count(*) as c from dba_sequences where SEQUENCE_OWNER='%s' and sequence_name='%s'"
                , dataSource.getUsername().toUpperCase(), sequenceName.toUpperCase());
        System.out.println("SQL: "+sql);
        Connection conn = getConnection();
        boolean b = false;
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet resultSet = pstmt.executeQuery()
        ) {
            if (resultSet.next()) {
                b = resultSet.getInt("c") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    // 插入序列
    public boolean execSequence(String sql) throws SQLException, ClassNotFoundException {
        System.out.println("SQL: "+sql);
        Connection conn = getConnection();
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            return pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
