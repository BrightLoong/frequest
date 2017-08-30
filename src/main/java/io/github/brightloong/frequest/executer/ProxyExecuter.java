package io.github.brightloong.frequest.executer;

import com.alibaba.fastjson.JSON;
import io.github.brightloong.frequest.bean.PermitBean;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.helpers.PermitHelper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by BrightLoong on 2017/8/15.
 */
public class ProxyExecuter {

    private static final Log LOGGER = Log.get(ProxyExecuter.class);

    public static TransmissionBean proceed(String requestBeanJson) {
        TransmissionBean transmissionBean = (TransmissionBean) JSON.parse(requestBeanJson);
        String interfaceName = transmissionBean.getInterfaceName();
        String methodName = transmissionBean.getMethodName();
        List<String> argsClassName = transmissionBean.getArgsClassName();
        Class[] clss = new Class[argsClassName.size()];
        Object[] argsValue = transmissionBean.getArgsValue().toArray();
        TransmissionBean resultTransmissionBean = new TransmissionBean();
        Object result = null;
        try {
            for (int i = 0; i < argsClassName.size(); i++) {
                clss[i] = Class.forName(argsClassName.get(i));
            }
            PermitHelper.putRequestPermit(new PermitBean(transmissionBean));

            Class cls = Class.forName(interfaceName);
            Method med = cls.getMethod(methodName,clss);
            result = med.invoke(cls.newInstance(), argsValue);
            resultTransmissionBean.setStatus(GeneralConstants.STATUS_OK);
        } catch (Exception e) {
            resultTransmissionBean.setStatus(GeneralConstants.STATUS_ERR);
            resultTransmissionBean.setMessage(e.getMessage());
            LOGGER.error("执行远程请求方法失败！",e);
        }
        resultTransmissionBean.setResult(result);
        resultTransmissionBean.setUuid(transmissionBean.getUuid());
        return resultTransmissionBean;
    }
}
