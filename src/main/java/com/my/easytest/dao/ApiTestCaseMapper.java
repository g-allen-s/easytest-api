package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.api.QueTestCaseListDto;
import com.my.easytest.entity.ApiTestCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiTestCaseMapper extends MySqlExtensionMapper<ApiTestCase> {
    ApiTestCase selectOneByTcCodeNotId(@Param("apiTcCode") String str, @Param("id") Integer id);

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueTestCaseListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ApiTestCase> list(@Param("params") QueTestCaseListDto params, @Param("pageNum") Integer pageNum,
                           @Param("pageSize") Integer pageSize);


}