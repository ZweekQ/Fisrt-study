package cn.core;

import cn.http.HTTPRequest;
import cn.http.HTTPResponse;

import java.io.*;
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

            //获取并处理请求
            HTTPRequest request = new HTTPRequest(socket.getInputStream());

            //利用httpResponse对浏览器做出数据响应
            HTTPResponse response = new HTTPResponse(socket.getOutputStream());

            //响应文件的地址
            File file = new File("C:\\NewFile\\web"+ request.getUrl());

            //设置参数
            response.setProtocol("HTTP/1.1");
            response.setStatus(200);
            response.setContentType("text/html");
            response.setContentLength((int)file.length());

            responseFile(response,file);
            //调用响应实体方法
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void responseFile(HTTPResponse response,File file){
        //4.响应实体
        //带缓冲的输出流;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            //创建保存文件数据的数组
            byte[] b = new byte[(int) file.length()];

            //把文件读到数组里
            int res = bis.read(b);

            //响应到页面
            response.getOutputStream().write(b);

            //刷新IO缓冲区的内容
            response.getOutputStream().flush();

            //关闭
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
