package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.TestApiParam;
import org.springframework.stereotype.Repository;

@Repository
public interface TestApiParamMapper extends MySqlExtensionMapper<TestApiParam> {
}