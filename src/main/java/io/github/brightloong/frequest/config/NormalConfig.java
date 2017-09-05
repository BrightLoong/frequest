package io.github.brightloong.frequest.config;

import io.github.brightloong.frequest.constants.GeneralConstants;

/**
 * 配置类.
 * Created by BrightLoong on 2017/8/21.
 */
public class NormalConfig {
    /**线程休眠时间,每间隔多久扫描一次文件变动*/
    private int sleepTime;

    /**等待接收文件的次数，并且每次休眠sleepTime的时间*/
    private int waitCount;

    /**发送接收目录xml配置文件的目录*/
    private String xmlConfigPath;

    /**是否远端，如果不是则将本地方法拦截调用远端方法*/
    private boolean isRemote;

    /**唯一实例*/
    private static final NormalConfig normalConfig = new NormalConfig();

    /**
     * 私有构造函数
     */
    private NormalConfig() {
    }

    /**
     * 获取唯一实例
     * @return
     */
    public static NormalConfig getInstance() {
        return normalConfig;
    }

    /**
     * 设置线程休眠时间,单位ms
     * @param sleepTime
     * @return
     */
    public NormalConfig setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    /**
     * 设置是否进行代理
     * @param isRemote
     * @return
     */
    public NormalConfig setIsRemote(boolean isRemote) {
        this.isRemote = isRemote;
        return this;
    }

    /**
     * 设置配置xml路径
     * @param xmlConfigPath
     * @return
     */
    public NormalConfig setXmlConfigPath(String xmlConfigPath) {
        this.xmlConfigPath = xmlConfigPath;
        return this;
    }

    /**
     * 获取配置xml路径
     * @return
     */
    public String getXmlConfigPath() {
        return this.xmlConfigPath;
    }

    /**
     * 返回休眠时间，如果为0，返回默认值1000ms
     * @return
     */
    public int getSleepTime() {
        return this.sleepTime == 0 ? GeneralConstants.DEFAULT_SLEEP_TIME : this.sleepTime;
    }

    /**
     * @return
     */
    public boolean getIsRemote() {
        return this.isRemote;
    }

    public int getWaitCount() {
        return this.waitCount == 0 ? GeneralConstants.DEFAULT_WAIT_COUNT : this.waitCount;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }
}
