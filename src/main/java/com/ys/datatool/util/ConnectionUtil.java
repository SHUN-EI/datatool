package com.ys.datatool.util;

import com.ys.datatool.domain.config.WebConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by mo on  2017/7/10.
 */
public class ConnectionUtil {

    public static Response doPostWithSOAP(String url, String SOAPAction, String param) throws IOException {
        Response response = Request.Post(url)
                .setHeader("SOAPAction", SOAPAction)
                .bodyString(param, ContentType.TEXT_XML)
                .execute();

        return response;
    }

    public static Response doGetWith(String url, String cookie) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Cookie", cookie)
                .setHeader("X-Requested-With", WebConfig.X_REQUESTED_WITH)
                .execute();

        return response;
    }

    public static Response doPutWithJson(String url, String param, String cookie) throws IOException {

        Response response = Request.Put(url)
                .setHeader("Cookie", cookie)
                .setHeader("content-type", WebConfig.CONTENT_TYPE)
                .bodyString(param, ContentType.APPLICATION_JSON)
                .execute();

        return response;
    }

    public static Response doPostWithoutParam(String url, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("cookie", cookie)
                .setHeader("content-type", WebConfig.CONTENT_TYPE)
                .execute();

        return response;
    }

    public static Response doPostWithToken(String url, String param, String cookie, String token) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .setHeader("token", token)
                .bodyString(param, ContentType.APPLICATION_FORM_URLENCODED)
                .execute();

        return response;
    }


    public static Response doPostWithLeastParamJson(String url, String param, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .setHeader("content-type", WebConfig.CONTENT_TYPE)
                .setHeader("accept", WebConfig.ACCEPT)
                .bodyString(param, ContentType.APPLICATION_JSON)
                .execute();

        return response;
    }

    public static Response doPostWithLeastParams(String url, List params, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .bodyForm(params, Charset.forName("utf-8"))
                .execute();

        return response;
    }

    public static Response doGetEncode(String url, String cookie, String accept_encoding) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Cookie", cookie)
                .setHeader("accept-encoding", accept_encoding)
                .setHeader("accept", WebConfig.ACCEPT)
                .execute();

        return response;
    }

    public static Response doPostEncode(String url, List params, String cookie, String accept_encoding) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .setHeader("accept-encoding", accept_encoding)
                .setHeader("accept", WebConfig.ACCEPT)
                .bodyForm(params, Charset.forName("utf-8"))
                .execute();

        return response;
    }

    public static Response doGetWithLeastParams(String url, String cookie) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Cookie", cookie)
                .execute();

        return response;
    }

    public static Response doPostWithLeastParamJsonInPhone(String url, List params, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .setHeader("model", "iPhone9,1")
                .setHeader("appVer", "5.0.7")
                .bodyForm(params, Charset.forName("utf-8"))
                .execute();

        return response;
    }


}
