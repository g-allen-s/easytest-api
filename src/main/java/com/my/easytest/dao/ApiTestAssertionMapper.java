package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.ApiTestAssertion;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTestAssertionMapper extends MySqlExtensionMapper<ApiTestAssertion> {
}