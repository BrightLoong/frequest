package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.log.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * 文件操作帮助类.
 * Created by BrightLoong on 2017/8/30.
 */
public class FileHelper {
    private final  static Log LOGGER = Log.get(FileHelper.class);

    /**
     * 更具文件名获取文件的一些属性.
     * @return 返回属性数组
     */
    public static String[] getPropertiesByFileName(String fileName) {
        String[] properties = fileName.split(GeneralConstants.HYPHEN);
        return properties;
    }

    /**
     * 循环删除文件10次.
     * @param file 要删除的文件
     */
    public static void delFile(File file) {
        boolean isDel = FileUtils.deleteQuietly(file);
        int delCount = 0;
        while (!isDel) {
            delCount ++;
            LOGGER.info("删除文件" + file.getName() + "失败！再次删除...");
            if (delCount == GeneralConstants.NUM_TEN) {
                LOGGER.warn("文件" + file.getName() + "10次没有删除");
                break;
            }
        }
    }
}
