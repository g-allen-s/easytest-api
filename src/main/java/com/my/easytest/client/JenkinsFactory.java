package com.my.easytest.client;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.exception.ServiceException;
import com.my.easytest.dao.TestJenkinsMapper;
import com.my.easytest.entity.TestJenkins;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 获取Jenkins服务
 * @Author G_ALLEN
 * @Date 2021/1/28 8:48
 **/

@Slf4j
@Component
public class JenkinsFactory {

    @Autowired
    private TestJenkinsMapper testJenkinsMapper;

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

    public JenkinsServer getJenkinsServer(Integer createUserId, Integer jenkinsId){
        JenkinsServer jenkinsServer = new JenkinsServer(getJenkinsHttpClient(createUserId, jenkinsId));
        return jenkinsServer;
    }

    /**
     * 获取Jenkins的java客户端的JenkinsServer
     * @return
     */

    public JenkinsServer getJenkinsServer(JenkinsHttpClient jenkinsHttpClient){
        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
        return jenkinsServer;
    }

    /**
     * 获取Jenkins的java客户端的JenkinsHttpClient
     * @return
     */


    public JenkinsHttpClient getJenkinsHttpClient(Integer createUserId, Integer jenkinsId){
        JenkinsHttpClient jenkinsHttpClient = null;
        try {

            TestJenkins result = getTestJenkins(createUserId, jenkinsId);

            log.info("getJenkinsHttpClient--TestJenkins=  "+ JSONObject.toJSONString(result));

            jenkinsHttpClient = new JenkinsHttpClient(new URI(result.getJenkinsUrl()), result.getUsername(), result.getPassword());
        } catch (URISyntaxException e) {
            String tips = "获取Jenkins服务异常"+e.getMessage();
            log.error(tips,e);
            throw new ServiceException(tips);
        }
        return jenkinsHttpClient;
    }

    /**
     * 根据环境信息获取数据库中配置的Jenkins服务信息
     * @return
     */

    public TestJenkins getTestJenkins(Integer createUserId, Integer jenkinsId){


        TestJenkins queryTestJenkins = new TestJenkins();

        queryTestJenkins.setId(jenkinsId);
        queryTestJenkins.setCreateUserId(createUserId);


        TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

        if(Objects.isNull(result)){
            throw new ServiceException("未查到Jenkins信息");
        }

        return result;
    }
}
