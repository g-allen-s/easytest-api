package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.TestUser;
import org.springframework.stereotype.Repository;

@Repository
public interface TestUserMapper extends MySqlExtensionMapper<TestUser> {
//    int updateDefaultJenkinsId(TestUser testUser);
}