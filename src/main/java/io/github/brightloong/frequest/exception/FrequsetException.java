package io.github.brightloong.frequest.exception;

/**
 * 文件请求异常类
 * Created by BrightLoong on 2017/8/16.
 */
public class FrequsetException extends Exception {
    /**
     * 无参构造器
     */
    public FrequsetException() {
        super();
    }

    /**
     * 参数构造器
     * @param message
     */
    public FrequsetException(String message) {
        super(message);
    }

    /**
     * 带参数的构造器
     * @param message
     * @param e
     */
    public FrequsetException(String message, Throwable e) {
        super(message, e);
    }
}
