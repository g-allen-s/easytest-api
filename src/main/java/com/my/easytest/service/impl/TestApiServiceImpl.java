package com.my.easytest.service.impl;

import com.my.easytest.client.RestassuredClient;
import com.my.easytest.common.TokenDb;
import com.my.easytest.dao.*;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.api.QueTestApiListDto;
import com.my.easytest.entity.*;
import com.my.easytest.model.Api;
import com.my.easytest.model.ApiGen;
import com.my.easytest.service.TestApiService;
import com.my.easytest.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Slf4j
public class TestApiServiceImpl implements TestApiService {

	@Autowired
	private TestApiMapper testApiMapper;

	@Autowired
	private TestApiHeaderMapper testApiHeaderMapper;

	@Autowired
	private TestApiParamMapper testApiParamMapper;

	@Autowired
	private TestApiGlobalVariableMapper testApiGlobalVariableMapper;

	@Autowired
	private TestApiActionMapper testApiActionMapper;

    @Autowired
    private TestModuleMapper testModuleMapper;

	@Autowired
	private TokenDb tokenDb;


	/**
	 * 新增接口信息
	 *
	 * @param testApi
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApi> save(TestApi testApi) {

		TestApi qureyTestApi = new TestApi();
		qureyTestApi.setApiCode(testApi.getApiCode());
		TestApi storedTestApi = testApiMapper.selectOne(qureyTestApi);

		if(Objects.nonNull(storedTestApi)){
			return ResultDto.fail("接口["+testApi.getApiCode()+"]已存在！");
		}

		testApi.setApiStatus((byte) 1);
		testApi.setCreateTime(new Date());
		testApi.setUpdateTime(new Date());
		testApiMapper.insertUseGeneratedKeys(testApi);

		return ResultDto.success("添加成功", testApi);
	}

	/**
	 * 修改接口信息
	 *
	 * @param testApi
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApi> update(TestApi testApi) {

		TestApi qureyTestApi = new TestApi();
		qureyTestApi.setId(testApi.getId());
		TestApi storedTestApi = testApiMapper.selectOne(qureyTestApi);
		if(Objects.isNull(storedTestApi)){
			return ResultDto.fail("未查到接口信息");
		}

		if(testApi.getCreateUserId() != storedTestApi.getCreateUserId()){
			return ResultDto.success("不可修改该接口");
		}

		TestApi qureyTakenTestApi = new TestApi();
		qureyTakenTestApi.setApiCode(testApi.getApiCode());
		TestApi takenTestApi = testApiMapper.selectOne(qureyTakenTestApi);
		if(Objects.nonNull(takenTestApi) && !String.valueOf(testApi.getId()).equals(String.valueOf(takenTestApi.getId()))){
			return ResultDto.fail("此接口已存在");
		}

		testApi.setApiStatus((byte) 1);
		testApi.setCreateTime(storedTestApi.getCreateTime());
		testApi.setUpdateTime(new Date());

		testApiMapper.updateByPrimaryKey(testApi);

		return ResultDto.success("更新成功");
	}

	/**
	 * 根据ApiId删除接口
	 *
	 * @param apiId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApi> delete(Integer apiId, TokenDto tokenDto) {

		TestApi qureyTestApi = new TestApi();
		qureyTestApi.setId(apiId);
		qureyTestApi.setCreateUserId(tokenDto.getUserId());
		TestApi storedTestApi = testApiMapper.selectOne(qureyTestApi);
		if(Objects.isNull(storedTestApi)){
			return ResultDto.fail("未查到接口信息");
		}

		testApiMapper.deleteByPrimaryKey(apiId);

		return ResultDto.success("删除成功");
	}

	/**
	 * 查询接口信息列表
	 *
	 * @param pageTableRequest
	 * @return
	 */
	@Override
	public ResultDto<PageTableResponse<TestApi>> list(PageTableRequest<QueTestApiListDto> pageTableRequest) {

		QueTestApiListDto params = pageTableRequest.getParams();
		Integer pageNum = pageTableRequest.getPageNum();
		Integer pageSize = pageTableRequest.getPageSize();


		//总数
		Integer recordsTotal = testApiMapper.count(params);

		//分页查询数据
		List<TestApi> testApis = testApiMapper.list(params, (pageNum - 1) * pageSize, pageSize);

		PageTableResponse<TestApi> pageTableResponse = new PageTableResponse<>();
		pageTableResponse.setRecordsTotal(recordsTotal);
		pageTableResponse.setData(testApis);

		return ResultDto.success("查询成功", pageTableResponse);
	}

	/**
	 * 根据id查询接口信息
	 *
	 * @param apiId
	 * @return createUserId
	 */
	@Override
	public ResultDto<TestApi> getById(Integer apiId, Integer createUserId) {

		TestApi qureyTestApi = new TestApi();
		qureyTestApi.setId(apiId);
		qureyTestApi.setCreateUserId(createUserId);
		TestApi storedTestApi = testApiMapper.selectOne(qureyTestApi);
		if(Objects.isNull(storedTestApi)){
			return ResultDto.fail("未查到模块信息");
		}

		return ResultDto.success("查询成功", storedTestApi);
	}

	/**
	 * 新增请求头信息
	 *
	 * @param testApiHeader
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiHeader> save(TestApiHeader testApiHeader, Integer operator){

		TestApiHeader qureyParam = new TestApiHeader();
		qureyParam.setApiId(testApiHeader.getApiId());
		qureyParam.setHeaderKey(testApiHeader.getHeaderKey());
		TestApiHeader storedTestApiHeader = testApiHeaderMapper.selectOne(qureyParam);

		if(Objects.nonNull(storedTestApiHeader)){
			testApiHeader.setUid(storedTestApiHeader.getUid());
			testApiHeaderMapper.updateByPrimaryKey(testApiHeader);
			return ResultDto.success("更新成功", testApiHeader);
		}

		testApiHeader.setUid(UUID.randomUUID().toString().replace("-", ""));
		testApiHeaderMapper.insert(testApiHeader);

		return ResultDto.success("添加成功", testApiHeader);
	}

	/**
	 * 根据Uid删除
	 *
	 * @param uid
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiHeader> deleteHeader(String uid, Integer userId) {

		TestApiHeader qureyParam = new TestApiHeader();
		qureyParam.setUid(uid);
		TestApiHeader storedTestApiHeader = testApiHeaderMapper.selectOne(qureyParam);
		if(Objects.isNull(storedTestApiHeader)){
			return ResultDto.fail("未查到请求头信息");
		}

		testApiHeaderMapper.deleteByPrimaryKey(uid);

		return ResultDto.success("删除成功");
	}

	/**
	 * 新增请求参数信息
	 *
	 * @param testApiParam
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiParam> save(TestApiParam testApiParam, Integer operator){

		TestApiParam qureyParam = new TestApiParam();
		qureyParam.setApiId(testApiParam.getApiId());
		qureyParam.setParamName(testApiParam.getParamName());
		qureyParam.setParamType(testApiParam.getParamType());
		TestApiParam storedTestApiParam = testApiParamMapper.selectOne(qureyParam);

		if(Objects.nonNull(storedTestApiParam)){
			testApiParam.setUid(storedTestApiParam.getUid());
			testApiParamMapper.updateByPrimaryKey(testApiParam);
			return ResultDto.success("更新成功", testApiParam);
		}

		testApiParam.setUid(UUID.randomUUID().toString().replace("-", ""));
		testApiParamMapper.insert(testApiParam);

		return ResultDto.success("添加成功", testApiParam);
	}

	/**
	 * 根据Uid删除
	 *
	 * @param uid
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiParam> deleteParam(String uid, Integer userId) {

		TestApiParam qureyParam = new TestApiParam();
		qureyParam.setUid(uid);
		TestApiParam storedTestApiParam = testApiParamMapper.selectOne(qureyParam);
		if(Objects.isNull(storedTestApiParam)){
			return ResultDto.fail("未查到请求参数信息");
		}

		testApiParamMapper.deleteByPrimaryKey(uid);

		return ResultDto.success("删除成功");
	}

	/**
	 * 新增接口全局变量信息
	 *
	 * @param testApiGlobalVariable
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiGlobalVariable> save(TestApiGlobalVariable testApiGlobalVariable, Integer operator){

		TestApiGlobalVariable qureyParam = new TestApiGlobalVariable();
		qureyParam.setModuleId(testApiGlobalVariable.getModuleId());
		qureyParam.setVariableKey(testApiGlobalVariable.getVariableKey());
		TestApiGlobalVariable storedTestApiGlobalVariable = testApiGlobalVariableMapper.selectOne(qureyParam);

		if(Objects.nonNull(storedTestApiGlobalVariable)){
			testApiGlobalVariable.setUid(storedTestApiGlobalVariable.getUid());
			testApiGlobalVariableMapper.updateByPrimaryKey(testApiGlobalVariable);
			return ResultDto.success("更新成功", testApiGlobalVariable);
		}

		testApiGlobalVariable.setUid(UUID.randomUUID().toString().replace("-", ""));
		testApiGlobalVariableMapper.insert(testApiGlobalVariable);

		return ResultDto.success("添加成功", testApiGlobalVariable);
	}

	/**
	 * 根据Uid删除
	 *
	 * @param uid
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiGlobalVariable> deleteGlobal(String uid, Integer userId) {

		TestApiGlobalVariable qureyParam = new TestApiGlobalVariable();
		qureyParam.setUid(uid);
		TestApiGlobalVariable storedTestApiGlobal = testApiGlobalVariableMapper.selectOne(qureyParam);
		if(Objects.isNull(storedTestApiGlobal)){
			return ResultDto.fail("未查到全局变量信息");
		}

		testApiGlobalVariableMapper.deleteByPrimaryKey(uid);

		return ResultDto.success("删除成功");
	}

	/**
	 * 新增接口操作信息
	 *
	 * @param testApiAction
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestApiAction> save(TestApiAction testApiAction, Integer operator) {

		TestApiAction qureyParam = new TestApiAction();
		qureyParam.setApiId(testApiAction.getApiId());
		Integer storedTotal = testApiActionMapper.selectCount(qureyParam);
		testApiAction.setStepId((byte) (storedTotal + 1));

		testApiAction.setUid(UUID.randomUUID().toString().replace("-", ""));
		testApiActionMapper.insert(testApiAction);

		return ResultDto.success("添加成功", testApiAction);
	}

    /**
     * 运行接口action
     *
     * @param apiId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto run(Integer apiId, String env, Integer operator) {
        // 获取api对象
        TestApi api = testApiMapper.selectOne((TestApi) getQueryObj4PrimaryKey(new TestApi(), apiId));
        // 根据api获取module对象
        TestModule apiModule = testModuleMapper.selectOne((TestModule) getQueryObj4PrimaryKey(new TestModule(), api.getApiModule()));
        // 根据apiModule+env获取host
        String host = getHost(apiModule, env);
        if (StringUtils.isEmpty(host)) return ResultDto.fail("获取运行环境异常！");
        // 根据apiId获取步骤列表
        List<TestApiAction> apiActionSteps = testApiActionMapper.selectById(apiId);
        // 获取请求头
        List<TestApiHeader> headers = testApiHeaderMapper.selectById(apiId, "request_header");
        // 获取全局参数
        List<TestApiGlobalVariable> globalParams = testApiGlobalVariableMapper.selectById(apiModule.getId());
        Map<String, Object> globalParam = transformGlobalParamList2Map(globalParams);
        try {
			RestassuredClient restassuredClient = new RestassuredClient();
			ResultDto result = restassuredClient
					.preHandle(api, host, headers, apiActionSteps, globalParam)
					.send().assertResponse(globalParam);
            // todo this.updateGlobalParams(apiModule.getModuleId(), globalParam);
            return result;
        } catch (Exception e) {
            return ResultDto.fail(e.getMessage());
        }
    }
    private Object getQueryObj4PrimaryKey(Object o, Integer id) {
        if (o instanceof TestApi) {
            ((TestApi) o).setId(id);
        }

        if (o instanceof TestModule) {
            ((TestModule) o).setId(id);
        }

        return o;
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

    /**
     *  导入接口
     *
     * @param apiGen
     * @return
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto generateTestApiKit(ApiGen apiGen, Integer operator) {
        Integer moduleId = apiGen.getModuleId();
        this.saveGlobalParams(moduleId, apiGen.getGlobalParams(), operator);
        List<Api> apiList = apiGen.getApiList();
        Iterator<Api> iterator = apiList.iterator();
        while(iterator.hasNext()) {
            Api api = iterator.next();
			TestApi testApi = new TestApi();
			CopyUtil.copyPropertiesCglib(api, testApi); // ApiModel to TestApiEntity
			testApi.setApiModule(moduleId);
			testApi.setCreateUserId(operator);
            ResultDto saveApiResult; //this.generateApi(testApi);
			try {
				saveApiResult = this.save(testApi);
				if (saveApiResult.getResultCode() == 0) {
					return saveApiResult;
				}
				api.setApiId(((TestApi) saveApiResult.getData()).getId());
				this.saveApiHeaders(api).saveApiParams(api).saveApiActions(api);
			} catch (Exception e) {
				return ResultDto.fail("Exception::"+e.getMessage());
			}
        }
        return ResultDto.success("导入成功！");
    }
    private ResultDto generateApi(TestApi testApi) {
        try {
        	ResultDto saveApiResult = this.save(testApi);
        	if (saveApiResult.getResultCode() == 0) return ResultDto.fail("接口["+testApi.getApiCode()+"]已存在！");
        	//this.saveApiHeaders(api).saveApiParams(api).saveApiActions(api);
            return ResultDto.success("处理成功");
        } catch (Exception e) {
			return ResultDto.fail("Exception::"+e.getMessage());
        }
    }
    private TestApiServiceImpl saveApiHeaders(Api api) {
        Integer apiId = api.getApiId();
        List<TestApiHeader> headers = api.getApiHeaders();
        Iterator<TestApiHeader> iterator = headers.iterator();
        while(iterator.hasNext()) {
			TestApiHeader header = iterator.next();
            header.setApiId(apiId);
            this.save(header, 100);
        }
        return this;
    }
    private TestApiServiceImpl saveApiParams(Api api) {
        int apiId = api.getApiId();
        List<TestApiParam> params = api.getRequestParams();
        params.addAll(api.getResponseParams());
        Iterator<TestApiParam> iterator = params.iterator();
        while(iterator.hasNext()) {
			TestApiParam param = iterator.next();
            param.setApiId(apiId);
			this.save(param, 100);
        }
        return this;
    }
    private TestApiServiceImpl saveApiActions(Api api) {
        int apiId = api.getApiId();
        List<TestApiAction> apiActionSteps = api.getApiActions();
        Iterator<TestApiAction> iterator = apiActionSteps.iterator();
        while(iterator.hasNext()) {
			TestApiAction actionStep = iterator.next();
            actionStep.setApiId(apiId);
            this.save(actionStep, 100);
        }
		return this;
    }

	public void saveGlobalParams(Integer moduleId, List<TestApiGlobalVariable> globalParams, Integer operator) {
		Iterator<TestApiGlobalVariable> iterator = globalParams.iterator();
		while(iterator.hasNext()) {
			TestApiGlobalVariable globalParam = iterator.next();
			globalParam.setModuleId(moduleId);
			this.save(globalParam, operator);
		}
	}
}
