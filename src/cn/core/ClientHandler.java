package cn.core;

import cn.http.HTTPRequest;
import cn.http.HTTPResponse;

import java.io.*;
import java.net.Socket;
import java.util.PrimitiveIterator;
import cn.webService.webContext;

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

            //获取服务端发来的数据,并处理请求.getInputStream方法可以得到一个输入流，客户端的Socket对象上的getInputStream方法得到输入流其实就是从服务器端发回的数据
            HTTPRequest request = new HTTPRequest(socket.getInputStream());

            //利用httpResponse对浏览器做出数据响应,getOutputStream方法得到的是一个输出流，客户端的Socket对象上的getOutputStream方法得到的输出流其实就是发送给服务器端的数据
            HTTPResponse response = new HTTPResponse(socket.getOutputStream());

            //响应文件的地址
            File file = new File(webContext.webRoot + "/" + request.getUrl());

            //判断访问的文件是否存在
            if (file.exists()) {
                response.setStatus(200);
            }else{
                //文件不存在响应404文件
                file = new File(webContext.webRoot + "/" + webContext.notFoundPage);
                response.setStatus(404);
            }

            //设置参数
            response.setProtocol(webContext.procotol);
            //动态获取响应内容的类型
            response.setContentType(textType(request.getUrl()));
            response.setContentLength((int)file.length());

            responseFile(response,file);
            //调用响应实体方法
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String textType(String url) {
        //截取url的后缀名
        String lastStirng = url.substring(url.lastIndexOf(".") + 1);
        return webContext.map.get(lastStirng);
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
