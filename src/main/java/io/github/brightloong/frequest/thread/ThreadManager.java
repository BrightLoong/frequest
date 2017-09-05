package io.github.brightloong.frequest.thread;

import io.github.brightloong.frequest.config.NormalConfig;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.file.FileManager;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.ThreadUtils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * 线程管理类.
 * Created by BrightLoong on 2017/8/28.
 */
public class ThreadManager {
    /**日志*/
    private static final Log LOGGER = Log.get(ThreadManager.class);

    /**文件管理类*/
    private FileManager fileManager;

    /**每次休眠时间*/
    private static long sleepTime = NormalConfig.getInstance().getSleepTime();

    /**上次扫描时间*/
    private long lastScanTime;

    /**是否运行*/
    private boolean isRunning = false;

    /**
     * 构造函数.
     * @param fileManager 文件管理类
     */
    public ThreadManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * 开始执行扫描，并启动保护线程
     */
    public synchronized void beginRun() {
        if (!isRunning) {
            isRunning = true;
            //初始化一个时间
            lastScanTime = new Date().getTime();
            autoRun();
            protectThreadRun();
        }
    }

    /**
     * 启动扫描线程
     */
    private void autoRun() {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    LOGGER.info(Thread.currentThread().getName() + "==========文件扫描运行中==========");
                    lastScanTime = new Date().getTime();
                    fileManager.scanReceiveFileFolder();
                    ThreadUtils.sleepMillis(sleepTime);
                }
            }
        });
        exec.shutdown();
    }

    /**
     * 启动保护线程
     */
    private void protectThreadRun() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isRunning && (new Date().getTime() - lastScanTime) > GeneralConstants.PROTECT_WAIT_TIME) {
                        LOGGER.info("扫描线程超过10秒未运行，执行重启");
                        isRunning = false;
                        beginRun();
                    }
                    ThreadUtils.sleepMillis(sleepTime);
                }
            }
        });
        exec.shutdown();
    }
}
