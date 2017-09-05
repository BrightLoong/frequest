package io.github.brightloong.frequest.exception;

/**
 * 工具异常类
 * Created by BrightLoong on 2017/8/16.
 */
public class FrequsetException extends Exception {
    /**
     * 无参构造器.
     */
    public FrequsetException() {
        super();
    }

    /**
     * 参数构造器.
     * @param message 信息
     */
    public FrequsetException(String message) {
        super(message);
    }

    /**
     * 带参数的构造器.
     * @param message 信息
     * @param e 异常
     */
    public FrequsetException(String message, Throwable e) {
        super(message, e);
    }
}
