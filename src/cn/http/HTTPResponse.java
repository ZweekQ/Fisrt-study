package cn.http;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 封装响应信息
 * 1.在构造函数中掺入OutputStream，并保存在类中
 * 2.改造getOutStream方法
 * 3.改造run方法
 */

public class HTTPResponse {
    //响应的协议名和版本号
    private String protocol;

    //响应的状态码
    private int status;

    //响应头contentType
    private String contentType;

    //响应头contentLength
    private int contentLength;

    private OutputStream outputStream;

    //修改构造方法
    public HTTPResponse(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    //对外提供访问和修改私有属性的方法
    //包装此方法只执行一次
    private boolean isSend; //默认是flase
    public OutputStream getOutputStream() {
        if (!isSend) {
            //提取拼接响应头的代码
            PrintStream ps = new PrintStream(outputStream);

            //状态行
            ps.println(protocol + " " + status + " " + "OK");

            //响应头:响应的数据格式
            ps.println("Content-type:" + contentType);

            //响应头:响应的数据长度
            ps.println("Content-Length:" + contentLength);

            //空白行
            ps.println();

            isSend = true;
        }

        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

}
