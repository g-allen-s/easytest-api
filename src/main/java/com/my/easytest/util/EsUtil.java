package com.my.easytest.util;

import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class EsUtil {

    private static DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");

    public EsUtil() {
    }

    public static String getIndexNameForWindq(String type) {
        StringBuffer indexName = (new StringBuffer(type)).append("_").append(dateFormat2.format(new Date()));
        return indexName.toString();
    }

    public static String getQueryforWindqWithMultiParams(String destination, String keyword, int varNumber, long timeRange) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery("destination", new String[]{destination.toLowerCase()}));
        if (timeRange > 0L) {
            timeRange = timeRange * 60L * 1000L;
            Date now = new Date();
            Calendar from = Calendar.getInstance();
            from.setTime(new Date(now.getTime() - timeRange));
            Calendar to = Calendar.getInstance();
            to.setTime(new Date(now.getTime()));
            boolQueryBuilder.must(QueryBuilders.rangeQuery("messageTime").gte(from.getTimeInMillis()).lte(to.getTimeInMillis()).includeLower(true).includeUpper(true));
        }

        String[] keywords = keyword.split("&&");
        String[] var13 = keywords;
        int var14 = keywords.length;

        for(int var10 = 0; var10 < var14; ++var10) {
            String key = var13[var10];
            key = key.replaceAll("&#", "&");
            boolQueryBuilder.must(QueryBuilders.termQuery("fullTextMessage", key.toLowerCase()));
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("messageTime", SortOrder.DESC);
        searchSourceBuilder.size(varNumber);
        searchSourceBuilder.from(0);
        return searchSourceBuilder.toString();
    }

    public static SearchResult search(String serverUri, String indexName, String query) {
        HttpClientConfig httpClientConfig = ((HttpClientConfig.Builder)((HttpClientConfig.Builder)((HttpClientConfig.Builder)((HttpClientConfig.Builder)(new HttpClientConfig.Builder(serverUri)).gson((new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create())).connTimeout(1500)).readTimeout(3000)).multiThreaded(true)).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(httpClientConfig);
        JestClient jestClient = factory.getObject();
        SearchResult searchResult = null;

        try {
            Search search = ((io.searchbox.core.Search.Builder)((io.searchbox.core.Search.Builder)(new io.searchbox.core.Search.Builder(query)).addIndex(indexName)).addType("log")).build();
            searchResult = (SearchResult)jestClient.execute(search);
        } catch (Exception var15) {
            throw new RuntimeException("failed to search esb request log from elasticsearch server.", var15);
        } finally {
            try {
                jestClient.shutdownClient();
            } catch (Exception var14) {
                System.out.println("failed to close JestClient." + var14);
            }

        }

        return searchResult;
    }

    public static List<Map<String, Object>> getContent(SearchResult result) {
        if (result == null) {
            return null;
        } else {
            if (!result.isSucceeded()) {
                System.out.println(result.getErrorMessage());
            }

            List<Map<String, Object>> list = new ArrayList();
            if (result.getJsonObject().has("hits")) {
                Iterator var2 = result.getHits(Object.class).iterator();

                while(var2.hasNext()) {
                    SearchResult.Hit<Object, Void> hit = (SearchResult.Hit)var2.next();
                    Object info = hit.source;
                    list.add((Map)info);
                }
            }

            return list;
        }
    }
}
