package com.jbox.common.base;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by Administrator on 2018/5/13.
 */
public class XmlConfiger {
    private Document root;

    public XmlConfiger(String strFilePath) throws IOException, DocumentException {
        InputStream in = null;

        // 解析xml文档内容
        SAXReader reader = new SAXReader();
        in = new FileInputStream(new File(strFilePath));
        root = reader.read(in);
        in.close();
    }

    public String GetValue(String strKey) {
        String[] arrKey = strKey.split("\\.");
        if (arrKey.length == 0) {
            return null;
        }

        Element node = root.getRootElement();
        if (node.getName().equals(arrKey[0])) {
            if (arrKey.length == 1) {
                return node.getText();
            }
        }

        for (int i=1; i<arrKey.length; i++) {
            String strSubKey = arrKey[i];
            if (strSubKey.length() != 0) {
                List<Element> lstEle = node.elements();
                boolean found = false;
                for (Element e : lstEle) {
                    if (e.getName().equals(strSubKey)) {
                        if ((i+1) == arrKey.length) {
                            return e.getText();
                        }else {
                            node = e;
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    break;
                }
            }else {
                break;
            }
        }

        return null;
    }
}
