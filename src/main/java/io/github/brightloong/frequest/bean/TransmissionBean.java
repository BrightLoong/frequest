package io.github.brightloong.frequest.bean;

import java.util.List;
/**
 * 用于传输信息的bean
 * Created by BrightLoong on 2017/8/15.
 */
public class TransmissionBean {
    /**请求标识*/
    private String uuid;
    private String interfaceName;
    private String className;
    private String methodName;
    private List<String> argsClassName;
    private List<Object> argsValue;
    private String clientName;
    private Object result;
    private String status;
    private String message;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
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
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
