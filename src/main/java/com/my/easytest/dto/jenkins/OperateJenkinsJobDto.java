package com.my.easytest.dto.jenkins;

import com.my.easytest.dto.TokenDto;
import com.my.easytest.entity.TestJenkins;
import lombok.Data;

import java.util.Map;

/**
 * @Author G_ALLEN
 * @Date 2021/2/6 13:09
 **/
@Data
public class OperateJenkinsJobDto {


    private TokenDto tokenDto;


    private TestJenkins testJenkins;

    //构建参数
    private Map<String, String> params;

}
