package io.github.brightloong.frequest.config;

/**
 * Created by BrightLoong on 2017/8/15.
 */
public class PathConfig {
    /**发起请求的目录或者存放请求文件的目录*/
    private String sendPath;

    /**发起请求后接收结果的目录或者存放结果文件的目录*/
    private String receivePath;

    /**唯一实例*/
    private static final PathConfig pathConfig = new PathConfig();

    /**
     * 构造函数
     */
    public PathConfig() {
    }

    /**
     * 获取唯一实例
     * @return PathConfig
     */
    public static PathConfig getInstance() {
        return pathConfig;
    }

    /**
     * 设置发起请求的目录或者存放请求文件的目录
     * @param sendPath 发起请求的目录或者存放请求文件的目录
     * @return PathConfig
     */
    public PathConfig setSendPath(String sendPath) {
        this.sendPath = sendPath;
        return this;
    }

    /**
     * 设置发起请求后接收结果的目录或者存放结果文件的目录
     * @param receivePath 发起请求后接收结果的目录或者存放结果文件的目录
     * @return PathConfig
     */
    public PathConfig setReceivePath(String receivePath) {
        this.receivePath = receivePath;
        return this;
    }

    /**
     * 返回发起请求的目录或者存放请求文件的目录
     * @return 发起请求的目录或者存放请求文件的目录
     */
    public String getSendPath() {
        return this.sendPath;
    }

    /**
     * 返回发起请求后接收结果的目录或者存放结果文件的目录
     * @return 发起请求后接收结果的目录或者存放结果文件的目录
     */
    public String getReceivePath() {
        return this.receivePath;
    }

    @Override
    public String toString() {
        return "PathConfig{" +
                "sendPath='" + sendPath + '\'' +
                ", receivePath='" + receivePath + '\'' +
                '}';
    }
}
