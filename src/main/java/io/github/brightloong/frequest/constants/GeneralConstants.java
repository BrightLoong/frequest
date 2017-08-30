package io.github.brightloong.frequest.constants;

/**
 * Created by BrightLoong on 2017/8/15.
 */
public class GeneralConstants {
    /**连号-*/
    public static final String HYPHEN = "-";

    /**空*/
    public static final String BLANK = "";

    /**左括弧*/
    public static final String LEFT_PARENTHESIS = "(";

     /**右括弧*/
    public static final String RIGHT_PARENTHESIS = ")";

    /**逗号*/
    public static final String COMMA = ",";

    /**执行状态-成功*/
    public static final String STATUS_OK = "OK";

    /**执行状态-失败*/
    public static final String STATUS_ERR = "ERROR";

    /**默认休眠时间1000ms*/
    public static final int DEFAULT_SLEEP_TIME = 100;

    /**文件请求类型*/
    public static final String REQUEST_TYPE_REQUEST = "request";

    /**文件请求结果*/
    public static final String REQUEST_TYPE_RESULT = "result";

    /**xml节点config*/
    public static final String XML_NODE_CONFIG = "config";

    /**xml节点send-path*/
    public static final String XML_NODE_SEND_PATH = "send-path";

    /**xml节点receive-path*/
    public static final String XML_NODE_RECEIVE_PATH = "receive-path";

    /**保护线程再间隔时间大于10000ms时重启文件扫描线程*/
    public static final long PROTECT_WAIT_TIME = 10000;

    /**编码格式utf-8*/
    public static final String CODING_UTF8 = "UTF-8";

    /**transmissionBean的包路径*/
    public static final String TRANSMISSIONBEAN_PATH = "io.github.brightloong.frequest.bean.TransmissionBean";

    /**默认等待的次数*/
    public static final int DEFAULT_WAIT_COUNT = 300;

    /**数字10*/
    public static final int NUM_TEN = 10;
}
