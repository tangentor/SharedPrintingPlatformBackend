package org.swunlp.printer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.cache.UserCache;
import org.swunlp.printer.constants.UserState;
import org.swunlp.printer.entity.User;
import org.swunlp.printer.mapper.UserMapper;
import org.swunlp.printer.other.BusinessException;
import org.swunlp.printer.service.UserService;
import org.swunlp.printer.util.JwtUtil;
import org.swunlp.printer.util.MinioUtil;
import org.swunlp.printer.util.UsernameUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {


	private final UserCache userCache;

	private final UserMapper userMapper;

	private final MinioUtil minioUtil;


	public UserServiceImpl(UserCache userCache, UserMapper userMapper, MinioUtil minioUtil) {
		this.userCache = userCache;
		this.userMapper = userMapper;
		this.minioUtil = minioUtil;
	}

	@Override
	public List<User> listUser() {
		//查看缓存
		return userMapper.selectList(null);
	}

	@Override
	public User detail(String uid) {
		//缓存中是否存在
		boolean exist = userCache.exist(uid);
		if(exist){
			return userCache.get(uid);
		}
		User user = userMapper.selectByUid(uid);
		//存入缓存中
		userCache.set(uid,user);
		return user;
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
			throw new BusinessException("账号或密码不正确") ;
		}
		//删除密码信息
		res.setPassword(null);
		//生成token
		String token = JwtUtil.createJWT(user.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		map.put("user",res);
		map.put("token",token);
		return map;
	}

	@Override
	public Object register(User user) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(User::getUsername,user.getUsername());
		//判断一个用户是否存在
		if(userMapper.selectOne(wrapper) != null){
			throw new BusinessException("该用户名已存在") ;
		}
		//新增用户，设置默认状态
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		//普通用户
		user.setRid("4");
		//启用状态
		user.setStatus("1");
		//保存用户信息
		userMapper.insert(user);
		return user;
	}

	/**
	 * 更新用户信息，只有两种情况
	 * 1.昵称
	 * 2.密码
	 * @param user 传递过来的用户信息
	 * @return 是否更新成功
	 */
	@Override
	public boolean updateUser(User user) {
		if("1010".equalsIgnoreCase(UsernameUtil.getLoginUser())){
			throw new BusinessException("公共账户信息不允许修改");
		}
		User tmp = new User();
		tmp.setId(user.getId());
		if(user.getPassword()==null){
			//1.更新昵称
			tmp.setNickname(user.getNickname());
		} else {
			//2.更新密码
			tmp.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		}
		//更新信息
		return updateById(tmp);
	}

	@Override
	public boolean updateAvatar(MultipartFile file) {
		String uploadUrl = minioUtil.upload(file);
		//获取用户名以及设置待修改信息
		User user = new User();
		user.setAvatar(uploadUrl);
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(User::getUsername,UsernameUtil.getLoginUser());
		return update(user,wrapper);
	}
}
