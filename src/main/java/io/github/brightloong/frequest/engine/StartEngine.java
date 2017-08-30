package io.github.brightloong.frequest.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.config.NormalConfig;
import io.github.brightloong.frequest.config.PathConfig;
import io.github.brightloong.frequest.config.PathConfigLoader;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.exception.NoPathConfigException;
import io.github.brightloong.frequest.executer.ProxyExecuter;
import io.github.brightloong.frequest.file.FileManager;
import io.github.brightloong.frequest.file.observer.RequestFileHandle;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.thread.ThreadManager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BrightLoong on 2017/8/15.
 */
public class StartEngine {
    private static final Log LOGGER = Log.get(StartEngine.class);

    /**客户端的clientFileManager*/
    private static FileManager clientFileManager;

    /**客户端是否开启代理功能*/
    private static boolean isProxy = false;

    /**开启引擎*/
    public static void start() {
        //打开fastjson的autotype功能
        ParserConfig.getGlobalInstance().addAccept(GeneralConstants.TRANSMISSIONBEAN_PATH);
        //是否是远端，如果是远端开启文件扫描功能
        boolean isRemote = NormalConfig.getInstance().getIsRemote();
        String configPath = NormalConfig.getInstance().getXmlConfigPath();
        //获取配置文件信息
        List<PathConfig> pathConfigs = null;
        try {
            pathConfigs = PathConfigLoader.getInstance().parsePathConfig(configPath).getPathConfigs();
        } catch (NoPathConfigException e) {
            LOGGER.error("加载发送接收目录配置文件出错", e);
            throw new RuntimeException();
        }
        if (isRemote) {
            LOGGER.info("==========开启文件扫描引擎==========");
            //可能会响应多个目录
            for (PathConfig pathConfig : pathConfigs) {
                FileManager serviceFileManager = new FileManager(pathConfig);
                serviceFileManager.addObserver(new RequestFileHandle());
                ThreadManager serviceThreadManager = new ThreadManager(serviceFileManager);
                serviceThreadManager.beginRun();
            }
        } else {
            //如果不是远端那么需要需要对方法进行拦截，并且默认取配置项的第一个
            isProxy = true;
            clientFileManager = new FileManager(pathConfigs.get(0));
            ThreadManager serviceThreadManager = new ThreadManager(clientFileManager);
            serviceThreadManager.beginRun();
        }
    }

    public static FileManager getClientFileManager() {
        return clientFileManager;
    }

    public static boolean getIsProxy() {
        return isProxy;
    }

}
