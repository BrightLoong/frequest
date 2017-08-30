package io.github.brightloong.frequest.exception;

/**
 * Created by BrightLoong on 2017/8/28.
 */
public class NoPathConfigException extends Exception {
        /**
         * 无参构造器
         */
        public NoPathConfigException() {
            super();
    }
        /**
         * 带参数的构造器
         * @param message
         * @param e
         */
    public NoPathConfigException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 仅仅msg的构造器
     * @param message
     */
    public NoPathConfigException(String message) {
        super(message);
    }
}
