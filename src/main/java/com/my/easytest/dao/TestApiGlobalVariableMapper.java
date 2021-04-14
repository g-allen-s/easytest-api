package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.TestApiGlobalVariable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestApiGlobalVariableMapper extends MySqlExtensionMapper<TestApiGlobalVariable> {
    List<TestApiGlobalVariable> selectById(@Param("id") Integer id);
}