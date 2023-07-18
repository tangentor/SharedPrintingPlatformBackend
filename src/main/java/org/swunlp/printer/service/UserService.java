package org.swunlp.printer.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.swunlp.printer.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

	List<User> listUser();

	User detail(String uid);

	Object login(User user);

    Object register(User user);

	boolean updateUser(User user);

	boolean updateAvatar(MultipartFile file);

}
