package com.my.easytest.client;

import com.my.easytest.exception.ServiceException;
import com.my.easytest.util.StrUtil;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpClient {
    private static final Logger slfLogger = LoggerFactory.getLogger(HttpClient.class);
    private static final int HTTP_RESPONSE_CODE_FLAG = 300;
    private static final String IBM_JSSE2_SOCKETFACTORY_CLASS = "com.ibm.jsse2.SSLSocketFactoryImpl";
    private static SSLSocketFactory jsse2SslSocketFactory;
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
    private static final String USER_AGENT = "User-Agent";
    private static final String FAKE_USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

    public HttpClient() {
    }

    public static String getStartStabilityUrl(String ip) {
        return String.format("http://%s:2015/stabilityMainTest", ip);
    }

    public static String getResponseFromServer(String url, int connectionTimeout, int readTimeout, boolean trustAllCerts) throws IOException {
        URLConnection conn = (new URL(url)).openConnection(Proxy.NO_PROXY);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        initSslSetting(conn, trustAllCerts);
        if (connectionTimeout >= 0) {
            conn.setConnectTimeout(connectionTimeout);
        }

        if (readTimeout >= 0) {
            conn.setReadTimeout(readTimeout);
        }

        if (conn instanceof HttpURLConnection) {
            validateResponse((HttpURLConnection)conn);
        }

        return readResult(conn.getInputStream());
    }

    private static void initSslSetting(URLConnection conn, boolean trustAllCerts) {
        if (conn instanceof HttpsURLConnection) {
            if (trustAllCerts) {
                try {
                    SSLContext sslCtx = SSLContext.getInstance("TLS");
                    sslCtx.init((KeyManager[])null, new TrustManager[]{new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }}, new SecureRandom());
                    ((HttpsURLConnection)conn).setSSLSocketFactory(sslCtx.getSocketFactory());
                    ((HttpsURLConnection)conn).setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    });
                } catch (Exception var3) {
                }
            } else {
                if (System.getProperty("java.vm.vendor").toLowerCase().startsWith("ibm") && jsse2SslSocketFactory != null) {
                    ((HttpsURLConnection)conn).setSSLSocketFactory(jsse2SslSocketFactory);
                }

                ((HttpsURLConnection)conn).setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
            }
        }

    }

    public static String getResponseViaPost(String url, Map<String, String> headers, String body, String contentType, int connectionTimeout, int readTimeout, boolean trustAllCerts) throws IOException {
        return HttpClientRequest.executePost(url, headers, body, contentType, connectionTimeout, readTimeout);
    }

    public static String getPost(String url, String param, String contentType, int connectionTimeout, int readTimeout, boolean trustAllCerts) throws MalformedURLException, IOException {
        URLConnection connection = (new URL(url)).openConnection(Proxy.NO_PROXY);
        initSslSetting(connection, trustAllCerts);
        HttpURLConnection con = (HttpURLConnection)connection;
        if (connectionTimeout >= 0) {
            con.setConnectTimeout(connectionTimeout);
        }

        if (readTimeout >= 0) {
            con.setReadTimeout(readTimeout);
        }

        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", contentType);
        con.setRequestProperty("Content-Length", Integer.toString(param.getBytes(Charset.defaultCharset()).length));
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        DataOutputStream printout = new DataOutputStream(con.getOutputStream());
        printout.write(param.getBytes("UTF-8"));
        printout.flush();
        printout.close();
        validateResponse(con);
        return readResult(con.getInputStream());
    }

    public static String getResponseViaPost(String url, String param, String contentType, int connectionTimeout, int readTimeout, boolean trustAllCerts) throws IOException {
        return HttpClientRequest.executePost(url, param, (String)null, connectionTimeout, readTimeout);
    }

    private static void validateResponse(HttpURLConnection con) throws IOException {
        if (con.getResponseCode() >= 300) {
            try {
                InputStream es = con.getErrorStream();
                if (es != null) {
                }
            } catch (Exception var2) {
            }

            throw new IOException("Did not receive successful HTTP response: status code = " + con.getResponseCode() + ", status message = [" + con.getResponseMessage() + "]");
        }
    }

    public static String readResult(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder temp = new StringBuilder();

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                temp.append(line);
            }

            String var4 = temp.toString();
            return var4;
        } finally {
            reader.close();
        }
    }

    private static File downloadFile(String httpUrl, String saveFile) throws IOException {
        URL url = null;
        String uri = StrUtil.substringBeforeLast(httpUrl, "/");
        String fileName = StrUtil.substringAfterLast(httpUrl, "/");
        httpUrl = uri + "/" + URLEncoder.encode(fileName, "UTF-8");
        url = new URL(httpUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        try {
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(45000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            InputStream inputStream = conn.getInputStream();
            writeInputStreamToFile(inputStream, saveFile);
        } catch (Exception var10) {
            slfLogger.warn(var10.getMessage());
        } finally {
            conn.disconnect();
        }

        return new File(saveFile);
    }

    public static boolean httpDownload(String httpUrl, String saveFile, int tryTimes) {
        File oSaveFile = null;

        for(int i = 0; i < tryTimes; ++i) {
            try {
                oSaveFile = downloadFile(httpUrl, saveFile);
                if (oSaveFile.exists()) {
                    break;
                }
            } catch (Exception var6) {
                slfLogger.warn("Download file :" + httpUrl + " exception.");
            }
        }

        if (oSaveFile != null && oSaveFile.exists()) {
            return true;
        } else {
            throw new ServiceException("下载文件失败，请检查网络状态及确认网络文件是否存在.Trytime为：" + tryTimes, (Throwable)null);
            // throw ExceptionHandles.getFileNotFoundException("下载文件失败，请检查网络状态及确认网络文件是否存在.Trytime为：" + tryTimes, (Throwable)null);
        }
    }

    public static boolean httpDownload(String httpUrl, String saveFile) {
        return httpDownload(httpUrl, saveFile, 2);
    }

    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        boolean var3 = false;

        int rc;
        while((rc = inStream.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }

        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static File writeBytesToFile(byte[] b, String outputFile) {
        File file = null;
        FileOutputStream os = null;

        try {
            file = new File(outputFile);
            os = new FileOutputStream(file);
            os.write(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return file;
    }

    public static void writeInputStreamToFile(InputStream inputStream, String filename) {
        File file = new File(filename);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException var16) {
            slfLogger.warn("写入文件前清空操作失败 " + var16.getMessage());
        }

        byte[] bytes = new byte[1024];
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file, true);

            int readNum;
            while((readNum = inputStream.read(bytes, 0, 1024)) > 0) {
                fileOutputStream.write(bytes, 0, readNum);
            }
        } catch (Exception var17) {
            slfLogger.warn(var17.getMessage());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

                inputStream.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

    }

    static {
        if (System.getProperty("java.vm.vendor").toLowerCase().startsWith("ibm")) {
            try {
                Class jsse2SslSocketFactoryClass = Class.forName("com.ibm.jsse2.SSLSocketFactoryImpl");
                jsse2SslSocketFactory = (SSLSocketFactory)jsse2SslSocketFactoryClass.newInstance();
            } catch (Exception var1) {
            }
        }

    }
}
