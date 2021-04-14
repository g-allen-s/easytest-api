package com.my.easytest.service.impl;

import com.my.easytest.client.RestassuredClient;
import com.my.easytest.model.result.TestActionResult;
import com.my.easytest.model.result.TestDDTResult;
import com.my.easytest.model.result.TestResult;
import com.my.easytest.common.TokenDb;
import com.my.easytest.dao.*;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.api.QueTestCaseListDto;
import com.my.easytest.entity.*;
import com.my.easytest.model.ApiTest;
import com.my.easytest.model.ApiTestActionGen;
import com.my.easytest.model.ApiTestGen;
import com.my.easytest.service.ApiTestCaseService;
import com.my.easytest.service.TestApiService;
import com.my.easytest.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiTestCaseServiceImpl implements ApiTestCaseService {

    @Autowired
    private ApiTestCaseMapper apiTestCaseMapper;

    @Autowired
    private ApiPreTestMapper apiPreTestMapper;

    @Autowired
    private ApiTestActionMapper apiTestActionMapper;

    @Autowired
    private ApiTestAssertionMapper apiTestAssertionMapper;

    @Autowired
    private ApiTestSampleMapper apiTestSampleMapper;

    @Autowired
    private ApiTestDataPoolMapper apiTestDataPoolMapper;

    @Autowired
    private TestApiGlobalVariableMapper apiGlobalVariableMapper;

    @Autowired
    private TestApiMapper apiMapper;

    @Autowired
    private TestApiHeaderMapper apiHeaderMapper;

    @Autowired
    private TestApiParamMapper apiParamMapper;

    @Autowired
    private TestApiActionMapper apiActionMapper;

    @Autowired
    private TestModuleMapper testModuleMapper;

    @Autowired
    private TokenDb tokenDb;

    @Autowired
    private TestApiService testApiService;

    final static String TEST_PASSED = "Test Passed";

    final static String TEST_FAILED = "Test Failed";

    final static String TEST_SKIPPED = "Test Skipped";


    /**
     * 新增接口用例信息
     *
     * @param testCase
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestCase> save(ApiTestCase testCase) {

        ApiTestCase qureyParam = new ApiTestCase();
        qureyParam.setApiTcCode(testCase.getApiTcCode());
        ApiTestCase storedTestCase = apiTestCaseMapper.selectOne(qureyParam);

        if(Objects.nonNull(storedTestCase)){
            return ResultDto.fail("用例["+testCase.getApiTcCode()+"]已存在！");
        }

        testCase.setApiTcStatus((byte) 1);
        testCase.setCreateTime(new Date());
        testCase.setUpdateTime(new Date());
        apiTestCaseMapper.insertUseGeneratedKeys(testCase);

        return ResultDto.success("添加成功", testCase);
    }

    /**
     * 修改接口用例信息
     *
     * @param testCase
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestCase> update(ApiTestCase testCase, Integer operator) {

        ApiTestCase qureyParam = new ApiTestCase();
        qureyParam.setId(testCase.getId());
        ApiTestCase storedTestCase = apiTestCaseMapper.selectOne(qureyParam);
        if(Objects.isNull(storedTestCase)){
            return ResultDto.fail("未查到接口用例信息");
        }

        if(operator != storedTestCase.getCreateUserId()){
            return ResultDto.success("不可修改该接口用例");
        }

        ApiTestCase takenTestCase = apiTestCaseMapper.selectOneByTcCodeNotId(testCase.getApiTcCode(), testCase.getId());
        if(Objects.nonNull(takenTestCase)){
            return ResultDto.fail("用例["+testCase.getApiTcCode()+"]已存在！");
        }

        testCase.setApiTcStatus((byte) 1);
        testCase.setCreateUserId(storedTestCase.getCreateUserId());
        testCase.setCreateTime(storedTestCase.getCreateTime());
        testCase.setUpdateTime(new Date());

        apiTestCaseMapper.updateByPrimaryKey(testCase);

        return ResultDto.success("更新成功");
    }

    /**
     * 根据TestCaseId删除接口用例
     *
     * @param testCaseId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestCase> delete(Integer testCaseId, Integer operator) {

        ApiTestCase qureyParam = new ApiTestCase();
        qureyParam.setId(testCaseId);
        qureyParam.setCreateUserId(operator);
        ApiTestCase storedTestCase = apiTestCaseMapper.selectOne(qureyParam);
        if(Objects.isNull(storedTestCase)){
            return ResultDto.fail("未查到用例信息");
        }

        apiTestCaseMapper.deleteByPrimaryKey(storedTestCase);

        return ResultDto.success("删除成功");
    }

    /**
     * 查询用例信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<ApiTestCase>> list(PageTableRequest<QueTestCaseListDto> pageTableRequest) {

        QueTestCaseListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal = apiTestCaseMapper.count(params);

        //分页查询数据
        List<ApiTestCase> testApis = apiTestCaseMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<ApiTestCase> pageTableResponse = new PageTableResponse<>();
        pageTableResponse.setRecordsTotal(recordsTotal);
        pageTableResponse.setData(testApis);

        return ResultDto.success("查询成功", pageTableResponse);
    }

    /**
     * 新增接口前置测试
     *
     * @param preTest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiPreTest> save(ApiPreTest preTest, Integer operator){

        preTest.setUid(UUID.randomUUID().toString().replace("-", ""));
        apiPreTestMapper.insert(preTest);

        return ResultDto.success("添加成功", preTest);
    }

    /**
     * 根据Uid删除前置测试
     *
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiPreTest> deletePreTest(String uid, Integer operator) {

        ApiPreTest qureyParam = new ApiPreTest();
        qureyParam.setUid(uid);
        ApiPreTest storedPreTest= apiPreTestMapper.selectOne(qureyParam);
        if(Objects.isNull(storedPreTest)){
            return ResultDto.fail("未查到前置测试信息");
        }

        apiPreTestMapper.deleteByPrimaryKey(uid);

        return ResultDto.success("删除成功");
    }

    /**
     * 新增接口测试步骤
     *
     * @param testAction
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestAction> save(ApiTestAction testAction){

        testAction.setUid(UUID.randomUUID().toString().replace("-", ""));
        testAction.setCreateTime(new Date());
        apiTestActionMapper.insert(testAction);

        return ResultDto.success("添加成功", testAction);
    }

    /**
     * 根据Uid删除测试步骤
     *
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestAction> deleteTestAction(String uid, Integer operator) {

        ApiTestAction qureyParam = new ApiTestAction();
        qureyParam.setUid(uid);
        qureyParam.setCreateUserId(operator);
        ApiTestAction storedTestAction= apiTestActionMapper.selectOne(qureyParam);
        if(Objects.isNull(storedTestAction)){
            return ResultDto.fail("未查到测试步骤信息");
        }

        apiTestActionMapper.deleteByPrimaryKey(uid);

        return ResultDto.success("删除成功");
    }

    /**
     * 新增接口测试断言
     *
     * @param testAssertion
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestAssertion> save(ApiTestAssertion testAssertion){

        testAssertion.setUid(UUID.randomUUID().toString().replace("-", ""));
        apiTestAssertionMapper.insert(testAssertion);

        return ResultDto.success("添加成功", testAssertion);
    }

    /**
     * 根据Uid删除测试断言
     *
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestAssertion> deleteTestAssertion(String uid, Integer operator) {

        ApiTestAssertion qureyParam = new ApiTestAssertion();
        qureyParam.setUid(uid);
        if(Objects.isNull(apiTestAssertionMapper.selectOne(qureyParam))){
            return ResultDto.fail("未查到测试断言信息");
        }

        apiTestAssertionMapper.deleteByPrimaryKey(uid);

        return ResultDto.success("删除成功");
    }

    /**
     * 新增接口测试取样
     *
     * @param testSample
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestSample> save(ApiTestSample testSample){

        testSample.setUid(UUID.randomUUID().toString().replace("-", ""));
        apiTestSampleMapper.insert(testSample);

        return ResultDto.success("添加成功", testSample);
    }

    /**
     * 根据Uid删除测试取样
     *
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestSample> deleteTestSample(String uid, Integer operator) {

        ApiTestSample qureyParam = new ApiTestSample();
        qureyParam.setUid(uid);
        if(Objects.isNull(apiTestSampleMapper.selectOne(qureyParam))){
            return ResultDto.fail("未查到测试取样信息");
        }

        apiTestSampleMapper.deleteByPrimaryKey(uid);

        return ResultDto.success("删除成功");
    }

    /**
     * 新增接口测试数据
     *
     * @param testDataPool
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestDataPool> save(ApiTestDataPool testDataPool){

        testDataPool.setUid(UUID.randomUUID().toString().replace("-", ""));
        apiTestDataPoolMapper.insert(testDataPool);

        return ResultDto.success("添加成功", testDataPool);
    }

    /**
     * 根据Uid删除测试数据
     *
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<ApiTestDataPool> deleteTestDataPool(String uid, Integer operator) {

        ApiTestDataPool qureyParam = new ApiTestDataPool();
        qureyParam.setUid(uid);
        if(Objects.isNull(apiTestDataPoolMapper.selectOne(qureyParam))){
            return ResultDto.fail("未查到测试取样信息");
        }

        apiTestDataPoolMapper.deleteByPrimaryKey(uid);

        return ResultDto.success("删除成功");
    }

    /**
     *  运行接口测试用例
     * @param tcId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto run(Integer tcId, String env, Integer operator)
    {
        if(1==1) return ResultDto.fail("test");
        // 根据testCaseId查询出testCase对象
        ApiTestCase apiTestCase = apiTestCaseMapper.selectOne(new ApiTestCase(tcId));
        // 根据moduleId查询出module对象
        TestModule apiModule = testModuleMapper.selectOne(new TestModule(apiTestCase.getApiModule()));
        // 根据module+env获取host
        String host = this.getHost(apiModule, env);
        // 查询出全局参数并转换成键值对
        List<TestApiGlobalVariable> globalParams = apiGlobalVariableMapper.select(new TestApiGlobalVariable(apiTestCase.getApiModule()));
        Map<String, Object> globalParam = transformGlobalParamList2Map(globalParams);
        List<ApiPreTest> preTests = apiPreTestMapper.select(new ApiPreTest(tcId));
        List<ApiTestDataPool> testDataPool = apiTestDataPoolMapper.select(new ApiTestDataPool(tcId));
        List<ApiTestAction> testActions = apiTestActionMapper.select(new ApiTestAction(tcId))
                .stream().sorted(Comparator.comparing(ApiTestAction::getCreateTime)).collect(Collectors.toList());
        TestResult testResult = this.runApiTestCase(globalParam, host, preTests, testDataPool, testActions);
        testResult.setTestName(apiTestCase.getApiTcName());
        return ResultDto.success("执行成功！");
    }

    private TestResult runApiTestCase(Map<String, Object> runtimeGlobalParams, String host, List<ApiPreTest> apiPreTests, List<ApiTestDataPool> testDataPool, List<ApiTestAction> testActions)
    {
        System.out.println("=== run api testCase in default way");
        // 【step1】执行前置条件
        if (null == apiPreTests || apiPreTests.size() == 0) System.out.println("===没有可执行的前置条件！");
        Iterator<ApiPreTest> preTestIterator = apiPreTests.iterator();
        while(preTestIterator.hasNext()) {
            ApiPreTest preTest = preTestIterator.next();
            System.out.println("=== run before test @" + preTest.getUid());
            // 判断操作方式：接口请求 or 数据库操作
            if (preTest.getPreTestMethod().equals("api")) {
                try {
                    this.runBeforeTest(preTest, host, runtimeGlobalParams);
                    System.out.println("BeforeTest["+preTest.getPreTestMethod()+"]");
                } catch (Exception e) {
                    System.out.println("BeforeTest["+preTest.getPreTestMethod()+"]-run exception:: "+e.getMessage());
                    return new TestResult("", TEST_SKIPPED, "Exception while running beforeTest: " + e.getMessage());
                }
            } else continue; // todo
        } /* 前置条件执行完成，取样参数存入{runtimeGlobalParams} */

        // 【step2】数据驱动-遍历数据集，每一条数据驱动一次测试运行
        if (null == testDataPool || testDataPool.size() == 0) {
            return new TestResult("", TEST_SKIPPED, "No Test-Data-Driver to execute test case error!");
        }
        Map<String, TestApi> apiCache = new HashMap<String, TestApi>();
        Map<String, List<TestApiHeader>> headersCache = new HashMap<String, List<TestApiHeader>>();
        Map<String, List<TestApiParam>> paramsCache = new HashMap<String, List<TestApiParam>>();
        Map<String, List<ApiTestAssertion>> assertionsCache = new HashMap<String, List<ApiTestAssertion>>();
        Map<String, List<ApiTestSample>> samplesCache = new HashMap<String, List<ApiTestSample>>();
        TestResult testResult = new TestResult();
        testResult.setTestResult(TEST_PASSED);
        List<TestDDTResult> testDDTResults = new ArrayList<TestDDTResult>();
        Iterator<ApiTestDataPool> dataPoolIterator = testDataPool.iterator();
        while(dataPoolIterator.hasNext()) {
            TestDDTResult testDDTResult = new TestDDTResult();
            testDDTResult.setTestResult(TEST_PASSED);
            // 每一轮测试运行初始化一次全局参数
            Map<String, Object> roundGlobalParams = new HashMap<String, Object>();
            roundGlobalParams.putAll(runtimeGlobalParams);
            // 行数据驱动一轮测试执行
            ApiTestDataPool ddt = dataPoolIterator.next();
            JSONObject dataDriver = JSONObject.fromObject(ddt.getDatapoolParam());
            System.out.println("=== run test driven by ddt @" + ddt.getUid());
            List<TestActionResult> testActionResultList = new ArrayList<TestActionResult>();
            // 遍历test action
            Iterator<ApiTestAction> actionIterator = testActions.iterator();
            while(actionIterator.hasNext()) {
                // 迭代测试用例每一步action（对应一个接口操作）
                ApiTestAction testAction = actionIterator.next();
                System.out.println("=== run test action @" + testAction.getUid());
                try {
                    TestActionResult testActionResult = this.runTestAction(testAction, dataDriver, host, roundGlobalParams, apiCache, headersCache, paramsCache, assertionsCache, samplesCache);
                    testActionResult.setTestActionName(testAction.getTestActionName());
                    testActionResultList.add(testActionResult);
                    if (!testActionResult.getTestResult().equals(TEST_PASSED)) {
                        testDDTResult.setTestResult(TEST_FAILED);
                        testResult.setTestResult(TEST_FAILED);
                    }
                    System.out.println("TestAction["+testAction.getTestActionName()+"]-run result:: "+testActionResult);
                } catch (Exception e) {
                    testActionResultList.add(new TestActionResult(testAction.getTestActionName(), TEST_FAILED,
                            "Exception while running testAction: " + e.getMessage()));
                    testDDTResult.setTestResult(TEST_FAILED);
                    testResult.setTestResult(TEST_FAILED);
                    System.out.println("TestAction["+testAction.getTestActionName()+"]-run exception:: "+e.getMessage());
                }
            }
            testDDTResult.setDdtName(ddt.getDatapoolName());
            testDDTResult.setTestActionResultList(testActionResultList);
            testDDTResults.add(testDDTResult);
        }
        testResult.setTestDDTResult(testDDTResults);
        return testResult;
    }

    private void runBeforeTest(ApiPreTest preTest, String host, Map<String, Object> runtimeGlobalParams) throws Exception
    {
        // 获取请求接口
        String apiCode = preTest.getPreTestTarget();
        TestApi api = apiMapper.selectOne(new TestApi(apiCode));
        // 获取请求头
        List<TestApiHeader> headers = apiHeaderMapper.select(new TestApiHeader(api.getId(), "request_header"));
        // 获取请求参数
        List<TestApiParam> params = apiParamMapper.select(new TestApiParam(api.getId(), "request_param"));
        params.addAll(apiParamMapper.select(new TestApiParam(api.getId(), "request_body")));
        // 根据apiId获取步骤列表
        List<TestApiAction> apiActionSteps = this.getApiActionSteps(
                JSONObject.fromObject(preTest.getRequestParam()), JSONObject.fromObject(preTest.getSamplesParam()), transformApiParamList2NameTypeMap(params));
        //List<TestApiAction> apiActionSteps = apiActionMapper.select(new TestApiAction(api.getId()));
        System.out.println("before test onging");
        try {
            RestassuredClient restassuredClient = new RestassuredClient();
            ResultDto result = restassuredClient
                    .preHandle(api, host, headers, apiActionSteps, runtimeGlobalParams)
                    .send().assertResponse(runtimeGlobalParams);
            System.out.println("preTestResult::"+ result.getResultCode());
        } catch (Exception e) {
            throw new Exception("e.getMessage()");
        }
    }
    private List<TestApiAction> getApiActionSteps(JSONObject requestParam, JSONObject sampleParam, Map<String, Object> apiParams)
    {
        List<TestApiAction> apiActionSteps = new ArrayList<TestApiAction>();

        if (null != requestParam && !requestParam.isEmpty()) {
            for (Object each: requestParam.keySet()) {
                String key = (String) each;
                String requestType = (String) apiParams.get(key);

                TestApiAction apiActionStep = new TestApiAction();
                apiActionStep.setStepKey(key);
                apiActionStep.setStepValue((String) requestParam.get(key));
                if (requestType.equalsIgnoreCase("request_param"))
                    apiActionStep.setStepKeyword("setParam");
                else if (requestType.equalsIgnoreCase("request_body"))
                    apiActionStep.setStepKeyword("setBodyParam");
                else continue;
                apiActionSteps.add(apiActionStep);
            }
        }

        if (null != sampleParam && !sampleParam.isEmpty()) {
            for (Object each: sampleParam.keySet()) {
                String key = (String) each;
                TestApiAction apiActionStep = new TestApiAction();
                apiActionStep.setStepKey(key);
                apiActionStep.setStepValue((String) sampleParam.get(key));
                apiActionStep.setStepKeyword("getResponse");
                apiActionSteps.add(apiActionStep);
            }
        }
        return apiActionSteps;
    }

    private TestActionResult runTestAction(ApiTestAction testAction, JSONObject dataDriver, String host, Map<String, Object> roundGlobalParams, Map<String, TestApi> apiCache, Map<String, List<TestApiHeader>> headersCache, Map<String, List<TestApiParam>> paramsCache, Map<String, List<ApiTestAssertion>> assertionsCache, Map<String, List<ApiTestSample>> samplesCache) throws Exception
    {
        String testActionId = testAction.getUid();
        Integer apiId = testAction.getApiId();
        // 获取Api
        TestApi api = (TestApi) MapUtils.getObject(apiCache, testActionId);
        if (null == api) {
            api = apiMapper.selectOne(new TestApi(apiId));
            apiCache.put(testActionId, api);
            System.out.println("=== get api for action @" + testActionId + " from database");
        }
        // 获取请求头
        List<TestApiHeader> headers = (List<TestApiHeader>) MapUtils.getObject(headersCache, testActionId);
        if (null == headers) {
            headers = apiHeaderMapper.select(new TestApiHeader(apiId, "request_header"));
            headersCache.put(testActionId, null == headers ? new ArrayList<TestApiHeader>() : headers);
            System.out.println("=== get header list for action @" + testActionId + " from database");
        }
        // 获取接口参数
        List<TestApiParam> apiParams = (List<TestApiParam>) MapUtils.getObject(paramsCache, testActionId);
        if (null == apiParams) {
            apiParams = apiParamMapper.select(new TestApiParam(apiId, "request_param"));
            apiParams.addAll(apiParamMapper.select(new TestApiParam(apiId, "request_body")));
            paramsCache.put(testActionId, null == apiParams ? new ArrayList<TestApiParam>() : apiParams);
            System.out.println("=== get param list for action @" + testActionId + " from database");
        }
        // 获取步骤断言
        List<ApiTestAssertion> assertions = (List<ApiTestAssertion>) MapUtils.getObject(assertionsCache, testActionId);
        if (null == assertions) {
            assertions = apiTestAssertionMapper.select(new ApiTestAssertion(testActionId));
            assertionsCache.put(testActionId, null == assertions ? new ArrayList<ApiTestAssertion>() : assertions);
            System.out.println("=== get assertion list for action @" + apiId + " from database");
        }
        // 获取取样参数
        List<ApiTestSample> samples = (List<ApiTestSample>) MapUtils.getObject(samplesCache, testActionId);
        if (null == samples) {
            samples = apiTestSampleMapper.select(new ApiTestSample(testActionId));
            samplesCache.put(testActionId, null == samples ? new ArrayList<ApiTestSample>() : samples);
            System.out.println("=== get sample list for action @" + testActionId + " from database");
        }
        // 根据apiId获取步骤列表
        List<TestApiAction> apiActionSteps = this.getApiActionSteps(
                dataDriver, assertions, samples, transformApiParamList2NameTypeMap(apiParams));
        //List<TestApiAction> apiActionSteps = apiActionMapper.selectById(api.getId());
        try {
            RestassuredClient restassuredClient = new RestassuredClient();
            ResultDto result = restassuredClient
                    .preHandle(api, host, headers, apiActionSteps, roundGlobalParams)
                    .send().assertResponse(roundGlobalParams);
            System.out.println("runTestResult::"+ result.getResultCode());
        } catch (Exception e) {
            throw new Exception("e.getMessage()");
        }
        TestActionResult testActionResult = new TestActionResult();
        return testActionResult;
    }
    private List<TestApiAction> getApiActionSteps(JSONObject dataDriver, List<ApiTestAssertion> assertions, List<ApiTestSample> samples, Map<String, Object> apiParams) {
        List<TestApiAction> apiActionSteps = new ArrayList<TestApiAction>();
        // 根据接口的请求参数去数据驱动中找映射
        if (null != apiParams && !apiParams.isEmpty()) {
            for (Object each: apiParams.keySet()) {
                String key = (String) each; // key from apiParam
                Object data = dataDriver.get(key); // value from dataDriver
                if (null != data) {
                    TestApiAction apiActionStep = new TestApiAction();
                    apiActionStep.setStepKey(key);
                    apiActionStep.setStepValue((String) data);
                    String requestType = (String) apiParams.get(key); // type from apiParam
                    if (requestType.equalsIgnoreCase("request_param"))
                        apiActionStep.setStepKeyword("setParam");
                    else if (requestType.equalsIgnoreCase("request_body"))
                        apiActionStep.setStepKeyword("setBodyParam");
                    else continue;
                    apiActionSteps.add(apiActionStep);
                }
            }
        }
        // 断言转换成测试步骤
        Iterator<ApiTestAssertion> assertionIterator = assertions.iterator();
        while(assertionIterator.hasNext()) {
            ApiTestAssertion assertion = assertionIterator.next();
            TestApiAction apiActionStep = new TestApiAction();
            apiActionStep.setStepKey(assertion.getAssertionKey());
            apiActionStep.setStepValue(assertion.getAssertionValue());
            apiActionStep.setStepKeyword(assertion.getAssertionType());
            String expectedValue = assertion.getAssertionValue();
            if (expectedValue.startsWith("&") && expectedValue.endsWith("&")) {
                Object target = dataDriver.get(expectedValue.substring(1, expectedValue.lastIndexOf("&")));
                if (null != target) apiActionStep.setStepValue((String) target);
            }
            apiActionSteps.add(apiActionStep);
        }
        // 取样参数转换成测试步骤
        Iterator<ApiTestSample> sampleIterator = samples.iterator();
        while(sampleIterator.hasNext()) {
            ApiTestSample sample = sampleIterator.next();
            TestApiAction apiActionStep = new TestApiAction();
            apiActionStep.setStepKey(sample.getSampleSource());
            apiActionStep.setStepValue(sample.getSampleTarget());
            apiActionStep.setStepKeyword("getResponse");
            apiActionSteps.add(apiActionStep);
        }
        return apiActionSteps;
    }


    private String getHost(TestModule apiModule, String env) {
        Method[] apiModuleMethods = TestModule.class.getMethods();
        for (Method m : apiModuleMethods) {
            if (m.getName().startsWith("get") && m.getName().contains(env)) {
                try {
                    return (String) m.invoke(apiModule);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Map<String, Object> transformGlobalParamList2Map(List<TestApiGlobalVariable> globalParams) {
        Map<String, Object> globalParamMap = new HashMap<String, Object>();
        Iterator<TestApiGlobalVariable> iterator = globalParams.iterator();
        while(iterator.hasNext()) {
            TestApiGlobalVariable apiGlobalParam = iterator.next();
            globalParamMap.put(apiGlobalParam.getVariableKey(), apiGlobalParam.getVariableValue());
        }
        return globalParamMap;
    }

    private Map<String, Object> transformApiParamList2NameTypeMap(List<TestApiParam> apiParams) {
        Map<String, Object> ApiParamMap = new HashMap<String, Object>();
        Iterator<TestApiParam> iterator = apiParams.iterator();
        while(iterator.hasNext()) {
            TestApiParam apiParam = iterator.next();
            ApiParamMap.put(apiParam.getParamName(), apiParam.getParamType());
        }
        return ApiParamMap;
    }


    /**
     *  导入接口测试
     *
     * @param apiTestGen
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto generateApiTestKit(ApiTestGen apiTestGen, Integer operator) {
        Integer moduleId = apiTestGen.getModuleId();
        testApiService.saveGlobalParams(moduleId, apiTestGen.getGlobalParams(), operator);
        List<ApiTest> apiTcList = apiTestGen.getApiTestCases();
        Iterator<ApiTest> iterator = apiTcList.iterator();
        while(iterator.hasNext()) {
            ApiTest apiTest = iterator.next();
            ApiTestCase apiTestCase = new ApiTestCase();
            CopyUtil.copyPropertiesCglib(apiTest, apiTestCase); // ApiTestModel to ApiTestCaseEntity
            apiTestCase.setApiModule(moduleId);
            apiTestCase.setCreateUserId(operator);
            ResultDto saveTestResult;
            try {
                saveTestResult = this.save(apiTestCase);
                if (saveTestResult.getResultCode() == 0) {
                    return saveTestResult;
                }
                apiTest.setApiTcId(((ApiTestCase) saveTestResult.getData()).getId());
                this.savePreTests(apiTest).saveTestDataPool(apiTest).saveTestActions(apiTest, operator);
            } catch (Exception e) {
                return ResultDto.fail("Exception::"+e.getMessage());
            }
        }
        return ResultDto.success("导入成功！");
    }
    private ApiTestCaseServiceImpl savePreTests(ApiTest apiTest) {
        if(Objects.isNull(apiTest.getPreTests())) return this;
        Integer apiTcId = apiTest.getApiTcId();
        List<ApiPreTest> preTests = apiTest.getPreTests();
        Iterator<ApiPreTest> iterator = preTests.iterator();
        while(iterator.hasNext()) {
            ApiPreTest preTest = iterator.next();
            preTest.setTcId(apiTcId);
            this.save(preTest, 100);
        }
        return this;
    }
    private ApiTestCaseServiceImpl saveTestDataPool(ApiTest apiTest) {
        if(Objects.isNull(apiTest.getTestDataPool())) return this;
        Integer apiTcId = apiTest.getApiTcId();
        List<ApiTestDataPool> dataPool = apiTest.getTestDataPool();
        Iterator<ApiTestDataPool> iterator = dataPool.iterator();
        while(iterator.hasNext()) {
            ApiTestDataPool testData = iterator.next();
            testData.setTcId(apiTcId);
            this.save(testData);
        }
        return this;
    }
    private ApiTestCaseServiceImpl saveTestActions(ApiTest apiTest, Integer operator) {
        if(Objects.isNull(apiTest.getTestActions())) return this;
        Integer apiTcId = apiTest.getApiTcId();
        List<ApiTestActionGen> testActions = apiTest.getTestActions();
        Iterator<ApiTestActionGen> iterator = testActions.iterator();
        while(iterator.hasNext()) {
            ApiTestActionGen testActionGen = iterator.next();
            ApiTestAction testAction = new ApiTestAction();
            CopyUtil.copyPropertiesCglib(testActionGen, testAction);
            String apiCode = testActionGen.getApiCode();
            TestApi api = apiMapper.selectOne(new TestApi(apiCode));
            testAction.setApiId(api.getId());
            testAction.setTcId(apiTcId);
            testAction.setCreateUserId(operator);
            ResultDto saveActionResult = this.save(testAction);
            testActionGen.setUid(((ApiTestAction) saveActionResult.getData()).getUid());
            this.saveTestActionAssertions(testActionGen).saveTestActionSamples(testActionGen);
        }
        return this;
    }
    private ApiTestCaseServiceImpl saveTestActionAssertions(ApiTestActionGen apiTestActionGen) {
        if(Objects.isNull(apiTestActionGen.getTestAssertions())) return this;
        String testActionUid = apiTestActionGen.getUid();
        List<ApiTestAssertion> assertions = apiTestActionGen.getTestAssertions();
        Iterator<ApiTestAssertion> iterator = assertions.iterator();
        while(iterator.hasNext()) {
            ApiTestAssertion assertion = iterator.next();
            assertion.setTestActionId(testActionUid);
            this.save(assertion);
        }
        return this;
    }
    private ApiTestCaseServiceImpl saveTestActionSamples(ApiTestActionGen apiTestActionGen) {
        if(Objects.isNull(apiTestActionGen.getTestSamples())) return this;
        String testActionUid = apiTestActionGen.getUid();
        List<ApiTestSample> samples = apiTestActionGen.getTestSamples();
        Iterator<ApiTestSample> iterator = samples.iterator();
        while(iterator.hasNext()) {
            ApiTestSample sample = iterator.next();
            sample.setTestActionId(testActionUid);
            this.save(sample);
        }
        return this;
    }

    private void demo() {
        apiTestDataPoolMapper.selectAll();

    }
}
