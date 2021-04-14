package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.ApiTestAction;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTestActionMapper extends MySqlExtensionMapper<ApiTestAction> {
}