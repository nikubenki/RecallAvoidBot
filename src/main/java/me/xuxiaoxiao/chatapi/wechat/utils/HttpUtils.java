/**
 *
 */
package me.xuxiaoxiao.chatapi.wechat.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @Title: HttpUtils.java
 * @description:
 * @author guxinyue
 * @date:2011-8-20
 * @version 1.0
 */
public class HttpUtils {
    /**
     *   httpGET
     * @param GETURL  http://a
     * @param para
     * @param charset
     * @return
     */
    public static String httpGet(String GETURL, String para, String charset) {
        String getURL = GETURL + "?" + para;

        return httpGet(getURL, charset);
    }


    /**
     *
     * @param GETURL http://a?a=a
     * @param para
     * @return
     */
    public static String httpGet(String GETURL) {


        return httpGet(GETURL, "");
    }

    /**
     *
     * @param GETURL  http://a?a=a
     * @param charset
     * @return
     */
    public static String httpGet(String GETURL, String charset) {
        if (GETURL == null)
            return null;

        HttpURLConnection connection = null;
        try {
            URL getUrl = new URL(GETURL);
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();

            BufferedReader reader = null;
            if (charset == null || "".equals(charset)) {
                reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream(), charset));
            }

            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    public static String httpPost(String urlStr, String content) {
        return httpPost(urlStr, content, "");
    }

    /**
     * http post
     *
     * @param urlStr
     * @param content
     * @param charset
     * @return
     */
    public static String httpPost(String urlStr, String content, String charset) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());

            if (charset == null || "".equals(charset)) {
                out.write(content.getBytes());
            } else {
                out.write(content.getBytes(charset));
            }
            out.flush();
            out.close();

            BufferedReader reader = null;

            if (charset == null || "".equals(charset)) {
                reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection
                        .getInputStream(), charset));
            }

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {

                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     *
     * @param charset
     * @param para
     * @return
     */
    public static String urlencode(String charset, String para) {
        if (para == null)
            return null;
        StringBuffer sb = new StringBuffer();
        String kvarr[] = para.split("&");
        try {
            for (int i = 0; i < kvarr.length; i++) {

                if (charset != null && !"".equals(charset)) {
                    String tmps[] = kvarr[i].split("=");
                    if (tmps.length >= 2) {
                        tmps[1] = URLEncoder.encode(tmps[1], charset);
                        kvarr[i] = tmps[0].concat("=").concat(tmps[1]);
                    }
                }
                if (i != kvarr.length - 1)
                    sb.append(kvarr[i]).append("&");
                else {
                    sb.append(kvarr[i]);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
