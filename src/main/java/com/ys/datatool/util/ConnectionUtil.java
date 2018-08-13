package com.ys.datatool.util;

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

    public static Response doGetWithAuthority(String url, String accept, String cookie, String referer, String authority, String x_Requested_With, String user_Agent) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Accept", accept)
                .setHeader("Cookie", cookie)
                .setHeader("authority", authority)
                .setHeader("Referer", referer)
                .setHeader("X-Requested-With", x_Requested_With)
                .setHeader("User-Agent", user_Agent)
                .execute();

        return response;
    }

    public static Response doPostWithToken(String url, String param, String accept, String accept_encoding, String content_type, String cookie, String origin, String referer, String user_agent, String x_CSRF_Token, String x_JDY_Ver) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Accept", accept)
                .setHeader("accept-encoding", accept_encoding)
                .setHeader("content-type", content_type)
                .setHeader("cookie", cookie)
                .setHeader("origin", origin)
                .setHeader("referer", referer)
                .setHeader("user-agent", user_agent)
                .setHeader("X-CSRF-Token", x_CSRF_Token)
                .setHeader("X-JDY-Ver", x_JDY_Ver)
                .bodyString(param, ContentType.APPLICATION_JSON)
                .execute();

        return response;
    }

    public static Response doPostWithJson(String url, String param, String authority, String accept, String accept_encoding, String accept_language, String content_type, String cookie, String origin, String referer, String user_agent) throws IOException {

        Response response = Request.Post(url)
                .setHeader("authority", authority)
                .setHeader("Accept", accept)
                .setHeader("accept-encoding", accept_encoding)
                .setHeader("accept-language", accept_language)
                .setHeader("content-type", content_type)
                .setHeader("cookie", cookie)
                .setHeader("origin", origin)
                .setHeader("referer", referer)
                .setHeader("user-agent", user_agent)
                .bodyString(param, ContentType.APPLICATION_JSON)
                .execute();

        return response;
    }

    public static Response doPost(String url, List params, String accept, String cookie, String connection, String host, String origin, String referer, String user_Agent, String x_Requested_With) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Accept", accept)
                .setHeader("Cookie", cookie)
                .setHeader("Connection", connection)
                .setHeader("Host", host)
                .setHeader("Origin", origin)
                .setHeader("Referer", referer)
                .setHeader("User-Agent", user_Agent)
                .setHeader("X-Requested-With", x_Requested_With)
                .bodyForm(params, Charset.forName("utf-8"))
                .execute();

        return response;
    }

    public static Response doPostWithLeastParamJson(String url, String param, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
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

    public static Response doGetEncode(String url, String cookie,String accept_encoding,String accept) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Cookie", cookie)
                .setHeader("accept-encoding", accept_encoding)
                .setHeader("accept", accept)
                .execute();

        return response;
    }

    public static Response doGetWithLeastParams(String url, String cookie) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Cookie", cookie)
                .execute();

        return response;
    }

    public static Response doPostWithLeastParamJsonInPhone(String url,  List params, String cookie) throws IOException {

        Response response = Request.Post(url)
                .setHeader("Cookie", cookie)
                .setHeader("model", "iPhone9,1")
                .setHeader("appVer", "5.0.7")
                .bodyForm(params, Charset.forName("utf-8"))
                .execute();

        return response;
    }

    public static Response doGet(String url, String accept, String cookie, String connection, String host, String referer, String x_Requested_With, String upgrade_Insecure_Requests, String user_Agent) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Accept", accept)
                .setHeader("Cookie", cookie)
                .setHeader("Connection", connection)
                .setHeader("Host", host)
                .setHeader("Referer", referer)
                .setHeader("X-Requested-With", x_Requested_With)
                .setHeader("Upgrade-Insecure-Requests", upgrade_Insecure_Requests)
                .setHeader("User-Agent", user_Agent)
                .execute();

        return response;
    }

    public static Response doGetWithXAGENT(String url, String accept, String cookie, String connection, String host, String referer, String x_REQUESTED_WITH, String uSER_AGENT, String xagent) throws IOException {

        Response response = Request.Get(url)
                .setHeader("Accept", accept)
                .setHeader("Cookie", cookie)
                .setHeader("Connection", connection)
                .setHeader("Host", host)
                .setHeader("Referer", referer)
                .setHeader("X-Requested-With", x_REQUESTED_WITH)
                .setHeader("User-Agent", uSER_AGENT)
                .setHeader("xagent", xagent)
                .execute();

        return response;
    }
}
