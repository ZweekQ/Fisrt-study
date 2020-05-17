package cn.core.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个类用来代表服务器端
 * 1.申明ServerSocket对象
 * 2.在构造函数中初始化ServerSocket对象
 * 3.创建start方法，用来接收客户端请求和响应
 * 4。创建main方法启动服务器
 * 5.测试
 */
public class WebServer {
    //1.申明ServerSocket对象
    private ServerSocket server;
    //1.1 申明线程池对象
    private ExecutorService theradPool;

    //2.在构造函数中初始化ServerSocket和threadPool对象
    public WebServer(){

        try{
            //初始化ServerSocket对象
            server = new ServerSocket(8080);

            //初始化threadPool对象,让其里面有100根线程
            theradPool = Executors.newFixedThreadPool(100);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //3.创建start方法，用来接收客户端请求并响应
    public void strat() {

        try {
            while(true) {
                //接收客户端请求
                Socket socket = server.accept();

                //利用线程池执行线程类
                theradPool.execute(new ClientHandler(socket));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //4.创建main方法启动服务器
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.strat();
    }
}
