package cn.core;

import cn.definedPool.pool.ToolSutil.JDBCUtils;
import cn.http.HTTPRequest;
import cn.http.HTTPResponse;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;

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
            //获取服务端发来的数据,并处理请求.getInputStream方法可以得到一个输入流，客户端的Socket对象上的getInputStream方法得到的输入流其实就是从服务器端发回的客户端请求数据
            HTTPRequest request = new HTTPRequest(socket.getInputStream());

            if (request.getUrl() != null) {
                //利用httpResponse对浏览器做出数据响应,getOutputStream方法得到的是一个输出流，客户端的Socket对象上的getOutputStream方法得到的输出流其实就是发送给服务器端的数据
                HTTPResponse response = new HTTPResponse(socket.getOutputStream());

                //判断是否为注册操作,并做出相应的响应
                if (request.getUrl().startsWith("/RegisterUser")){
                    registerUser(request,response);
                    return;
                }

                //判断是否为登录操作，并作出相应的响应
                if (request.getUrl().startsWith("/LoginUser")){
                    loginUser(request,response);
                    return;
                }

                //响应文件的地址
                File file = new File(webContext.webRoot + "/" + request.getUrl());

                //判断访问的文件是否存在
                if (file.exists()) {
                    response.setStatus(200);
                } else {
                    //文件不存在响应404文件
                    file = new File(webContext.webRoot + "/" + webContext.notFoundPage);
                    response.setStatus(404);
                    System.out.println(file.getPath());
                }

                //设置参数
                response.setProtocol(webContext.procotol);
                //动态获取响应内容的类型
                response.setContentType(textType(request.getUrl()));
                response.setContentLength((int) file.length());

                //调用响应实体方法
                responseFile(response, file);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //实现注册操作
    private void registerUser(HTTPRequest request, HTTPResponse response) {

        //1.获取用户的注册信息
        String name = request.getuserMap("username");
        String password = request.getuserMap("password");
        //2.将用户的注册信息存储到数据库中
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //1.获取数据库连接对象
            conn = JDBCUtils.getConn();

            //2.声明sql语句,并获取带预编译的数据库传输器对象
            String sql = "insert into user (id,name,password) values (null,?,?)";
            ps = conn.prepareStatement(sql);

            //3.设置sql参数
            ps.setString(1,name);
            ps.setString(2,password);

            //4.执行sql语句
            ps.executeUpdate();

            //5.注册成功作出响应(提示用户注册成功)
            File file = new File(webContext.webRoot + "/register_success.html");
            //设置状态码
            response.setStatus(200);
            //设置客户端和服务端的交互协议
            response.setProtocol(webContext.procotol);
            //动态获取响应内容的类型
            response.setContentType(file.getPath());
            //获取响应文件长度
            response.setContentLength((int) file.length());
            //响应实体
            responseFile(response,file);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //如果是从连接池获取的连接对象，调用conn.close()只是还回到连接池中，并不关闭
            JDBCUtils.close(conn,ps,null);
        }
    }

    //实现登录操作
    private void loginUser(HTTPRequest request, HTTPResponse response) {
        //1.获取登录信息
        String name = request.getuserMap("username");
        String password = request.getuserMap("password");
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        //2.将获取的信息与数据库中的用户名和密码进行匹配
        try {
            // 从连接池获取数据库连接对象
            conn = JDBCUtils.getConn();
            //创建sql骨架，并获得带预编译效果的数据库传输对象
            String sql = "select * from user where name = ? and password = ?";
            ps = conn.prepareStatement(sql);
            //给sql骨架赋值
            ps.setString(1,name);
            ps.setString(2,password);
            //执行sql
            rs = ps.executeQuery();
            //3.匹配成功,登录成功并作出相应的响应
            if (rs.next()){
                //登录成功直接进去主页
                File file = new File(webContext.webRoot + "/" + "index.html");
                //设置状态码
                response.setStatus(200);
                //设置响应文件的长度
                response.setContentLength((int)file.length());
                //设置响应的文件类型
                response.setContentType(file.getPath());
                //设置服务端客户端通信协议
                response.setProtocol(webContext.procotol);
                //响应实体
                responseFile(response,file);
            }else{
                //4.匹配失败,登录失败并作出相应的响应
                //登录失败进入登录失败的提示页面
                File file = new File(webContext.webRoot + "/" + "login_failed.html");
                //设置响应状态码
                response.setStatus(200);
                //设置响应文件的长度
                response.setContentLength((int)file.length());
                //设置响应文件的类型
                response.setContentType(file.getPath());
                //设置服务器客服端的通信协议
                response.setProtocol(webContext.procotol);
                //响应实体
                responseFile(response,file);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,ps,rs);
        }

    }

    private String textType(String url) {
        //截取url的后缀名
        String lastStirng = url.substring(url.lastIndexOf(".") + 1);
        return webContext.map.get(lastStirng);
    }

    //4.响应实体
    public void responseFile(HTTPResponse response,File file){
        try {
            //带缓冲的输出流;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            //创建保存文件数据的数组
            byte[] b = new byte[(int) file.length()];

            //把文件读到数组里
            int res = bis.read(b);

            //响应到页面
            response.getOutputStream().write(b);

            //刷新IO缓冲区的内容
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭
            try {
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
