package com.my.easytest.service.impl;

import com.my.easytest.dao.TestTaskCaseRelMapper;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.testcase.TestTaskCaseRelDetailDto;
import com.my.easytest.dto.testcase.QueryTestTaskCaseRelListDto;
import com.my.easytest.service.TestTaskCaseRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TestTaskCaseRelServiceImpl implements TestTaskCaseRelService {


    @Autowired
    private TestTaskCaseRelMapper testTaskCaseRelMapper;

    /**
     * 查询任务关联的详细信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<TestTaskCaseRelDetailDto>> listDetail(PageTableRequest<QueryTestTaskCaseRelListDto> pageTableRequest) {

        QueryTestTaskCaseRelListDto params = pageTableRequest.getParams();

        List<TestTaskCaseRelDetailDto> testTaskCaseRelDetailDtoList = testTaskCaseRelMapper.listDetail(params,null,null);

        PageTableResponse<TestTaskCaseRelDetailDto> testJenkinsPageTableResponse = new PageTableResponse<>();
        testJenkinsPageTableResponse.setData(testTaskCaseRelDetailDtoList);

        return ResultDto.success("成功", testJenkinsPageTableResponse);

    }
}
