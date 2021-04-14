package com.my.easytest.common;

import com.my.easytest.dto.TokenDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 16:36
 **/

@Component
public class TokenDb {

    private Map<String, TokenDto> tokenMap = new HashMap<>();

    public int getTokenMapSize() {
        return tokenMap.size();
    }

    public TokenDto getTokenDto(String token){

        if(StringUtils.isEmpty(token)){
            return new TokenDto();
        }

        return tokenMap.get(token);
    }

    public TokenDto addTokenDto(String token,TokenDto tokenDto){

        if(Objects.isNull(tokenDto)){
            return tokenDto;
        }

        return tokenMap.put(token, tokenDto);
    }

    public TokenDto removeTokenDto(String token){

        if(Objects.isNull(token)){
            return null;
        }

        return tokenMap.remove(token);
    }

    public boolean isLogin(String token){
        return tokenMap.get(token) != null;
    }


}
