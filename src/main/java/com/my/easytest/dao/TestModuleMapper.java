package com.my.easytest.dao;

import com.my.easytest.common.MySqlExtensionMapper;
import com.my.easytest.dto.module.QueTestModuleListDto;
import com.my.easytest.entity.TestModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestModuleMapper extends MySqlExtensionMapper<TestModule> {

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueTestModuleListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestModule> list(@Param("params") QueTestModuleListDto params, @Param("pageNum") Integer pageNum,
                          @Param("pageSize") Integer pageSize);
}