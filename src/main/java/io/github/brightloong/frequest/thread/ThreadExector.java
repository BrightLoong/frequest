package io.github.brightloong.frequest.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 获取线程池的类
 * Created by BrightLoong on 2017/8/28.
 */
public class ThreadExector {
    /**CachedThreadPool*/
    private static ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * 返回ExecutorService.
     * @return ExecutorService
     */
    public static ExecutorService getThreadExector () {
        return exec;
    }
}
