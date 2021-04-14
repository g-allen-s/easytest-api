package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.ApiTestSample;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTestSampleMapper extends MySqlExtensionMapper<ApiTestSample> {
}