package com.my.easytest.dto;

import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 16:38
 **/

@Data
public class TokenDto {

    private Integer userId;

    private String userName;

    private Integer defaultJenkinsId;

    private String token;


}
