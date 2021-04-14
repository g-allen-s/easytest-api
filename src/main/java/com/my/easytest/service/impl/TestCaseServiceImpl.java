package com.my.easytest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.constants.Constants;
import com.my.easytest.dao.TestCaseMapper;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.testcase.QueryTestCaseListDto;
import com.my.easytest.entity.TestCase;
import com.my.easytest.service.TestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private TestCaseMapper testCaseMapper;

    /**
     *
     * @param testCase
     * @return
     */
    @Override
    public ResultDto<TestCase> save(TestCase testCase) {


        testCase.setCreateTime(new Date());
        testCase.setUpdateTime(new Date());
        testCase.setDelFlag(Constants.DEL_FLAG_ONE);

        testCaseMapper.insertUseGeneratedKeys(testCase);
        return ResultDto.success("成功", testCase);
    }

    /**
     * 删除测试用例信息
     *
     * @param caseId
     * @return createUserId
     */
    @Override
    public ResultDto<TestCase> delete(Integer caseId, Integer createUserId) {

        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(caseId);
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }
        result.setDelFlag(Constants.DEL_FLAG_ZERO);
        testCaseMapper.updateByPrimaryKey(result);

        return ResultDto.success("成功");
    }

    /**
     * 修改测试用例信息
     *
     * @param testCase
     * @return
     */
    @Override
    public ResultDto<TestCase> update(TestCase testCase) {

        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(testCase.getId());
        queryTestCase.setCreateUserId(testCase.getCreateUserId());
        queryTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        testCase.setCreateTime(result.getCreateTime());
        testCase.setUpdateTime(new Date());
        testCase.setDelFlag(Constants.DEL_FLAG_ONE);

        testCaseMapper.updateByPrimaryKey(testCase);

        return ResultDto.success("成功");
    }

    /**
     * 根据id查询测试用例信息
     *
     * @param jenkinsId
     * @return createUserId
     */
    @Override
    public ResultDto<TestCase> getById(Integer jenkinsId, Integer createUserId) {

        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(jenkinsId);
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        //如果为空，则提示，也可以直接返回成功
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        return ResultDto.success("成功",result);
    }

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<TestCase>> list(PageTableRequest<QueryTestCaseListDto> pageTableRequest) {

        QueryTestCaseListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal =  testCaseMapper.count(params);

        //分页查询数据
        List<TestCase> testJenkinsList = testCaseMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<TestCase> testJenkinsPageTableResponse = new PageTableResponse<>();
        testJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        testJenkinsPageTableResponse.setData(testJenkinsList);

        return ResultDto.success("成功", testJenkinsPageTableResponse);
    }

    /**
     * 根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
     *
     * @param createUserId
     * @param caseId
     * @return
     */
    @Override
    public String getCaseDataById(Integer createUserId, Integer caseId) {
        if(Objects.isNull(caseId)){
            return "用例id为空";
        }

        TestCase queryTestCase = new TestCase();
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setId(caseId);
        log.info("=====根据测试用例id查询case原始数据-查库入参====："+ JSONObject.toJSONString(queryTestCase));
        TestCase resultTestCase = testCaseMapper.selectOne(queryTestCase);

        if(Objects.isNull(resultTestCase)){
            return "用例数据未查到";
        }
        if(StringUtils.isEmpty(resultTestCase.getCaseData())){
            return "用例原始数据未查到";
        }

        return resultTestCase.getCaseData();
    }

}
