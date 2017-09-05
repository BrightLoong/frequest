package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.constants.GeneralConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于获取joinPoint一些信息的帮助类.
 * Created by BrightLoong on 2017/8/15.
 */
public class JoinPointHelper {
    /**
     * 获取接口信息.
     * @param joinPoint JoinPoint
     * @return 接口信息
     */
    public static String getInterfaceName(JoinPoint joinPoint) {
        return getSignature(joinPoint).getDeclaringTypeName();
    }

    /**
     * 获取Signature.
     * @param joinPoint JoinPoint
     * @return Signature.
     */
    public static Signature getSignature(JoinPoint joinPoint) {
        return joinPoint.getSignature();
    }

    /**
     * 获取方法ClassName.
     * @param joinPoint JoinPoint
     * @return ClassName
     */
    public static String getClassName(JoinPoint joinPoint) {
        return getTarget(joinPoint).getClass().getName();
    }

    /**
     * 获取Target。
     * @param joinPoint JoinPoint
     * @return Target
     */
    public static Object getTarget(JoinPoint joinPoint) {
        return joinPoint.getTarget();
    }

    /**
     * 获取方法名.
     * @param joinPoint JoinPoint
     * @return 方法名
     */
    public static String getMethodName(JoinPoint joinPoint) {
        return getSignature(joinPoint).getName();
    }

    /**
     * 获取方法参数Class.
     * @param joinPoint JoinPoint
     * @return 参数Class list
     */
    public static List<String> getArgsClassName(JoinPoint joinPoint) {
        List<String> argsClassName = new ArrayList<String>();
        String longName = getSignature(joinPoint).toLongString();
        longName = longName.substring(longName.indexOf(GeneralConstants.LEFT_PARENTHESIS) + 1);
        longName = longName.substring(0, longName.indexOf(GeneralConstants.RIGHT_PARENTHESIS));
        if (longName.length() > 0) {
            String[] classNames = longName.split(GeneralConstants.COMMA);
            for (int i = 0; i < classNames.length; i++) {
                argsClassName.add(classNames[i].trim());
            }
        }
        return argsClassName;
    }

    /**
     * 获取方法参数.
     * @param joinPoint JoinPoint
     * @return 参数list
     */
    public static List<Object> getArgsValue(JoinPoint joinPoint) {
        return Arrays.asList(joinPoint.getArgs());
    }
}
