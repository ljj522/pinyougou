package cn.itcast.demo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Test {

    public static void main(String[] args) throws IOException, TemplateException {

        //1.创建一个配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板的所在的目录
        configuration.setDirectoryForTemplateLoading(new File("D:\\idea_workspaces\\pinyougouparent\\freemarkerDemo\\src\\main\\resources"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");

        //4获取模板对象
        Template template = configuration.getTemplate("test.ftl");

        //5.创建数据模型（可以是对象，也可以是map）
        Map map= new HashMap();
        map.put("name","李四");
        map.put("message","欢迎来到李瑾杰世界");
        map.put("success",false);

        List goodsList=new ArrayList();
        Map goods1=new HashMap();
        goods1.put("name", "苹果");
        goods1.put("price", 5.8);
        Map goods2=new HashMap();
        goods2.put("name", "香蕉");
        goods2.put("price", 2.5);
        Map goods3=new HashMap();
        goods3.put("name", "橘子");
        goods3.put("price", 3.2);
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        map.put("goodsList", goodsList);

        map.put("today",new Date());

        map.put("point", 102920122);


        //6.创建一个输出对象
        Writer out = new FileWriter("d:\\test.html");
        //7.输出
        template.process(map,out);
        //8.关闭out
        out.close();

    }
}
