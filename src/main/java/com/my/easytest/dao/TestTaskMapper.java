package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.task.QueryTestTaskListDto;
import com.my.easytest.entity.TestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTaskMapper extends MySqlExtensionMapper<TestTask> {
    Integer count(@Param("params") QueryTestTaskListDto params);

    List<TestTask> list(@Param("params") QueryTestTaskListDto params, @Param("pageNum") Integer pageNum,
                        @Param("pageSize") Integer pageSize);
}