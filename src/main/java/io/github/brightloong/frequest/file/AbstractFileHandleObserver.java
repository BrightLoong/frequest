package io.github.brightloong.frequest.file;

import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.thread.ThreadExector;

import java.util.concurrent.ExecutorService;

/**
 * 对发现文件进行处理的观察者抽象类
 * Created by BrightLoong on 2017/8/28.
 */
public abstract class AbstractFileHandleObserver {

    /**日志*/
    private static final Log LOGGER = Log.get(AbstractFileHandleObserver.class);

    /**
     * 通知方法.
     * @param uuid uuid
     * @param fileName 文件名
     * @param fileManager 文件管理实例
     */
    public void notify(final String uuid, final String fileName, final FileManager fileManager) {
        //新开分支执行
        ExecutorService exec = ThreadExector.getThreadExector();
        exec.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("执行update : " + fileName);
                update(uuid, fileName, fileManager);
            }
        });
    }

    /**
     * 更新方法.
     * @param uuid uuid
     * @param fileName 文件名
     * @param fileManager 文件管理实例
     */
    public abstract void update(String uuid, String fileName, FileManager fileManager);
}
