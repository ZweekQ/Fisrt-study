package ToolSutil;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * JDBC工具类
 */
public class JDBCUtils {
    //1.创建静态的工具方法
    private JDBCUtils(){
    }
    //申明为成员变量，以便整个类使用,设置为静态成员因为静态代码块只能引用静态上下文
    static ResourceBundle rb = null;
    //在静态代码块加载一次属性文件，不用每次时用都加载一次,不用每次调用GetCoonetion方法都加载一起属性文件。
    static{
        //读取属性文件
        rb = ResourceBundle.getBundle("JDBC");
    }
    //２.提供静态的getConnection方法，封装１，２步
    public static Connection getConnection(){
        try{
            //1.注册驱动
            Class.forName(rb.getString("DriverClass"));
            //2.获取数据库连接
            String url = rb.getString("jdbcUrl");
            String user = rb.getString("user");
            String password = rb.getString("password");
            return DriverManager.getConnection(url,user,password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //3.提供静态的close方法,封装第六步
    public static void close(Connection conn, Statement st, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(st != null){
            try{
                st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if (rs != null){
            try{
                rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
