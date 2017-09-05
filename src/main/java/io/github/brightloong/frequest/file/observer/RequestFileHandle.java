package io.github.brightloong.frequest.file.observer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.executer.ProxyExecuter;
import io.github.brightloong.frequest.file.AbstractFileHandleObserver;
import io.github.brightloong.frequest.file.FileManager;
import io.github.brightloong.frequest.utils.helpers.FileHelper;

/**
 * request类文件请求观察者实现
 * Created by BrightLoong on 2017/8/28.
 */
public class RequestFileHandle extends AbstractFileHandleObserver {

    /**
     * 对请求文件进行处理，调用方法，并返回结果文件.
     * @param uuid uuid
     * @param fileName 文件名
     * @param fileManager 文件管理实例
     */
    @Override
    public void update(String uuid, String fileName, FileManager fileManager) {
        String[] properties = FileHelper.getPropertiesByFileName(fileName);
        if (GeneralConstants.REQUEST_TYPE_REQUEST.equals(properties[0])) {
            //移除处理的文件
            fileManager.removeCompletFile(uuid);
            String beanJson = fileManager.receiveJson(fileName);
            TransmissionBean transmissionBean = ProxyExecuter.proceed(beanJson);
            fileManager.generateFileByJsonString(GeneralConstants.REQUEST_TYPE_RESULT,transmissionBean.getUuid(),
                    JSON.toJSONString(transmissionBean, SerializerFeature.WriteClassName));

        }
    }
}
