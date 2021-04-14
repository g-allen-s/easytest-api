package com.my.easytest.service.impl;

import com.my.easytest.common.Token;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dao.TestUserMapper;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.entity.TestUser;
import com.my.easytest.service.TestUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

@Service
@Slf4j
public class TestUserServiceImpl implements TestUserService {

	@Autowired
	private TestUserMapper testUserMapper;

	@Autowired
	private TokenDb tokenDb;


	@Override
	public ResultDto<TestUser> getById(Integer id) {

		TestUser queryTestUser = new TestUser();
		queryTestUser.setId(id);

		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		if(Objects.isNull(resultTestUser)){
			return ResultDto.fail("用户不存在");
		}

		return ResultDto.success("成功", resultTestUser);
	}

	@Override
	public ResultDto<TestUser> save(TestUser testUser) {

		String userName = testUser.getUserName();
		String password = testUser.getPassword();

		TestUser queryTestUser = new TestUser();
		queryTestUser.setUserName(userName);

		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		if(Objects.nonNull(resultTestUser)){
			return ResultDto.fail("用户名已存在");
		}

		String digestedPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+password).getBytes());
		testUser.setPassword(digestedPwd);
		testUser.setCreateTime(new Date());
		testUser.setUpdateTime(new Date());

		testUserMapper.insert(testUser);

		return ResultDto.success("成功", testUser);
	}

	@Override
	public ResultDto<Token> login(String userName, String password) {

		TestUser queryTestUser = new TestUser();
		String queryPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+password).getBytes());
		queryTestUser.setUserName(userName);
		queryTestUser.setPassword(queryPwd);

		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		if(Objects.isNull(resultTestUser)){
			return ResultDto.fail("用户不存在或密码错误");
		}

		String tokenStr = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + userName+password).getBytes());

		TokenDto tokenDto = new TokenDto();
		tokenDto.setUserId(resultTestUser.getId());
		tokenDto.setUserName(userName);
		tokenDto.setToken(tokenStr);
		tokenDto.setDefaultJenkinsId(resultTestUser.getDefaultJenkinsId());

		tokenDb.addTokenDto(tokenStr, tokenDto);

		Token token = new Token();
		token.setToken(tokenStr);
		return ResultDto.success("成功", token);
	}

	@Override
	public ResultDto<TestUser> changePassword(String userName, String oldPassword, String newPassword) {

		TestUser queryTestUser = new TestUser();
		queryTestUser.setUserName(userName);

		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		if(Objects.isNull(resultTestUser)){
			return ResultDto.fail("用户信息异常");
		}

		String oDigestedPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+oldPassword).getBytes());
		if(!oDigestedPwd.equals(resultTestUser.getPassword())){
			return ResultDto.fail("旧密码不正确！");
		}

		String nDigestedPwd = DigestUtils.md5DigestAsHex((UserConstants.md5Hex_sign + userName+newPassword).getBytes());
		TestUser testUser = new TestUser();
		testUser.setId(resultTestUser.getId());
		testUser.setUserName(userName);
		testUser.setPassword(nDigestedPwd);
		testUser.setCreateTime(resultTestUser.getCreateTime());
		testUser.setUpdateTime(new Date());

		testUserMapper.updateByPrimaryKey(testUser);

		return ResultDto.success("成功", testUser);
	}

}
