package cn.definedPool.pool.TestFile;

import cn.definedPool.pool.ToolSutil.JDBCUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 这个类用来测试包装后的ConnectionDecorator类
 */
public class TestDecorator {
    @Test
    public void Testdecorctor(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //创建C3P0连接池对象
        ComboPooledDataSource pool = new ComboPooledDataSource();
        try{
            //1.注册驱动获取连接,获取数据库连接
            //调用c3p0连接池
            conn = pool.getConnection();

            // 2.获取传输器
            String sql = "select * from customer where id = ?";
            ps = conn.prepareStatement(sql);

            //3.赋值
            ps.setInt(1,1);

            //4.执行sql
            rs = ps.executeQuery();

            //5.便利结果集
            while(rs.next()){
                System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //6.释放资源并将Connection对象返回连接池
            JDBCUtils.close(conn,ps,rs);
        }
    }
}
