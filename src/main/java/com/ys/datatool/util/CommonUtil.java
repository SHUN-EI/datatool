package com.ys.datatool.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by mo on   2017/6/20.
 */
public class CommonUtil {

    public static String priceFormat(String price) {

        if ("0".equals(price) || price == null)
            return "0.00";

        String reverseStr = new StringBuffer(price).reverse().toString();
        String decimalStr = reverseStr.substring(0, 2) + ".";
        String decimal = new StringBuffer(decimalStr).reverse().toString();

        String integerStr = reverseStr.substring(2, reverseStr.length());
        String result = "";
        for (int i = 0; i < integerStr.length(); i++) {
            if (i * 3 + 3 > integerStr.length()) {
                result += integerStr.substring(i * 3, integerStr.length());
                break;
            }
            result += integerStr.substring(i * 3, i * 3 + 3) + ",";
        }

        if (result.endsWith(","))
            result = result.substring(0, result.length() - 1);

        String integer = new StringBuffer(result).reverse().toString();

        return integer + decimal;
    }

    public static String specialPriceFormat(String price) {
        if (".0000".equals(price) || "".equals(price) || price == null)
            return "0";

        String result = "";
        String condition = ".";
        if (price.contains(condition)) {
            int index = price.indexOf(condition);
            if (index != -1)
                result = price.substring(0, index);
        }

        return result;
    }

    public static String fetchString(String str, String regEx) throws PatternSyntaxException {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return m.group(0);
        return str;
    }

    public static String filterString(String str, String regEx) throws PatternSyntaxException {

        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.matches())
            return m.group(1);
        return str;
    }

    public static String filterString(String str) throws PatternSyntaxException {
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getIsValidForever(String validTime) {
        String isValidForever = "";

        if (!"".equals(validTime)) {
            isValidForever = "否";
        } else {
            isValidForever = "是";
        }

        return isValidForever;
    }

    public static String formatString(String target) {
        return target == "null" ? "" : target;
    }


}
