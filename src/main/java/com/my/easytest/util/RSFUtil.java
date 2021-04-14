package com.my.easytest.util;

import com.my.easytest.config.ConnectorConfig;
import com.my.easytest.constants.SCMConstants.Environment;
import com.suning.rsf.RSFException;
import com.suning.rsf.ServiceException;
import com.suning.rsf.consumer.ConsumerContext;
import com.suning.rsf.consumer.ConsumerContextFactory;
import com.suning.rsf.consumer.ServiceAgent;
import com.suning.rsf.model.ServiceKey;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class RSFUtil {
    private static final Logger slfLogger = LoggerFactory.getLogger(RSFUtil.class);
//    private static final String SCHEMA = "tcp";
//    private static final int PORT = 8888;
//    private static final String LDC_PROPERTY = "ldc";
//    private static final String LDC_NJXZ = "NJXZ";
//    public static List<ProviderServer> defaultServers = new ArrayList();
//    private static Map<String, Properties> scmConfig = new HashMap();
//    private static Map<String, ProviderInfo> providerCacheMaps = new HashMap();
//    private static final List<String> NJXZ_LDC = new ArrayList(Arrays.asList("NJXZ", "NJXZ_SIT", "NJXZ_PRE", "NJXZ_DEV"));
    public static final Map<String, List<String>> ldcRuleMap = new HashMap<String, List<String>>() {
        {
            this.put("NJXZ", Arrays.asList("DEV", "SIT", "PRE", "SITE", "SIT2"));
            this.put("NJYH", Arrays.asList("PRD", "SITE"));
            this.put("NJXG_POCYH", Arrays.asList("POC", "SITE"));
            this.put("NJXG_PRE", Arrays.asList("XGPRE", "SITE"));
            this.put("NJXG_PST", Arrays.asList("PST", "SITE"));
        }
    };

    public RSFUtil() {
    }

    public static void setLDC(String ldc) {
        if ("TestRunner".equals(System.getProperty("Client_Type"))) {
        } else {
            if (!StrUtil.isEmpty(ConnectorConfig.instance().getStringValue("rsf.dh")) && "true".equals(ConnectorConfig.instance().getStringValue("rsf.dh"))) {
                ldc = ConnectorConfig.instance().getStringValue("rsf.ldc");
            }

            if (StrUtil.isNotEmpty(ldc)) {
                System.setProperty("ldc", ldc);
            } else {
                System.setProperty("ldc", "NJXZ");
            }

        }
    }

    public static String getSCMEnvironment(File scmFile) {
        String environment = "DEV";
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            Properties properties = new Properties();
            fis = new FileInputStream(scmFile.getAbsolutePath());
            bis = new BufferedInputStream(fis);
            properties.load(bis);
            String scmServer = (String)properties.get("scmServer");
            if (scmServer.startsWith("http://scmdev")) {
                environment = Environment.DEV.name();
            } else if (scmServer.startsWith("http://scmsite")) {
                environment = Environment.SITE.name();
            } else if (scmServer.startsWith("http://scmsit2")) {
                environment = Environment.SIT2.name();
            } else if (scmServer.startsWith("http://scmsit")) {
                environment = Environment.SIT.name();
            } else if (scmServer.startsWith("http://scmpre")) {
                environment = Environment.PRE.name();
            } else if (scmServer.startsWith("http://scmpoc")) {
                environment = Environment.POC.name();
            } else if (scmServer.startsWith("http://scmxgpre")) {
                environment = Environment.XGPRE.name();
            } else if (scmServer.startsWith("http://scmpst")) {
                environment = Environment.PST.name();
            } else {
                environment = Environment.PRD.name();
            }

            if (StrUtil.isNotEmpty(ConnectorConfig.instance().getStringValue("rsf.dh")) && !"false".equals(ConnectorConfig.instance().getStringValue("rsf.dh"))) {
                setLDC(ConnectorConfig.instance().getStringValue("rsf.ldc"));
            } else {
                setLDC(RsfEnvironment.getLdc(environment));
            }
        } catch (Exception var13) {
            throw new RSFException("Parser scm file occur exception", var13);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return environment;
    }

    public static String getSCMPropertiesFile(String environment) {
        if (Environment.DEV.name().equalsIgnoreCase(environment)) {
            return "/scmdev.properties";
        } else if (Environment.SIT.name().equalsIgnoreCase(environment)) {
            return "/scmsit.properties";
        } else if (Environment.PRE.name().equalsIgnoreCase(environment)) {
            return "/scmpre.properties";
        } else if (Environment.POC.name().equalsIgnoreCase(environment)) {
            return "/scmpoc.properties";
        } else if (Environment.XGPRE.name().equalsIgnoreCase(environment)) {
            return "/scmxgpre.properties";
        } else if (Environment.PST.name().equalsIgnoreCase(environment)) {
            return "/scmpst.properties";
        } else if (Environment.SITE.name().equalsIgnoreCase(environment)) {
            return "/scmsite.properties";
        } else {
            return Environment.SIT2.name().equalsIgnoreCase(environment) ? "/scmsit2.properties" : "";
        }
    }

    public static ServiceAgent getServiceAgent(String environment, String contract, String implCode) {
        String targetLDC;
        try {
            resertZkLdc(); // 冷启动
        } catch (IllegalArgumentException var5) {
            throw new ServiceException("重置rsf的LDC变量失败", var5);
        } catch (IllegalAccessException var6) {
            targetLDC = ExceptionUtils.getStackTrace(var6);
            throw new ServiceException("重置rsf的LDC变量失败:" + targetLDC, var6);
        }

        String scmPropertiesFile = getSCMPropertiesFile(environment); // "/scmpre.properties"
        ConsumerContext context = ConsumerContextFactory.getConsumerContext(scmPropertiesFile); // 可以详细看看怎么获取到上下文的
        return context.getServiceAgent(new ServiceKey(contract, implCode));
    }

    public static void destroyConsumerContext(String environment) {
        String scmPropertiesFile = getSCMPropertiesFile(environment);
        ConsumerContext context = ConsumerContextFactory.getConsumerContext(scmPropertiesFile);
        context.destroy();
    }

    public static void resertZkLdc() throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = com.suning.rsf.util.Environment.class.getDeclaredFields();
        Field[] var1 = fields;
        int var2 = fields.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Field f = var1[var3];
            if ("LDC".equals(f.getName())) {
                f.setAccessible(true);
                f.set(f, (Object)null);
            }
        }

    }

    public static boolean checkLdc(String ldc, String env) {
        if (!StrUtil.isEmpty(ldc) && !StrUtil.isEmpty(env)) {
            ldc = ldc.toUpperCase();
            env = env.toUpperCase();
            if (ldc.contains("POC")) {
                ldc = "NJXG_POCYH";
            }

            if (ldcRuleMap.containsKey(ldc)) {
                List<String> list = (List)ldcRuleMap.get(ldc);
                return list.contains(env);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String environment = "sit";

        for(int i = 0; i < 5; ++i) {
            String scmFilePath = getSCMPropertiesFile(environment);
            InputStream inputStream = RSFUtil.class.getResourceAsStream(scmFilePath);
            Properties properties = new Properties();

            try {
                properties.load(inputStream);
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public static class RsfEnvironment {
        private static Map<String, String> ldcMap = new HashMap();

        public RsfEnvironment() {
        }

        public static String getLdc(String environment) {
            return (String)ldcMap.get(environment);
        }

        static {
            ldcMap.put(Environment.DEV.name(), "NJXZ");
            ldcMap.put(Environment.SIT.name(), "NJXZ");
            ldcMap.put(Environment.PRE.name(), "NJXZ");
            ldcMap.put(Environment.POC.name(), "NJXG_POCYH");
            ldcMap.put(Environment.XGPRE.name(), "NJXG_PRE");
            ldcMap.put(Environment.PRD.name(), "NJYH");
            ldcMap.put(Environment.PST.name(), "NJXG_PST");
            ldcMap.put(Environment.XZPRE.name(), "NJXZ");
        }
    }
}
