package cn.webService;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**动态提供元素
 *
 */

public class webContext {

    public static int port;
    public static String procotol;
    public static int maxSize;
    public static String notFoundPage;
    public static String webRoot;
    //创建保存响应文件类型的map
    public static Map<String,String> map = new HashMap<>();
    public static Map<Integer,String> statusMap = new HashMap<>();
    //初始化成员变量
    static{
        init();
    }

    private static void init() {
        try {
            SAXReader reader = new SAXReader();
            //读取配置文件
            Document doc = reader.read("web\\WEB-INF\\web1.xml");
            //获取根元素
            Element server = doc.getRootElement();
            //获取service元素
            Element service = server.element("service");
            //获取connctor元素
            Element connctor = service.element("connctor");
            //获取webRoot参数
            webRoot = service.element("webRoot").getText();
            //获取typemappings元素
            List<Element> list = server.element("typemappings").elements();

            //循环获取typemapping里面的元素的属性名和属性值存入到map里面
            for (Element element:list) {
                String ext = element.attributeValue("ext");
                String type = element.attributeValue("type");
                map.put(ext,type);
            }
            //获取connctor元素的属性值
            port = Integer.parseInt(connctor.attributeValue("port"));
            procotol = connctor.attributeValue("procotol");
            maxSize = Integer.parseInt(connctor.attributeValue("maxSize"));
            notFoundPage = service.elementText("not-found-page");
            //状态码对应的状态短语
            statusMap.put(200,"OK");
            statusMap.put(404,"not Found");
            statusMap.put(500,"Internal Server Error");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
