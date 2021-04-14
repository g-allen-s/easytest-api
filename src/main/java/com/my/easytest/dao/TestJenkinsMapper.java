package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.jenkins.QueryTestJenkinsListDto;
import com.my.easytest.entity.TestJenkins;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestJenkinsMapper extends MySqlExtensionMapper<TestJenkins> {

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryTestJenkinsListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestJenkins> list(@Param("params") QueryTestJenkinsListDto params, @Param("pageNum") Integer pageNum,
    @Param("pageSize") Integer pageSize);
}