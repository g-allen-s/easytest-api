package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.ApiTestDataPool;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTestDataPoolMapper extends MySqlExtensionMapper<ApiTestDataPool> {
}