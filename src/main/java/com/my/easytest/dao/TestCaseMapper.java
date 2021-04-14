package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.testcase.QueryTestCaseListDto;
import com.my.easytest.entity.TestCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseMapper extends MySqlExtensionMapper<TestCase> {

    List<TestCase> getByIdList(@Param("createUserId") Integer createUserId, @Param("list") List<Integer> list);

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryTestCaseListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestCase> list(@Param("params") QueryTestCaseListDto params, @Param("pageNum") Integer pageNum,
                        @Param("pageSize") Integer pageSize);
}