package com.my.easytest.util;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.constants.Constants;
import com.my.easytest.dto.RequestInfoDto;
import com.my.easytest.entity.TestTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Jenkins工具类
 * @Author G_ALLEN
 * @Date 2019/8/28 8:48
 **/

@Slf4j
public class JenkinsUtil {

    public static void main(String[] args) {

    }

    /**
     * 获取执行测试的Job名称
     * @return
     */

    public static String getStartTestJobName(Integer createUserId){
        String jobName = "easytest_start_test_" + createUserId;
        return jobName;
    }

    /**
     * 获取执行测试的Job名称
     * @return
     */

    public static String getJobSignByName(String jobName){
        if(StringUtils.isEmpty(jobName) || !jobName.contains("_")){
            return "";
        }
        String jobSign = jobName.substring(0, jobName.lastIndexOf("_"));
        return jobSign;
    }

    public static StringBuilder getUpdateTaskStatusUrl(RequestInfoDto requestInfoDto, TestTask testTask) {

        StringBuilder updateStatusUrl = new StringBuilder();

        updateStatusUrl.append("curl -X PUT ");
        updateStatusUrl.append("\""+requestInfoDto.getBaseUrl() + "/task/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json \" ");
        updateStatusUrl.append("-H \"token: "+requestInfoDto.getToken()+"\" ");
        updateStatusUrl.append("-d ");
        JSONObject json = new JSONObject();

        json.put("id", testTask.getId());
        json.put("status", Constants.STATUS_THREE);
        json.put("buildUrl","${BUILD_URL}");

        updateStatusUrl.append("'"+json.toJSONString()+"'");

        return updateStatusUrl;
    }



}
