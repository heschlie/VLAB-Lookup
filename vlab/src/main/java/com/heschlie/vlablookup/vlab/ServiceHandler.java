package com.heschlie.vlablookup.vlab;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sschlie on 6/3/2014.
 */
public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {}

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            //HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            //Checking http method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                //adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                //appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);

            }

            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                response = "error";
            } else {
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = "error";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            response = "error";
        } catch (IOException e) {
            e.printStackTrace();
            response = "error";
        }
        return response;
    }
}
