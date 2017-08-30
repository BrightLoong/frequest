package io.github.brightloong.frequest.utils;

import io.github.brightloong.frequest.log.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by BrightLoong on 2017/8/17.
 */
public class ThreadUtils {
    private static final Log LOGGER = Log.get(ThreadUtils.class);

    /**
     * 毫秒级休眠
     * @param millis
     */
    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("线程休眠异常！", e);
        }
    }

    /**
     * 秒级休眠
     * @param Seconds
     */
    public static void sleepSeconds(long Seconds) {
        try {
            TimeUnit.SECONDS.sleep(Seconds);
        } catch (InterruptedException e) {
            LOGGER.error("线程休眠异常！", e);
        }
    }
}
