package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.TestApiHeader;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestApiHeaderMapper extends MySqlExtensionMapper<TestApiHeader> {
    List<TestApiHeader> selectById(@Param("id") Integer id, @Param("headerType") String headerType);
}