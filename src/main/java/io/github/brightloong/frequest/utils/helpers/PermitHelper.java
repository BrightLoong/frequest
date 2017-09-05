package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.bean.PermitBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通行证帮助类，用于控制方法的远程调用和本地调用，避免死循环.
 * Created by BrightLoong on 2017/8/16.
 */
public class PermitHelper {
    private static Map<String, String> requestMap = new ConcurrentHashMap<String, String>();

    /**
     * 设置请求的许可通行证.
     * @param permit PermitBean
     */
    public static void putRequestPermit(PermitBean permit) {
        String json = permit.toJson();
        putRequestPermit(json);
    }

    /**
     * 获取请求的许可通行证.
     * @param permit PermitBean
     * @return true or false
     */
    public static boolean getRequestPermit(PermitBean permit) {
        String json = permit.toJson();
        return getRequestPermit(json);
    }

    /**
     * 设置请求的许可通行证.
     * @param content 转为String的PermitBean
     */
    public static void putRequestPermit(String content) {
        requestMap.put(content, content);
    }

    /**
     * 获取请求的许可通行证.
     * @param content 转为String的PermitBean
     * @return true or false
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
