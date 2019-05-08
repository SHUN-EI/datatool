package com.ys.datatool.domain.config;

import java.nio.charset.Charset;

/**
 * Created by mo on 2019/4/28
 */
public class WebConfig {

    public static String CONTENT_TYPE = "application/json;charset=UTF-8";

    public static String X_REQUESTED_WITH = "XMLHttpRequest";

    public static String ACCEPT = "application/json, text/javascript, */*; q=0.01";

    public static Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
}
