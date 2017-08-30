package io.github.brightloong.frequest.file.observer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.executer.ProxyExecuter;
import io.github.brightloong.frequest.file.AbstractFileHandleObserver;
import io.github.brightloong.frequest.file.FileManager;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.helpers.FileHelper;

import java.io.IOException;

/**
 * Created by BrightLoong on 2017/8/28.
 */
public class RequestFileHandle extends AbstractFileHandleObserver {

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
