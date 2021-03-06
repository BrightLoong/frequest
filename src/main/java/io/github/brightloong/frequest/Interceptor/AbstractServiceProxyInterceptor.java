package io.github.brightloong.frequest.Interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.bean.PermitBean;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.engine.StartEngine;
import io.github.brightloong.frequest.file.FileManager;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.helpers.JoinPointHelper;
import io.github.brightloong.frequest.utils.helpers.PermitHelper;
import io.github.brightloong.frequest.utils.helpers.UuidHelper;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 拦截Service请求
 * Created by BrightLoong on 2017/8/10.
 */
public abstract class AbstractServiceProxyInterceptor {
    /**日志*/
    private static final Log LOGGER = Log.get(AbstractServiceProxyInterceptor.class);

    /**
     * 具体的拦截方法的实现.
     * @param joinPoint ProceedingJoinPoint
     * @return 返回调用结果
     * @throws Throwable 抛出异常
     */
    protected abstract Object serviceAroundImpl(ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     * 对拦截方法根据判断，执行原来的方法，或者执行拦截方法：
     * 1.将拦截到的方法的信息读取出来生成json字符串
     * 2.将json字符串写入文件发送到指定目录
     * 3.等待远端返回的方法执行结果.
     * @param joinPoint
     * @return 拦截方法的返回值
     */
    protected Object serviceAround(ProceedingJoinPoint joinPoint) throws Throwable{
        boolean isProxy = StartEngine.getIsProxy();
        if (isProxy && !PermitHelper.getRequestPermit(new PermitBean(joinPoint))) {
            LOGGER.info("执行远程调用方法");
            return execInterceptMethod(joinPoint);
        }
        LOGGER.info("执行本地调用方法");
        return execOriginalMethod(joinPoint);
    }

    /**
     * 执行原本方法.
     * @param joinPoint ProceedingJoinPoint
     * @return 返回方法调用结果
     * @throws Throwable 抛出异常
     */
    private Object execOriginalMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        return joinPoint.proceed(args);
    }

    /**
     * 执行拦截方法.
     * @param joinPoint ProceedingJoinPoint
     * @return 返回方法调用结果
     * @throws Throwable 抛出异常
     */
    private Object execInterceptMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        FileManager clientFileManager = StartEngine.getClientFileManager();
        TransmissionBean transmissionBean = buildTransmissionBean(joinPoint);
        String uuid = transmissionBean.getUuid();
        String transmissionBeanJson = JSON.toJSONString(transmissionBean, SerializerFeature.WriteClassName);
        //生成文件到指定目录
        clientFileManager.generateFileByJsonString(GeneralConstants.REQUEST_TYPE_REQUEST,uuid,transmissionBeanJson);
        //等待接收结果
        return  clientFileManager.receiveResult(uuid);
    }

    /**
     * 构建joinPoint构建传输bean.
     * @param joinPoint ProceedingJoinPoint
     * @return 返回构建后的传输bean
     */
    private TransmissionBean buildTransmissionBean(ProceedingJoinPoint joinPoint) {
        TransmissionBean transmissionBean = new TransmissionBean();
        String uuid = UuidHelper.getUuid();
        String interfaceName = JoinPointHelper.getInterfaceName(joinPoint);
        String className = JoinPointHelper.getClassName(joinPoint);
        String methodName = JoinPointHelper.getMethodName(joinPoint);
        List<String> argsClassName = JoinPointHelper.getArgsClassName(joinPoint);
        List<Object> argsValue = JoinPointHelper.getArgsValue(joinPoint);

        transmissionBean.setUuid(uuid);
        transmissionBean.setInterfaceName(interfaceName);
        transmissionBean.setClassName(className);
        transmissionBean.setMethodName(methodName);
        transmissionBean.setArgsClassName(argsClassName);
        transmissionBean.setArgsValue(argsValue);
        return transmissionBean;
    }
}
