package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.testcase.TestTaskCaseRelDetailDto;
import com.my.easytest.dto.testcase.QueryTestTaskCaseRelListDto;

public interface TestTaskCaseRelService {

	/**
	 *  查询任务关联的详细信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestTaskCaseRelDetailDto>> listDetail(PageTableRequest<QueryTestTaskCaseRelListDto> pageTableRequest);


}
