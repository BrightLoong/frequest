package io.github.brightloong.frequest.config;

/**
 * 路径信息类.
 * Created by BrightLoong on 2017/8/15.
 */
public class PathConfig {
    /**发起请求的目录或者存放请求文件的目录*/
    private String sendPath;

    /**发起请求后接收结果的目录或者存放结果文件的目录*/
    private String receivePath;

    /**
     * 构造函数.
     */
    public PathConfig() {
    }

    /**
     * 设置发起请求的目录或者存放请求文件的目录.
     * @param sendPath 发起请求的目录或者存放请求文件的目录
     * @return PathConfig PathConfig
     */
    public PathConfig setSendPath(String sendPath) {
        this.sendPath = sendPath;
        return this;
    }

    /**
     * 设置发起请求后接收结果的目录或者存放结果文件的目录.
     * @param receivePath 发起请求后接收结果的目录或者存放结果文件的目录
     * @return PathConfig PathConfig
     */
    public PathConfig setReceivePath(String receivePath) {
        this.receivePath = receivePath;
        return this;
    }

    /**
     * 返回发起请求的目录或者存放请求文件的目录.
     * @return 发起请求的目录或者存放请求文件的目录
     */
    public String getSendPath() {
        return this.sendPath;
    }

    /**
     * 返回发起请求后接收结果的目录或者存放结果文件的目录.
     * @return 发起请求后接收结果的目录或者存放结果文件的目录
     */
    public String getReceivePath() {
        return this.receivePath;
    }

    /**
     * 覆盖toString()方法.
     * @return pathConfig信息
     */
    @Override
    public String toString() {
        return "PathConfig{" +
                "sendPath='" + sendPath + '\'' +
                ", receivePath='" + receivePath + '\'' +
                '}';
    }
}
