package com.my.easytest.client;

import com.my.easytest.common.Parameter;
import com.my.easytest.util.EsUtil;
import io.searchbox.core.SearchResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElasticSearchClient {

    public static void queryLog(Parameter poParam) {
        String urlConfigContent = poParam.getString("url"); // ES地址
        String destination = poParam.getString("destination"); // 目的名称
        String keyword = poParam.getString("keyword", ""); // 关键字
        String request = poParam.getString("request"); // 请求报文输出
        long timeRange = poParam.getLong("timeRange"); // 检索时间范围
        int reqNumber = poParam.getInt("requestNumber"); // 报文个数
        String[] requestVars = request.split(",");
        if (reqNumber != requestVars.length) {
            throw new RuntimeException("设置的报文个数和输出变量的个数不一致!");
        } else {
            String index = EsUtil.getIndexNameForWindq("sendtrace"); // todo -- 核心 sendtrace_njxz_20210204
            System.out.println("elasticsearch index jenkinsName: " + index);
            String query = EsUtil.getQueryforWindqWithMultiParams(destination, keyword, reqNumber, timeRange); // todo -- 核心
            System.out.println("elasticsearch query json: " + query);
            List<Map<String, Object>> requestList = getRequestList(urlConfigContent, index, query); // todo -- 核心
            if (requestList.isEmpty()) {
                System.out.println("未在elasticsearch平台获取到Windq报文！  类型[" + index + "]，目的名称[" + destination + "]，关键字[" + keyword + "]");
            } else {
                System.out.println("类型[" + index + "]，目的名称[" + destination + "]，关键字[" + keyword + "]，从elasticsearch平台获取到报文为：" + requestList.toString());
            }

            ComparatorMap comparatorString = new ComparatorMap();
            if (!requestList.isEmpty()) {
                Collections.sort(requestList, comparatorString);
            }

            for(int indx = 0; indx < reqNumber; ++indx) {
                //Reference<String> oRequest = Reference.refer(requestVars[indx]);
                if (indx < requestList.size()) {
                    String fullTextMessage = ((Map)requestList.get(indx)).get("fullTextMessage").toString();
                    //oRequest.setValue(poParam, fullTextMessage);
                } else {
                    //oRequest.setValue(poParam, "String.empty");
                }
            }

        }
    }

    private static List<Map<String, Object>> getRequestList(String urlConfig, String index, String query) {
        String[] urls = getUrls(urlConfig);
        List<Map<String, Object>> requestList = new ArrayList();
        Set<String> infoIds = new HashSet();

        for(int n = 0; n < urls.length; ++n) {
            String url = urls[n];
            SearchResult searchResult = EsUtil.search(url, index, query);
            List<Map<String, Object>> addRequestList = EsUtil.getContent(searchResult);
            if (null != addRequestList) {
                Iterator var11 = addRequestList.iterator();

                while(var11.hasNext()) {
                    Map<String, Object> map = (Map)var11.next();
                    String infoId = (String)map.get("messageId");
                    if (!infoIds.contains(infoId)) {
                        requestList.add(map);
                        infoIds.add(infoId);
                    }
                }
            }
        }

        return requestList;
    }

    private static String[] getUrls(String sUrl) {
        String pattern = "\\[.[a-z]+\\](.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sUrl);
        if (m.find()) {
            String urls = m.group(1);
            String[] urlList = urls.split(",");

            for(int n = 0; n < urlList.length; ++n) {
                String url = urlList[n];
                String urlPattern = "^http://\\d+.\\d+.\\d+.\\d+:\\d+$";
                Pattern pat = Pattern.compile(urlPattern);
                Matcher urlMt = pat.matcher(url);
                if (!urlMt.find()) {
                    throw new RuntimeException("请检查ES服务地址配置是否有误。请参考：[sit]http://10.47.149.108:9200,http://10.37.154.227:9200,http://10.37.154.228:9200");
                }
            }

            return urlList;
        } else {
            throw new RuntimeException("ES地址配置格式有误，请检查!配置格式请参考：[sit]http://10.47.149.108:9200,http://10.37.154.227:9200,http://10.37.154.228:9200,url之间用英文逗号(,)隔开");
        }
    }

    public static class ComparatorMap implements Comparator<Map<String, Object>> {
        private static final String KEY_NAME = "messageTime";

        public ComparatorMap() {
        }

        public int compare(Map<String, Object> a, Map<String, Object> b) {
            String valA = a.get("messageTime").toString();
            String valB = b.get("messageTime").toString();
            return valB.compareTo(valA);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", "[xzpre]http://10.47.153.50:9200,http://10.47.153.51:9200,http://10.47.153.52:9200");
        params.put("destination", "approvalPromCommdInfoNew");
        //params.put("keyword", "000000000940001796");
        params.put("request", "SEND");
        params.put("timeRange", (long) 120);
        params.put("requestNumber", 1);
        Parameter parameter = new Parameter();
        parameter.setParams(params);
        ElasticSearchClient.queryLog(parameter);

    }
}
