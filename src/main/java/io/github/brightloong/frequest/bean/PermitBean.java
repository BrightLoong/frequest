package io.github.brightloong.frequest.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.utils.helpers.JoinPointHelper;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BrightLoong on 2017/8/16.
 */
public class PermitBean {
    private String interfaceName; // 接口名称
    private String className; // 类名称
    private String methodName; // 方法名称
    private List<String> argsClassName; // 参数类名称
    private List<Object> argsValue; // 参数值

    /**
     * 空构造方法
     */
    public PermitBean() {}

    /**
     * 使用KwlRequestBean的属性的构造方法
     * @param transmissionBean
     */
    public PermitBean(TransmissionBean transmissionBean) {
        interfaceName = transmissionBean.getInterfaceName();
        className = transmissionBean.getClassName();
        methodName = transmissionBean.getMethodName();
        argsClassName = transmissionBean.getArgsClassName();
        argsValue = transmissionBean.getArgsValue();
    }

    /**
     * 使用ProceedingJoinPoint的属性的构造方法
     * @param joinPoint
     */
    public PermitBean(ProceedingJoinPoint joinPoint) {
        interfaceName = joinPoint.getSignature().getDeclaringTypeName();
        className = joinPoint.getTarget().getClass().getName();
        methodName = joinPoint.getSignature().getName();
        argsClassName = getClassNameFromJoinPoint(joinPoint);
        argsValue = getListFromArray(joinPoint.getArgs());
    }

    /**
     * 输出为json
     * @return
     */
    public String toJson() {
        String json = JSON.toJSONString(this, SerializerFeature.WriteClassName);
        return json;
    }

    /**
     * 计算方法的参数类型 用于服务端的方法反射
     * @param joinPoint
     * @return
     */
    private List<String> getClassNameFromJoinPoint(ProceedingJoinPoint joinPoint) {
        List<String> classNameList = new ArrayList<String>();
        String longName = JoinPointHelper.getSignature(joinPoint).toLongString();
        longName = longName.substring(longName.indexOf("(") + 1);
        longName = longName.substring(0, longName.indexOf(")"));
        if(longName.length()>0) {
            String[] classNames = longName.split(",");
            for(int i=0;i<classNames.length;i++) {
                classNameList.add(classNames[i]);
            }
        }
        return classNameList;
    }

    /**
     * 用于将数组转化成List在JSON转化中不能出现数组
     * @param objects
     * @return
     */
    private List<Object> getListFromArray(Object[] objects) {
        List<Object> objectList = new ArrayList<Object>();
        for(int i=0;i<objects.length;i++) {
            objectList.add(objects[i]);
        }
        return objectList;
    }

    /* geter 和 setter 方法*/
    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getArgsClassName() {
        return argsClassName;
    }

    public void setArgsClassName(List<String> argsClassName) {
        this.argsClassName = argsClassName;
    }

    public List<Object> getArgsValue() {
        return argsValue;
    }

    public void setArgsValue(List<Object> argsValue) {
        this.argsValue = argsValue;
    }


}
