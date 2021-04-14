package com.my.easytest.client;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.my.easytest.common.Parameter;
import com.my.easytest.enums.JavaBaseType;
import com.my.easytest.exception.FileNotFoundException;
import com.my.easytest.exception.ServiceException;
import com.my.easytest.util.ClassUtil;
import com.my.easytest.util.FileUtil;
import com.my.easytest.util.RSFUtil;
import com.my.easytest.util.StrUtil;
import com.suning.rsf.consumer.ServiceAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RsfClient {

    public RsfClient() {
    }

    public static void RsfServiceInvoke(Parameter params) {
        // ILogger logger = Logger.getLogger();
        String serversName = params.getString("serviceName"); // 服务契约
        String serverImplCode = params.getString("serviceImplCode"); // 实现码
        String operationName = params.getString("operationFunctionName"); // RSF方法execute
        String operationParams = params.getString("operationFunctionParams"); // 请求参数

        File oScmFile = (File) params.getObject("scmFile");
        if (!oScmFile.exists()) {
            throw new FileNotFoundException("SCM文件不存在！");
        } else {
            String environment = RSFUtil.getSCMEnvironment(oScmFile); // "XZPRE"
            if (params.isTestRunnerClient()) { // in case of suning_testwa_client_type
                String initRsfLdc = System.getProperty("TrInitRsfLdc");
                if (StrUtil.isEmpty(initRsfLdc)) {
                    initRsfLdc = "NJXZ";
                }
                System.setProperty("ldc", initRsfLdc);
                if (!RSFUtil.checkLdc(initRsfLdc, environment)) {
                    throw new ServiceException("请检查scm中的请求环境与当前选择的执行机环境是否一致,有可能产生跨机房调用");
                }
                System.out.println("checkLdc passed!");
            } // 暂可忽略

            Object res = RsfServiceInvoke(environment, serversName, serverImplCode, operationName, operationParams);
            // Reference<Object> ref = Reference.refer(operationReturn);
            // ref.setValue(params, res);
        }
    }

    public static Object RsfServiceInvoke(String environment, String psServiceName, String psServiceImplCode, String psFunctionName, String psArgs) {
        if (StrUtil.isNotEmpty(psFunctionName) && psFunctionName.contains("(") && psFunctionName.contains(")")) {
            Object invokeResult = null;
            Object[] paramLists = getInvokeParamLists(psFunctionName.trim(), psArgs.trim()); // 处理参数列表
            ServiceAgent agent = RSFUtil.getServiceAgent(environment, psServiceName, psServiceImplCode); // 创建agent--核心
            String sApiName = psFunctionName.substring(0, psFunctionName.indexOf("(")); // execute

            try {
                Class[] argTypes;
                if ((paramLists == null || paramLists.length != 1 || paramLists[0] != null) && !"{}".equals(psArgs.trim())) {
                    argTypes = getInvokeParamTypeLists(psFunctionName); // Map or String?
                    invokeResult = agent.invoke(sApiName, paramLists, argTypes); // 有参--核心
                } else {
                    argTypes = null;
                    invokeResult = agent.invoke(sApiName, (Object[])null, argTypes); // 无参
                }
            } finally {
                RSFUtil.destroyConsumerContext(environment);
            }

            if (!(invokeResult instanceof String)) {
                Gson disableEscapGson = (new GsonBuilder()).disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                invokeResult = disableEscapGson.toJson(invokeResult);
            }
            System.out.println("invokeResult:::" + invokeResult);
            return invokeResult;
        } else {
            throw new ServiceException("Rsf方法名称格式不合法，请准守function(参数1,参数2)格式", (Throwable)null);
        }
    }

    public static Class<?>[] getInvokeParamTypeLists(String psFunctionName) {
        String[] typeLists = getParamTypeList(psFunctionName);
        if (typeLists != null && typeLists.length != 0) {
            List<Class<?>> paramTypes = new ArrayList();
            String[] var3 = typeLists;
            int var4 = typeLists.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String type = var3[var5];
                if (StrUtil.isNotEmpty(type.trim())) {
                    try {
                        paramTypes.add(ClassUtil.forName(type.trim()));
                    } catch (ClassNotFoundException var10) {
                        String message = "Not found class for " + type.trim();
                        // ILogger logger = Logger.getLogger();
                        // logger.error(message);
                    }
                }
            }

            return (Class[])paramTypes.toArray(new Class[0]);
        } else {
            return null;
        }
    }

    private static Object[] getInvokeParamLists(String pFunctionName, String paramJsonString) {
        String[] typeLists = getParamTypeList(pFunctionName);
        Object[] res = new Object[typeLists.length];
        JSONObject oParamJsonObj = JSONObject.parseObject(paramJsonString);

        for(int i = 0; i < typeLists.length; ++i) {
            if (StrUtil.isNotEmpty(typeLists[i].trim())) {
                String keyName = i + "_" + typeLists[i].trim();
                if (!oParamJsonObj.containsKey(keyName)) {
                    throw new RuntimeException("RSF参数解析异常,无法找到Key=" + keyName + "值,请检查入参JSON串是否正确!");
                }

                String keyValue = oParamJsonObj.getString(keyName);
                JavaBaseType javaBaseType = JavaBaseType.fromName(typeLists[i].trim());
                if (javaBaseType != null) {
                    res[i] = javaBaseType.valueObject(keyValue);
                } else if ("Map".equalsIgnoreCase(typeLists[i].trim())) {
                    res[i] = JSONObject.parseObject(keyValue, Map.class);
                } else if ("List".equalsIgnoreCase(typeLists[i].trim())) {
                    res[i] = JSONObject.parseObject(keyValue, List.class);
                } else if ("String[]".equalsIgnoreCase(typeLists[i].trim())) {
                    res[i] = JSONObject.parseObject(keyValue, String[].class);
                }
            }
        }

        return res;
    }

    private static String[] getParamTypeList(String poTypeLists) {
        String temp = StrUtil.substringAfter(poTypeLists, "(");
        temp = StrUtil.substringBefore(temp, ")");
        return StrUtil.split(temp, ",");
    }

    /* debug */
    public static void main(String[] args) {
        String scmPath = "/scm/scm-sdas.properties";
        String dataPath = "/data/sdas-dataPermission-permissionList.txt";

        Parameter parameter = new Parameter();
        Map<String, Object> params = new HashMap<>();
        //params.put("serviceReturn", "");
        params.put("scmFile", new File(RsfClient.class.getResource(scmPath).getPath()));
        params.put("serviceName", "com.suning.api.rsf.service.ApiRemoteMapService");
        params.put("serviceImplCode", "sdas-dataPermission-permissionList");
        params.put("operationFunctionName", "execute(Map)");
        params.put("operationFunctionParams", FileUtil.getText(RsfClient.class.getResourceAsStream(dataPath)));
        //params.put("operationFunctionParams", "{\"0_Map\": {\"memberId\":\"7018010945\",\"type\":\"category-level2\",\"permissionType\":\"brand\"}}");


//        params.put("serviceName", "com.suning.api.rsf.service.ApiRemoteMapService");
//        params.put("serviceImplCode", "MPMS-SP-CommonSignUpVerifyServiceImpl");
//        params.put("operationFunctionName", "execute(Map)");
//        params.put("operationFunctionParams", "{\"0_Map\": {\"requestMapSub\": {\"auditObject\": \"01\",\"enrollInfoCode\": \"1593394489476\",\"originSystem\": \"NMPS\",\"comments\": \"1\",\"auditResult\": \"01\"},\"resultMapSub\": {\"enrollInfoCode\": \"1593394489476\"}}}");
        //params.put("client_type", "TestRunner");
        parameter.setParams(params);
        System.out.println(parameter.isTestRunnerClient());
        RsfClient.RsfServiceInvoke(parameter);
    }
}
