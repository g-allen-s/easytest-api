package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.testcase.TestTaskCaseRelDetailDto;
import com.my.easytest.dto.testcase.QueryTestTaskCaseRelListDto;
import com.my.easytest.entity.TestTaskCaseRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTaskCaseRelMapper extends MySqlExtensionMapper<TestTaskCaseRel> {

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestTaskCaseRelDetailDto> listDetail(@Param("params") QueryTestTaskCaseRelListDto params, @Param("pageNum") Integer pageNum,
                                              @Param("pageSize") Integer pageSize);


}