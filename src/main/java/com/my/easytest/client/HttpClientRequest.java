package com.my.easytest.client;

import com.suning.rsc.RscException;
import com.suning.rsc.utils.HttpClientTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);
    private static int maxConnPerRoute = 128;
    private static int maxTotalConn = 384;
    private static int CONN_TIME_OUT = 1000;
    private static int SO_TIME_OUT = 5000;
    private static ConcurrentHashMap<String, CloseableHttpClient> closeableHttpClientMaps = new ConcurrentHashMap();
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String DEFAULT_CONTENT_ENCODING = "UTF-8";
    private static ThreadSafeClientConnManager connectionManager = null;
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public HttpClientRequest() {
    }

    public static CloseableHttpClient buildPoolHttpClient(String hostName, int port, int liveSeconds, int maxPerRoute, int maxTotal) {
        String key = hostName + ":" + port;
        CloseableHttpClient closeableHttpClient = (CloseableHttpClient)closeableHttpClientMaps.get(key);
        if (closeableHttpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager((long)liveSeconds, TimeUnit.SECONDS);
            cm.setValidateAfterInactivity(2000);
            cm.setDefaultMaxPerRoute(maxPerRoute);
            cm.setMaxTotal(maxTotal);
            HttpHost httpHost = new HttpHost(hostName, port);
            cm.setMaxPerRoute(new HttpRoute(httpHost), maxTotal);
            closeableHttpClient = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(createConnectionKeepAliveStrategy()).build();
            closeableHttpClientMaps.put(key, closeableHttpClient);
        }

        return (CloseableHttpClient)closeableHttpClientMaps.get(key);
    }

    private static ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));

                while(true) {
                    String param;
                    String value;
                    do {
                        do {
                            if (!it.hasNext()) {
                                HttpHost target = (HttpHost)context.getAttribute("http.target_host");
                                if (target.getHostName().contains("testadminsit.cnsuning.com")) {
                                    return 15000L;
                                }

                                return 30000L;
                            }

                            HeaderElement he = it.nextElement();
                            param = he.getName();
                            value = he.getValue();
                        } while(value == null);
                    } while(!param.equalsIgnoreCase("timeout"));

                    try {
                        return Long.parseLong(value) * 1000L;
                    } catch (NumberFormatException var8) {
                    }
                }
            }
        };
        return myStrategy;
    }

    public static ThreadSafeClientConnManager getThreadSafeClientConnManager() {
        return connectionManager;
    }

    public static String executeGet(String url, String enchode, int timeOut, int soTimeOut) throws RscException {
        String result = execute(new HttpGet(url), url, enchode, timeOut, soTimeOut);
        logger.debug("[HttpClientTemplate:executeGet()]: [jenkinsUrl={}]: [response={}] send successful!", url, result);
        return result;
    }

    public static String executeGet(String url) throws RscException {
        return executeGet(url, (String)null, 0, 0);
    }

    public static String executePost(String url, List<? extends NameValuePair> params, String encoding, boolean isUrlEncode, int connTimeOut, int soTimeOut) throws RscException {
        if (encoding == null) {
            encoding = "UTF-8";
        }

        if (connTimeOut == 0) {
            connTimeOut = CONN_TIME_OUT;
        }

        if (soTimeOut == 0) {
            soTimeOut = SO_TIME_OUT;
        }

        HttpPost httpPost = new HttpPost(url);

        Object reqEntity;
        try {
            if (isUrlEncode) {
                reqEntity = new UrlEncodedFormEntity(params, encoding);
            } else {
                reqEntity = createRequestStringEntity(params, encoding);
            }
        } catch (IOException var10) {
            RscException re = new RscException("other", var10.getMessage(), var10);
            throw re;
        }

        httpPost.setEntity((HttpEntity)reqEntity);
        String result = execute(httpPost, url, encoding, connTimeOut, soTimeOut);
        logger.debug("[HttpClientTemplate:executePost()]: [jenkinsUrl={}]: [response={}] send successful!", url, result);
        return result;
    }

    public static String executePost(String url, List<? extends NameValuePair> params, boolean isUrlEncode) throws RscException {
        return executePost(url, params, (String)null, isUrlEncode, 0, 0);
    }

    public static String executePost(String url, List<? extends NameValuePair> params) throws RscException {
        return executePost(url, params, false);
    }

    public static String executePost(String url, String params, String encoding, int timeOut, int soTimeOut) throws IOException {
        try {
            logger.info("Execute post info: jenkinsUrl{}, params{} is", url + "," + params);
            if (encoding == null) {
                encoding = "UTF-8";
            }

            if (timeOut == 0) {
                timeOut = CONN_TIME_OUT;
            }

            if (soTimeOut == 0) {
                soTimeOut = SO_TIME_OUT;
            }

            HttpPost httpPost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(params, encoding);
            reqEntity.setContentEncoding(encoding);
            reqEntity.setContentType("application/json;charset=UTF-8");
            httpPost.setEntity(reqEntity);
            String result = execute(httpPost, url, encoding, timeOut, soTimeOut);
            logger.debug("[HttpClientTemplate:executePost()]: [jenkinsUrl=" + url + "]: [params=" + params + "]: [response=" + result + "] send successful!");
            return result;
        } catch (Exception var8) {
            throw new IOException("Execute post occur excepiton.", var8);
        }
    }

    private static void configRequest(HttpRequestBase httpReq, int connTimeOut, int soTimeout) {
        if (connTimeOut == 0) {
            connTimeOut = CONN_TIME_OUT;
        }

        if (soTimeout == 0) {
            soTimeout = SO_TIME_OUT;
        }

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeOut).setSocketTimeout(soTimeout).build();
        httpReq.setConfig(requestConfig);
    }

    public static String executePost(String url, Map<String, String> headers, String content, String contentType, int timeOut, int soTimeOut) throws IOException {
        try {
            if (contentType == null) {
                contentType = "application/json;charset=UTF-8";
            }

            if (timeOut == 0) {
                timeOut = CONN_TIME_OUT;
            }

            if (soTimeOut == 0) {
                soTimeOut = SO_TIME_OUT;
            }

            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
                Iterator var7 = headers.entrySet().iterator();

                while(var7.hasNext()) {
                    Entry<String, String> entry = (Entry)var7.next();
                    httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
                }
            }

            StringEntity reqEntity = new StringEntity(content, "UTF-8");
            reqEntity.setContentType(contentType);
            httpPost.setEntity(reqEntity);
            return execute(httpPost, url, (String)null, timeOut, soTimeOut);
        } catch (Exception var9) {
            throw new IOException("Execute post occur excepiton.", var9);
        }
    }

    public static String executePost(String url, String params) throws IOException {
        return executePost(url, params, (String)null, 0, 0);
    }

    private static StringEntity createRequestStringEntity(List<? extends NameValuePair> params, String encoding) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        Iterator var3 = params.iterator();

        while(var3.hasNext()) {
            NameValuePair parameter = (NameValuePair)var3.next();
            String encodedName = parameter.getName();
            String value = parameter.getValue();
            String encodedValue = value != null ? value : "";
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(encodedName);
            result.append("=");
            result.append(encodedValue);
        }

        StringEntity reqEntity = new StringEntity(result.toString(), encoding);
        return reqEntity;
    }

    private static String execute(HttpRequestBase httpReq, String url, String enchode, int timeOut, int soTimeOut) throws RscException {
        CloseableHttpClient httpclient = getHttpClient(url, timeOut, soTimeOut);
        StringBuilder sb = new StringBuilder();
        CloseableHttpResponse response = null;
        InputStreamReader inputStreamReader = null;

        try {
            RscException re;
            try {
                configRequest(httpReq, timeOut, soTimeOut);
                response = httpclient.execute(httpReq);
                HttpEntity entity = null;
                entity = response.getEntity();
                if (entity == null) {
                    re = null;
                    return re.toString();
                }

                logger.debug("Response content length: " + entity.getContentLength());
                if (enchode == null || "".equals(enchode)) {
                    enchode = "UTF-8";
                }

                inputStreamReader = new InputStreamReader(entity.getContent(), enchode);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = null;

                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception var20) {
                if (SocketTimeoutException.class.isInstance(var20)) {
                    logger.error("socket timeout error!,jenkinsUrl:{},enchode:{},timeOut:{},soTimeOut", new Object[]{url, enchode, timeOut, soTimeOut, var20});
                    throw new RscException("sotimeout", "socket timeout!", var20);
                }

                if (!ConnectTimeoutException.class.isInstance(var20) && !ConnectException.class.isInstance(var20)) {
                    logger.error("other exception error!,jenkinsUrl:{},enchode:{},timeOut:{},soTimeOut", new Object[]{url, enchode, timeOut, soTimeOut, var20});
                    re = new RscException("other", var20.getMessage(), var20);
                    throw re;
                }

                logger.error("connection timeout error!,jenkinsUrl:{},enchode:{},timeOut:{},soTimeOut", new Object[]{url, enchode, timeOut, soTimeOut, var20});
                throw new RscException("timeout", "connection timeout!", var20);
            }
        } finally {
            if (httpReq != null) {
                httpReq.abort();
            }

            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException var19) {
                }
            }

        }

        return sb.toString();
    }

    public static CloseableHttpClient getHttpClient(String url, int timeOut, int soTimeout) {
        if (timeOut == 0) {
            timeOut = CONN_TIME_OUT;
        }

        if (soTimeout == 0) {
            soTimeout = SO_TIME_OUT;
        }

        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }

        return buildPoolHttpClient(hostname, port, 600, maxConnPerRoute, maxTotalConn);
    }

    public static void release() {
        if (connectionManager != null) {
            connectionManager.shutdown();
        }

    }

    static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        connectionManager = new ThreadSafeClientConnManager(schemeRegistry);

        try {
            connectionManager.setMaxTotal(maxTotalConn);
        } catch (NumberFormatException var3) {
            throw new RuntimeException("Key[httpclient.max_total] Not Found in systemConfig.properties", var3);
        }

        try {
            connectionManager.setDefaultMaxPerRoute(maxConnPerRoute);
        } catch (NumberFormatException var2) {
            throw new RuntimeException("Key[httpclient.default_max_connection] Not Found in systemConfig.properties", var2);
        }
    }
}
