package com.my.easytest.common;

/**
 * mybatis的的mapper的统一父类，用于简单sql语句的快速编码
 * @Author G_ALLEN
 * @Date 2021/1/2 15:18
 **/
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MySqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
}
