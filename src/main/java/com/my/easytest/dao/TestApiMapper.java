package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.api.QueTestApiListDto;
import com.my.easytest.entity.TestApi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestApiMapper extends MySqlExtensionMapper<TestApi> {

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueTestApiListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestApi> list(@Param("params") QueTestApiListDto params, @Param("pageNum") Integer pageNum,
                       @Param("pageSize") Integer pageSize);
}