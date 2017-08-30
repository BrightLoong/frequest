package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.config.PathConfig;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.exception.NoPathConfigException;
import io.github.brightloong.frequest.log.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BrightLoong on 2017/8/25.
 */
public class XmlParseHelper {
    private static final Log LOGGER = Log.get(XmlParseHelper.class);

    public static Document getDocument(String xmlPath) throws NoPathConfigException{
         ClassLoader cld = Thread.currentThread().getContextClassLoader();
        InputStream is = cld.getResourceAsStream(xmlPath);
        if (is == null) {
            throw new NoPathConfigException("请检查传入路径配置文件路径是否正确。");
        }
        return getDocument(is);
    }

    public static Document getDocument(File xmlFile) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(xmlFile);
        } catch (DocumentException e) {
            LOGGER.error("解析xml:" + xmlFile.getPath() + "失败！" );
        }
        return document;
    }

    public static Document getDocument(InputStream inputStream) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            LOGGER.error("解析xml失败！" , e);
        }
        return document;
    }

    public static Element getRootElement(String xmlPath) throws NoPathConfigException{
        return getDocument(xmlPath).getRootElement();
    }

    public static Element getElement(Element element, String tag) {
        return element.element(tag);
    }

    public static List<Node> getAndcheckedNodes(Element element, String xPath) throws NoPathConfigException {
        @SuppressWarnings("unchecked")
        List<Node> nodes = element.selectNodes(GeneralConstants.XML_NODE_CONFIG);
        if (CollectionUtils.isEmpty(nodes)) {
            throw new NoPathConfigException("没有获取到指定节点" + xPath + "请检查配置");
        }
        return nodes;
    }

    public static List<PathConfig> getAndCheckedPathConfigs(List<Node> nodes) throws NoPathConfigException {
        List<PathConfig> pathConfigs = new ArrayList<PathConfig>();
        PathConfig pathConfig;
        String sendPath;
        String receivePath;
        for (Node node : nodes) {
             sendPath = node.selectSingleNode(GeneralConstants.XML_NODE_SEND_PATH).getText();
             receivePath = node.selectSingleNode(GeneralConstants.XML_NODE_RECEIVE_PATH).getText();
             if (StringUtils.isEmpty(sendPath) || StringUtils.isEmpty(receivePath)) {
                 throw new NoPathConfigException("获取不到对应的sendPath或receivePath，请检查配置");
             }
             pathConfig = new PathConfig();
             pathConfig.setSendPath(sendPath);
             pathConfig.setReceivePath(receivePath);
             pathConfigs.add(pathConfig);
        }
        return pathConfigs;
    }
}
