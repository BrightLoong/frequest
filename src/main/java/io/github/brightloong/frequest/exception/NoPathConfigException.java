package io.github.brightloong.frequest.exception;

/**
 * 解析路径配置的异常类
 * Created by BrightLoong on 2017/8/28.
 */
public class NoPathConfigException extends Exception {
        /**
         * 无参构造器.
         */
        public NoPathConfigException() {
            super();
    }
        /**
         * 带参数的构造器.
         * @param message 信息
         * @param e 异常
         */
    public NoPathConfigException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 仅仅msg的构造器.
     * @param message 信息
     */
    public NoPathConfigException(String message) {
        super(message);
    }
}
