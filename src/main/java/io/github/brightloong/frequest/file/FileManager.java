package io.github.brightloong.frequest.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.brightloong.frequest.bean.TransmissionBean;
import io.github.brightloong.frequest.config.NormalConfig;
import io.github.brightloong.frequest.config.PathConfig;
import io.github.brightloong.frequest.constants.GeneralConstants;
import io.github.brightloong.frequest.exception.FrequsetException;
import io.github.brightloong.frequest.log.Log;
import io.github.brightloong.frequest.utils.ThreadUtils;
import io.github.brightloong.frequest.utils.helpers.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BrightLoong on 2017/8/15.
 */
public class FileManager {
    private static final Log LOGGER = Log.get(FileManager.class);

    /**
     * 发送文件目录
     */
    private String sendPath;

    /**
     * 接收文件目录
     */
    private String receivePath;

    /**临时目录*/
    private static final String tempPath = System.getProperty("java.io.tmpdir");

    private static final long sleepTime = NormalConfig.getInstance().getSleepTime();

    private List<AbstractFileHandleObserver> fileHandleObservers = new ArrayList<AbstractFileHandleObserver>();

    /**
     * 记录有效的变动文件
     */
    private Map<String, String> completFiles = new ConcurrentHashMap<String, String>();

    /**
     * 带参数的构造器，用于初始化路径信息
     * @param pathConfig
     */
    public FileManager(PathConfig pathConfig) {
        this.sendPath = pathConfig.getSendPath();
        this.receivePath = pathConfig.getReceivePath();
    }

    /**
     * 生成文件到指定的目录
     * @param  type 文件类型
     * @param uuid uuid
     * @param jsonString 要写入的jsonString
     * @throws IOException
     */
    public  void generateFileByJsonString(String type, String uuid, String jsonString) {
        String commonName = type + GeneralConstants.HYPHEN + uuid;
        //生成临时文件
        File tempFile = new File(tempPath + commonName);
        try {
            FileUtils.writeStringToFile(tempFile,jsonString, GeneralConstants.CODING_UTF8);
            //获取文件大小
            long size = tempFile.length();
            String sendFilePath = sendPath + File.separator + commonName + GeneralConstants.HYPHEN + size;
            FileUtils.copyFile(tempFile, new File(sendFilePath));
            //移动到发送目录
        } catch (IOException e) {
            LOGGER.error("生成文件" + commonName + "出错" );
        }
        //删除临时文件
        FileHelper.delFile(tempFile);
    }

    /**
     * 扫描指定目录的文件变动
     * @return
     */
    public  void scanReceiveFileFolder() {
        File scanDirectory = new File(receivePath);
        String[] fileNames = scanDirectory.list();
        if (fileNames == null || fileNames.length == 0) {
            return;
        }
        for (int i = 0; i < fileNames.length; i++) {
            if (!isValidFile(fileNames[i])) {
                continue;
            }
            String[] properties = FileHelper.getPropertiesByFileName(fileNames[i]);
            String uuid = properties[1];
            String tempFilePath = tempPath + fileNames[i];
            File file = new File(receivePath + File.separator + fileNames[i]);
            try {
                FileUtils.copyFile(file, new File(tempFilePath));
            } catch (IOException e) {
                LOGGER.error("接收文件" + file.getName() + "失败！");
            }
            //添加文件到完成map里面
            this.addCompletFile(uuid, fileNames[i]);
            //删除接收到的文件
            FileHelper.delFile(file);
            //通知观察者进行处理
            update(uuid, fileNames[i]);
        }
    }



    public  String receiveJson(String fileName)  {
        String receiveFileName = tempPath + fileName;
        String resultJson = null;
        try {
            resultJson = FileUtils.readFileToString(new File(receiveFileName),GeneralConstants.CODING_UTF8);
        } catch (IOException e) {
            LOGGER.error("读取文件" + fileName + "失败");
        }
        FileHelper.delFile(new File(receiveFileName));
        return resultJson;
    }

    /**
     * 等待接收远程返回的结果
     * @param uuid
     * @return
     */
    public Object receiveResult(String uuid) throws FrequsetException {
        int waitCount = NormalConfig.getInstance().getWaitCount();
        String resultJson = "";
        String fileName = "";
        //File file = new File(fileName);
        File file = null;
        for (int i = 0; i < waitCount; i++) {
            fileName = completFiles.get(uuid);
            if (StringUtils.isBlank(fileName)) {
                ThreadUtils.sleepMillis(sleepTime);
                continue;
            }
            String filePath = tempPath + fileName;
            file = new File(filePath);
            try {
                resultJson = FileUtils.readFileToString(file, GeneralConstants.CODING_UTF8);
                break;
            } catch (IOException e) {
                LOGGER.error("从" + fileName + "获取结果失败");
            }
        }
        if (StringUtils.isBlank(resultJson)) {
            long waitTime = waitCount * sleepTime;
            throw  new FrequsetException("在" + waitTime + "ms内获取结果失败");
        }
        this.removeCompletFile(fileName);
        FileHelper.delFile(file);
        TransmissionBean transmissionBean = (TransmissionBean)JSON.parse(resultJson);
        if (GeneralConstants.STATUS_ERR.equals(transmissionBean.getStatus())) {
            throw new FrequsetException("从远程获取结果失败" + transmissionBean.getMessage());
        }
        return ((TransmissionBean)JSON.parse(resultJson)).getResult();
    }

    public void addObserver(AbstractFileHandleObserver fileHandleObserver) {
        fileHandleObservers.add(fileHandleObserver);
    }

    /**
     * 根据文件名后面的大小片段是否是有效文件
     * @param fileName 文件名
     * @return
     */
    private boolean isValidFile(String fileName) {
        String[] propertise = FileHelper.getPropertiesByFileName(fileName);
        File file = new File(receivePath + File.separator + fileName);
        if (propertise.length < 3) {
            return false;
        }
        if (propertise[2].equals(Long.toString(file.length()))) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param uuid
     * @param fileName
     */
    private void update(String uuid, String fileName) {
        for (AbstractFileHandleObserver fileHandleObserver : fileHandleObservers) {
            fileHandleObserver.notify(uuid, fileName, this);
        }
    }

    public void addCompletFile(String uuid, String fileName) {
        completFiles.put(uuid, fileName);
    }

    public void removeCompletFile(String uuid) {
        completFiles.remove(uuid);
    }
}
