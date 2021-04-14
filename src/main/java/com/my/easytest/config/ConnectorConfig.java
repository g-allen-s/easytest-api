package com.my.easytest.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectorConfig {
    private Properties moConectorCfg = new Properties();
    private Map<String, String> conf = new HashMap();
    private static String CONFIGPATH = "";
    private static ConnectorConfig m_instance;

    public static ConnectorConfig instance() {
        if (m_instance == null) {
            m_instance = new ConnectorConfig();
        }

        return m_instance;
    }

    public ConnectorConfig() {
        try {
            InputStream inputStream = new FileInputStream(CONFIGPATH);
            this.moConectorCfg.load(inputStream);
            inputStream.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public static void setConfigPath(String path) {
        CONFIGPATH = path;
    }

    public String getStringValue(String key) {
        return this.moConectorCfg.getProperty(key);
    }

    public void addCache(String key, String value) {
        this.conf.put(key, value);
    }

    public String getCache(String key) {
        return (String)this.conf.get(key);
    }

}
