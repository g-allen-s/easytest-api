package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.entity.TestApiAction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestApiActionMapper extends MySqlExtensionMapper<TestApiAction> {
    List<TestApiAction> selectById(@Param("id") Integer id);
}