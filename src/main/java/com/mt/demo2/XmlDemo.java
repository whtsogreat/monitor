package com.mt.demo2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

@RestController
@RequestMapping("/xml")
public class XmlDemo {

    /**
     * @Author: wss
     * @Date: 11:04 2021/10/27
     * @Description: xml转换为string导出
     */
    @RequestMapping("/string")
    public String string() {
        try {
            // 读取 xml 文件
//            File fileinput = new File("E:\\Documents\\censoft\\demo\\xmldemo\\src\\main\\resources\\demo.xml");
            File fileinput = new File("src/main/resources/logback.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fileinput);

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            // 将转换过的xml的String 样式打印到控制台
            System.out.println(writer.toString());
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


//    public static void main(String[] args) {
//        try {
//            // 读取 xml 文件
////            File fileinput = new File("E:\\Documents\\censoft\\demo\\xmldemo\\src\\main\\resources\\demo.xml");
//            File fileinput = new File("src/main/resources/demo.xml");
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
//                    .newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(fileinput);
//
//            DOMSource domSource = new DOMSource(doc);
//            StringWriter writer = new StringWriter();
//            StreamResult result = new StreamResult(writer);
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.transform(domSource, result);
//
//            // 将转换过的xml的String 样式打印到控制台
//            System.out.println(writer.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
