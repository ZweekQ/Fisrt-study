package cn.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 这个类用来封装请求信息
 * 1.申明三个请求的属性
 * 2.在构造函数中初始化参数的值
 * 3.改造run方法
 */
public class HTTPRequest {

    //请求方式
    private String method;

    //请求的资源路径
    private String url;

    //遵循的请求协议和
    private String protocol;


    //创建构造函数，传入一个输入流对象
    public HTTPRequest(InputStream in){
        try {
            //InputStreamReader()对象获取一个reder对象的结构的实现类
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            if (line != null && line.length() > 0){
                String[] datas = line.split(" ");
                method = datas[0];
                url = datas[1];
                //给网站设置默认主页
                if(url.equals("/")){
                    url = "/index.html";
                }
                 protocol = datas[2];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //对外提供参数的修改以及访问方式

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
