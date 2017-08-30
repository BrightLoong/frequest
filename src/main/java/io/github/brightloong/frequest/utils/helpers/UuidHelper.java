package io.github.brightloong.frequest.utils.helpers;

import io.github.brightloong.frequest.constants.GeneralConstants;

import java.util.UUID;

/**
 * 用于生成uuid的帮助类
 * Created by BrightLoong on 2017/8/15.
 */
public class UuidHelper {
    public static String getUuid() {
        return UUID.randomUUID().toString().toUpperCase().replace(GeneralConstants.HYPHEN,GeneralConstants.BLANK);
    }
}
