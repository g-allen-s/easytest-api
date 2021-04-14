package com.my.easytest.util;

import com.my.easytest.entity.TestJenkins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Jenkins工具类
 * @Author G_ALLEN
 * @Date 2019/8/28 8:48
 **/

@Slf4j
public class ReportUtil {

    public static void main(String[] args) {

        String buildUrl = "http:///job/my_start_test_1/label=jenkins_slave/2/allure/";

         String allureReportBaseUrl = buildUrl.substring(buildUrl.indexOf("/job"));

        System.out.println("allureReportBaseUrl== "+allureReportBaseUrl);

    }

    /**
     *  获取allure报告地址
     * @param buildUrl 构建地址
     * @param testJenkins Jenkins记录表
     * @param autoLoginJenkinsFlag 是否自动登录Jenkins
     * @return
     */

    public static String getAllureReportUrl(String buildUrl, TestJenkins testJenkins, boolean autoLoginJenkinsFlag){

        if(StringUtils.isEmpty(buildUrl) || !buildUrl.contains("/job")){
            return buildUrl;
        }
        String allureReportUrl = buildUrl;

        if(autoLoginJenkinsFlag){
            allureReportUrl = getAllureReportUrlAndLogin(buildUrl, testJenkins);
        }
        return allureReportUrl + "allure/";
    }

    /**
     *  获取可以自动登录Jenkins的allure报告地址
     * @param buildUrl
     * @param testJenkins
     * @return
     */
    private static String getAllureReportUrlAndLogin(String buildUrl, TestJenkins testJenkins) {
        String allureReportUrl;
        String allureReportBaseUrl = testJenkins.getJenkinsUrl() + "j_acegi_security_check?j_username="+ testJenkins.getUsername()
                +"&j_password="+ testJenkins.getPassword()+"&Submit=登录&remember_me=on"+"&from=";
        allureReportUrl = allureReportBaseUrl + buildUrl.substring(buildUrl.indexOf("/job"));
        return allureReportUrl;
    }

}
