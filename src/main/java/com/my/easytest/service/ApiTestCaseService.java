package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.api.QueTestCaseListDto;
import com.my.easytest.entity.*;
import com.my.easytest.model.ApiTestGen;

public interface ApiTestCaseService {

    /**
     *  新增接口用例信息
     * @param apiTestCase
     * @return
     */
    ResultDto<ApiTestCase> save(ApiTestCase apiTestCase);

    /**
     *  更新接口用例信息
     * @param apiTestCase
     * @return
     */
    ResultDto<ApiTestCase> update(ApiTestCase apiTestCase, Integer operator);

    /**
     *  根据TestCaseId删除接口用例
     * @param testCaseId
     * @return
     */
    ResultDto<ApiTestCase> delete(Integer testCaseId, Integer operator);

    /**
     *  查询列表信息
     * @param pageTableRequest
     * @return
     */
    ResultDto<PageTableResponse<ApiTestCase>> list(PageTableRequest<QueTestCaseListDto> pageTableRequest);

    /**
     *  新增接口前置测试
     * @param preTest
     * @return
     */
    ResultDto<ApiPreTest> save(ApiPreTest preTest, Integer operator);

    /**
     *  根据Uid删除前置测试
     * @param uid
     * @return
     */
    ResultDto<ApiPreTest> deletePreTest(String uid, Integer operator);

    /**
     *  新增接口测试步骤
     * @param testAction
     * @return
     */
    ResultDto<ApiTestAction> save(ApiTestAction testAction);

    /**
     *  根据Uid删除测试步骤
     * @param uid
     * @return
     */
    ResultDto<ApiTestAction> deleteTestAction(String uid, Integer operator);

    /**
     *  新增接口测试断言
     * @param testAssertion
     * @return
     */
    ResultDto<ApiTestAssertion> save(ApiTestAssertion testAssertion);

    /**
     *  根据Uid删除测试断言
     * @param uid
     * @return
     */
    ResultDto<ApiTestAssertion> deleteTestAssertion(String uid, Integer operator);

    /**
     *  新增接口测试取样
     * @param testSample
     * @return
     */
    ResultDto<ApiTestSample> save(ApiTestSample testSample);

    /**
     *  根据Uid删除测试取样
     * @param uid
     * @return
     */
    ResultDto<ApiTestSample> deleteTestSample(String uid, Integer operator);

    /**
     *  新增接口测试数据
     * @param testDataPool
     * @return
     */
    ResultDto<ApiTestDataPool> save(ApiTestDataPool testDataPool);

    /**
     *  根据Uid删除测试数据
     * @param uid
     * @return
     */
    ResultDto<ApiTestDataPool> deleteTestDataPool(String uid, Integer operator);

    /**
     *  运行接口测试用例
     * @param tcId
     * @return
     */
    ResultDto run(Integer tcId, String env, Integer operator);

    /**
     *  导入接口测试
     * @param apiTestGen
     * @return
     */
    ResultDto generateApiTestKit(ApiTestGen apiTestGen, Integer operator);
}
