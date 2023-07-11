package org.swunlp.printer.service;


import org.swunlp.printer.entity.User;

import java.util.List;

public interface UserService {

	List<User> listUser();

	User detail(String uid);

	Object login(User user);
}
