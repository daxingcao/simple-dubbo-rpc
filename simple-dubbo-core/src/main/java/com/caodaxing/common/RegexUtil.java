package com.caodaxing.common;

import java.util.regex.Pattern;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 正则表达式规则类
 */
public class RegexUtil {

    public final static Pattern IP_REGEX = Pattern.compile("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$");

    public final static Pattern PORT_REGEX = Pattern.compile("^[1-9][0-9]*$");

}
