package com.my.easytest.service;

import com.my.easytest.common.Token;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.entity.TestUser;

public interface TestUserService {

	ResultDto<TestUser> getById(Integer id);

	ResultDto<TestUser> save(TestUser testUser);

	ResultDto<Token> login(String username, String password);

	ResultDto<TestUser> changePassword(String userName, String oldPassword, String newPassword);

}
