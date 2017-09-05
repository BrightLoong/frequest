package io.github.brightloong.frequest.executer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.brightloong.frequest.bean.PermitBean;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.helpers.PermitHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对远端的请求文件进行处理的类
 * Created by BrightLoong on 2017/8/15.
 */
public class ProxyExecuter {

    /**日志*/
    private static final Log LOGGER = Log.get(ProxyExecuter.class);

    /**
     * 基本类型对应的class映射
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, Class> basicClassMap = new HashMap<String, Class>() {
        private static final long serialVersionUID = 1L;
        {
            put("byte", byte.class);
            put("short", short.class);
            put("int", int.class);
            put("long", long.class);
            put("float", float.class);
            put("double", double.class);
            put("boolean", boolean.class);
            put("char", char.class);
        }
    };

    /**
     * 解析json字符串，获取方法调用信息，利用反射机制调用被拦截的方法.
     * @param requestBeanJson 请求数据json
     * @return TransmissionBean传输bean
     */
    @SuppressWarnings("unchecked")
    public static TransmissionBean proceed(String requestBeanJson) {
        TransmissionBean transmissionBean = (TransmissionBean) JSON.parse(requestBeanJson);
        String interfaceName = transmissionBean.getInterfaceName();
        String methodName = transmissionBean.getMethodName();
        List<String> argsClassName = transmissionBean.getArgsClassName();
        Class[] clss = new Class[argsClassName.size()];
        Object[] argsValue = transmissionBean.getArgsValue().toArray();
        TransmissionBean resultTransmissionBean = new TransmissionBean();
        Object result = null;

        //处理方法的参数类型和参数值
        try {
            for (int i = 0; i < argsClassName.size(); i++) {
                String argClassName = argsClassName.get(i);
                //如果参数时数组类型需要单独进行处理
                if (isArray(argClassName)) {
                    clss[i] = handleClassAboutArray(argClassName);
                    argsValue[i] = handleArgValueAboutArray(argClassName, argsValue[i]);
                } else {
                    clss[i] = handleClassNotArray(argClassName);
                }
            }
            PermitHelper.putRequestPermit(new PermitBean(transmissionBean));

            //利用反射调用方法
            Class cls = Class.forName(interfaceName);
            Method med = cls.getMethod(methodName,clss);
            result = med.invoke(cls.newInstance(), argsValue);
            resultTransmissionBean.setStatus(GeneralConstants.STATUS_OK);
        } catch (Exception e) {
            resultTransmissionBean.setStatus(GeneralConstants.STATUS_ERR);
            resultTransmissionBean.setMessage(getDetailException(e));
            LOGGER.error("远程执行请求方法失败！",e);
        }
        resultTransmissionBean.setResult(result);
        resultTransmissionBean.setUuid(transmissionBean.getUuid());
        return resultTransmissionBean;
    }

    /**
     * 处理数组类型的参数，因为从json字符串中获取到的数组会转为jsonArray的类型，需要进行处理.
     * @param className String类型的className
     * @param o 要处理的参数
     * @return 返回处理后的参数
     */
    private static Object handleArgValueAboutArray(String className, Object o) throws ClassNotFoundException {
        Object array = getArrayInstanceByClassNameAndArg(className, o);
        parseJsonArray(o, array);
        return array;
    }

    /**
     * 对jsonArray进行解析，对将argValue中的值设置到array中.
     * @param argValue JSONArray的值
     * @param array 实际的数组
     */
    private static void parseJsonArray(Object argValue, Object array) {
        parseJsonArray(argValue, array, null, 0);
    }

    /**
     * 对jsonArray进行解析，对将argValue中的值设置到array中.
     * @param argValue JSONArray的值
     * @param array 实际的数组
     * @param lastArray 使用Arrary.get获取到的上一次的Array
     * @param index 要设置值的index
     */
    private static void  parseJsonArray(Object argValue, Object array, Object lastArray, int index) {
        JSONArray tempArray;
        if (!argValue.getClass().equals(JSONArray.class)) {
            Array.set(lastArray,index , argValue);
            return;
        }
        tempArray = (JSONArray)argValue;
        for (int i = 0; i < tempArray.size(); i++) {
            Object arrayTemp = Array.get(array, i);
            parseJsonArray(tempArray.get(i), arrayTemp, array, i);
        }
    }

    /**
     * 根据拿到的字符串判断是否是数组类型，判断是否包含[]符号.
     * @param str 要判断的字符串
     * @return 返回true or false
     */
    private static boolean isArray(String str) {
        return str.contains(GeneralConstants.LEFT_SQUARE_BRACKETS)
                && str.contains(GeneralConstants.RIGHT_SQUARE_BRACKETS);
    }

    /**
     * 利用Array反射处理类型是数组的Class.
     * @param className String类型的className
     * @return 返回得到的Class
     * @throws ClassNotFoundException 异常信息
     */
    private static Class handleClassAboutArray(String className) throws ClassNotFoundException {
        return getArrayInstanceByClassName(className).getClass();
    }

    /**
     * 根据className，利用array的反射创建一个实例.
     * @param className String类型的className
     * @return 获得一个array的实例
     * @throws ClassNotFoundException 异常信息
     */
    private static Object getArrayInstanceByClassName(String className) throws ClassNotFoundException {
        Class cls = getArrayBasicClassByClassName(className);
        //利用Array反射创建一个实例，使用getClass获取Class信息
        return Array.newInstance(cls, getDimsByClassName(className));
    }

    /**
     *  获取传入的String类型的className的Class信息，如果是数组，会进行截取获取基本的Class信息.
     * @param className String类型的className
     * @return 返回得到的Class
     * @throws ClassNotFoundException 异常信息
     */
    private static Class getArrayBasicClassByClassName(String className) throws ClassNotFoundException {
        //获取数组的基本类型
        String classNameTemp = className.substring(0, className.indexOf(GeneralConstants.LEFT_SQUARE_BRACKETS));
        return handleClassNotArray(classNameTemp);
    }

    /**
     * 根据String类型的className和参数值，获取数组的维度和长度，返回对应的数组.
     * @param className String类型的className
     * @param argValue 对应的参数值
     * @return 数组
     * @throws ClassNotFoundException 异常信息
     */
    private static Object getArrayInstanceByClassNameAndArg(String className, Object argValue)
            throws ClassNotFoundException {
        Object temp = argValue;
        JSONArray tempArray;
        List<Integer> dimsInf = new ArrayList<Integer>();
        while (temp.getClass().equals(JSONArray.class)) {
            tempArray = (JSONArray)temp;
            dimsInf.add(tempArray.size());
            temp = tempArray.get(0);
        }
        int[] dims = new int[dimsInf.size()];
        for (int i = 0; i < dimsInf.size(); i++) {
            dims[i] = dimsInf.get(i);
        }
        Class cls = getArrayBasicClassByClassName(className);
        return Array.newInstance(cls, dims);
    }

    /**
     * 同过className判断是几维数组,并构建参数dims
     * @param className String类型的className
     * @return 返回dims的数组
     */
    private static int[] getDimsByClassName(String className) {
        int[] dims = new int[getNumOfSquareBracets(className)];
        for (int i = 0; i < dims.length; i++) {
            dims[i] = 0;
        }
        return dims;
    }

    /**
     * 返回[的个数
     * @param className String类型的className
     * @return [个数
     */
    private static int getNumOfSquareBracets(String className) {
        return StringUtils.countMatches(className, GeneralConstants.LEFT_SQUARE_BRACKETS);
    }


    /**
     * 将报错的堆栈信息转换为String字符串
     * @param e 异常Exception
     * @return 转为String的异常信息
     */
    private static String getDetailException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    /**
     * 处理不是数组类型的className
     * @param className class的String
     * @return 返回得到的Class信息
     * @throws ClassNotFoundException 抛出异常由调用者捕获
     */
    private static Class handleClassNotArray(String className) throws ClassNotFoundException {
        Class cls = basicClassMap.get(className);
        //如果在试基础类型的则不为null,否则用Class.forName的方式获取
        cls = cls == null ? Class.forName(className) : cls;
        return cls;
    }
}
