package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.api.QueTestApiListDto;
import com.my.easytest.entity.*;
import com.my.easytest.model.ApiGen;

import java.util.List;

public interface TestApiService {


	/**
	 *  新增接口信息
	 * @param testApi
	 * @return
	 */
	ResultDto<TestApi> save(TestApi testApi);

	/**
	 *  更新接口信息
	 * @param testApi
	 * @return
	 */
	ResultDto<TestApi> update(TestApi testApi);

	/**
	 *  根据ApieId删除接口
	 * @param apiId
	 * @return
	 */
	ResultDto<TestApi> delete(Integer apiId, TokenDto tokenDto);

	/**
	 *  查询列表信息
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestApi>> list(PageTableRequest<QueTestApiListDto> pageTableRequest);

	/**
	 *  根据id查询接口信息
	 * @param apiId
	 * @return
	 */
	ResultDto<TestApi> getById(Integer apiId, Integer operator);

	/**
	 *  新增接口请求头信息
	 * @param testApiHeader
	 * @return
	 */
	ResultDto<TestApiHeader> save(TestApiHeader testApiHeader, Integer operator);

	/**
	 *  根据Uid删除
	 * @param uid
	 * @return
	 */
	ResultDto<TestApiHeader> deleteHeader(String uid, Integer operator);

	/**
	 *  新增接口请求参数信息
	 * @param testApiParam
	 * @return
	 */
	ResultDto<TestApiParam> save(TestApiParam testApiParam, Integer operator);

	/**
	 *  根据Uid删除
	 * @param uid
	 * @return
	 */
	ResultDto<TestApiParam> deleteParam(String uid, Integer operator);

	/**
	 *  新增接口全局变量信息
	 * @param globalVariable
	 * @return
	 */
	ResultDto<TestApiGlobalVariable> save(TestApiGlobalVariable globalVariable, Integer operator);

	/**
	 *  根据Uid删除
	 * @param uid
	 * @return
	 */
	ResultDto<TestApiGlobalVariable> deleteGlobal(String uid, Integer operator);

	/**
	 *  新增接口操作信息
	 * @param testApiAction
	 * @return
	 */
	ResultDto<TestApiAction> save(TestApiAction testApiAction, Integer operator);

    /**
     *  运行接口action
     * @param apiId
     * @return
     */
    ResultDto run(Integer apiId, String env, Integer operator);

    /**
     *  导入接口
     * @param apiGen
     * @return
     */
    ResultDto generateTestApiKit(ApiGen apiGen, Integer operator);

	void saveGlobalParams(Integer moduleId, List<TestApiGlobalVariable> globalParams, Integer operator);
}
