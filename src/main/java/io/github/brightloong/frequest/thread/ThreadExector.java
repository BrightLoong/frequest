package io.github.brightloong.frequest.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BrightLoong on 2017/8/28.
 */
public class ThreadExector {
    private static ExecutorService exec = Executors.newCachedThreadPool();
    public static ExecutorService getThreadExector () {
        return exec;
    }
}
