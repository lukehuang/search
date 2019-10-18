package com;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    /* fixme */
//    @Test
    public void test() {
        try {
            org.apache.tika.Tika tika = new Tika();
            InputStream inputStream = new FileInputStream(new File("C:/Users/Administrator/Desktop/test.txt"));
            System.out.println(tika.parseToString(inputStream));
        } catch (IOException | TikaException e) {
            e.printStackTrace();
        }
    }

    /* fixme */
    @Test
    public void test2() {
        try {
            AutoDetectParser parser = new AutoDetectParser(); /* 自动检测文件类型 */
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();

            InputStream inputStream = new FileInputStream(new File("D:/新建文件夹（同步）/文档/Idea/idea使用教程2017-06-01.pdf"));
            parser.parse(inputStream, handler, metadata);
            System.out.println(handler.toString());
        } catch (SAXException | TikaException | IOException e) {
            e.printStackTrace();
        }
    }

}
