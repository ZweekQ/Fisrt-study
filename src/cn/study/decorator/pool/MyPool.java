package cn.study.decorator.pool;

import ToolSutil.JDBCUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 用来实现自定义连接池技术
 */
public class MyPool implements DataSource {
    //1.创建一个存放数据库连接对象的容器,增删改多用linkedList
    private static final List<Connection> list = new LinkedList<>();

    //2.创建静态代码块，初始化容器
    static {
        for (int i=0;i<3;i++){
            Connection conn = JDBCUtils.getConnection();
            list.add(conn);
        }
    }

    //3.提供getconnection方法，对外提供获取连接数据库对象
    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = list.remove(0);//获取元素之后，会删除该元素
        System.out.println("减少了一个对象");
        return conn;
    }

    //4.还回数据库连接对象到列表
    public void returnConn(Connection conn){
        try {
            if (conn != null && !conn.isClosed()) list.add(conn);//isClosed()判断连接对象是否被关闭
            System.out.println("成功还回");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
