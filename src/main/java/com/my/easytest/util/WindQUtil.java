package com.my.easytest.util;

import com.suning.windq.jms.WindQConnectionFactory;

public class WindQUtil {

    public WindQUtil() {
    }

    public static WindQConnectionFactory getWindQConnectionFactory(String sEnvironment) throws Exception {
        String sScmFilePath = getFilePath(sEnvironment);
        if (null == sScmFilePath) {
            throw new Exception("当前所选环境(" + sEnvironment + ")有误,目前支持的环境有：DEV、SIT、XZPRE、XGPRE、POC");
        } else {
            WindQConnectionFactory oWindQConnectionFactory = new WindQConnectionFactory(sScmFilePath, (String)null);
            return oWindQConnectionFactory;
        }
    }

    private static String getFilePath(String sEnvironment) {
        String sScmFilePath = null;
        String env = sEnvironment.toUpperCase();
        byte var4 = -1;
        switch(env.hashCode()) {
            case 67573:
                if (env.equals("DEV")) {
                    var4 = 0;
                }
                break;
            case 79396:
                if (env.equals("POC")) {
                    var4 = 4;
                }
                break;
            case 82110:
                if (env.equals("SIT")) {
                    var4 = 1;
                }
                break;
            case 83464500:
                if (env.equals("XGPRE")) {
                    var4 = 2;
                }
                break;
            case 84030529:
                if (env.equals("XZPRE")) {
                    var4 = 3;
                }
        }

        switch(var4) {
            case 0:
                sScmFilePath = "/windqscmdev.properties";
                break;
            case 1:
                sScmFilePath = "/windqscmsit.properties";
                break;
            case 2:
                sScmFilePath = "/windqscmxgpre.properties";
                break;
            case 3:
                sScmFilePath = "/windqscmxzpre.properties";
                break;
            case 4:
                sScmFilePath = "/windqscmpoc.properties";
        }

        return sScmFilePath;
    }

}
