package io.github.brightloong.frequest.config;

import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.exception.NoPathConfigException;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.helpers.XmlParseHelper;
import org.dom4j.Document;
import org.dom4j.Node;
import java.util.List;

/**
 * Created by BrightLoong on 2017/8/25.
 */
public class PathConfigLoader {
    private List<PathConfig> pathConfigs;
    private static final Log LOGGER = Log.get(PathConfigLoader.class);
    private static PathConfigLoader pathConfigLoader = new PathConfigLoader();

    /**
     * 私有无参构造函数
     */
    private PathConfigLoader() {
    }

    /**
     * 获取唯一实例
     * @return
     */
    public static PathConfigLoader getInstance() {
        return pathConfigLoader;
    }

    /**
     * 解析pathConfig配置
     * @param xmlConfigPath 配置文件路径
     * @return this
     * @throws NoPathConfigException
     */
    public PathConfigLoader parsePathConfig(String xmlConfigPath) throws NoPathConfigException {
        Document document = XmlParseHelper.getDocument(xmlConfigPath);
        @SuppressWarnings("unchecked")
        List<Node> nodes = XmlParseHelper.getAndcheckedNodes(document.getRootElement(), GeneralConstants.XML_NODE_CONFIG);
        pathConfigs = XmlParseHelper.getAndCheckedPathConfigs(nodes);
        LOGGER.info("pathConfig配置：" + pathConfigs);
        return this;
    }

    /**
     * 获取pathConfigs配置
     * @return
     */
    public List<PathConfig> getPathConfigs() {
        return this.pathConfigs;
    }
}
