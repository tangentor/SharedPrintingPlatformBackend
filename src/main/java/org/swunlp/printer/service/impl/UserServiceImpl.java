package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.swunlp.printer.entity.User;
import org.swunlp.printer.mapper.UserMapper;
import org.swunlp.printer.service.UserService;
import org.swunlp.printer.constants.UserState;
import org.swunlp.printer.util.JwtUtil;
import org.swunlp.printer.util.RedisUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


	@Resource
	private RedisUtil<User> redisUtil;

	@Resource
	private UserMapper userMapper;

	@Override
	public List<User> listUser() {
		//查看缓存
		List<User> userList = null;
		if(redisUtil.hasKey("SharePrinter:userList")){
			userList = redisUtil.lGet("SharePrinter:userList",0,-1);
		} else {
			userList = userMapper.selectList(null);
			//设置redis,有效时间一小时
			redisUtil.lSet("SharePrinter:userList",userList);
		}
		return userList;
	}

	@Override
	public User detail(String uid) {
		//缓存中是否存在
		for (User user : listUser()) {
			if(Objects.equals(user.getUsername(),uid)){
				return user;
			}
		}
		return null;
	}

	@Override
	public Object login(User user) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(User::getUsername,user.getUsername());
		wrapper.eq(User::getPassword, DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		//判断账密
		User res = userMapper.selectOne(wrapper);
		//判断账户状态
		if(res == null || UserState.BLOCKED.equals(res.getStatus())){
			throw new RuntimeException("账号或密码不正确") ;
		}
		//生成token
		String token = JwtUtil.createJWT(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		map.put("user",res);
		map.put("token",token);
		return map;
	}
}
