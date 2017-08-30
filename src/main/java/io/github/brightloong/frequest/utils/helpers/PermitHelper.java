package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.bean.PermitBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通行证帮助类，用于控制方法的远程调用和本地调用，避免死循环
 * Created by BrightLoong on 2017/8/16.
 */
public class PermitHelper {
    private static Map<String, String> requestMap = new ConcurrentHashMap<String, String>();

    public static void putRequestMap() {

    }

    /**
     * 设置请求的许可通行证
     * @param permit
     */
    public static void putRequestPermit(PermitBean permit) {
        String json = permit.toJson();
        putRequestPermit(json);
    }

    /**
     * 获取请求的许可通行证
     * @param permit
     * @return
     */
    public static boolean getRequestPermit(PermitBean permit) {
        String json = permit.toJson();
        return getRequestPermit(json);
    }

    /**
     * 设置请求的许可通行证
     * @param content
     */
    public static void putRequestPermit(String content) {
        requestMap.put(content, content);
    }

    /**
     * 获取请求的许可通行证
     * @param content
     * @return
     */
    public static boolean getRequestPermit(String content) {
        if(requestMap.get(content) != null ) {
            requestMap.remove(content);
            return true;
        } else {
            return false;
        }
    }
}
