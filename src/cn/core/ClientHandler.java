package cn.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.PrimitiveIterator;

/**
 * 这个类用来完成webserver的抽取过程
 */
public class ClientHandler implements Runnable{
    //1.申明Socket对象
    private final Socket socket;

    //2.在构造函数中传入Socket对象
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    //3.重写run方法抽取响应的代码
    @Override
    public void run() {
        //获取数据响应
        try {
            PrintStream ps = new PrintStream(socket.getOutputStream());

            //拼接正确格式的响应代码
            //1.状态行 = 协议名/协议版本号 状态码 状态描述
            ps.println("HTTP/1.1 200 OK");

            //2.若干响应头
            //2.1 告诉浏览器要响应的数据格式
            ps.println("Content-Type:text/html");
            //2.2 响应数据的长度
//            String data = "Hello Web";
//            ps.println("Content-Length" + data.length());
            //响应网页文件
            File file = new File("web/hello.html");
            ps.println("Content-Lenght:" + file.length());
            //3.空白行
            ps.println();

            //4.响应实体
            //带缓冲的输出流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] b = new byte[(int)file.length()];
            bis.read(b);//把文件读到数组里

            ps.write(b);//把文件写出去,显示到页面上

            //刷新
            ps.flush();

            //关闭
            socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
