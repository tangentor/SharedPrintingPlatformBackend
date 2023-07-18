package org.swunlp.printer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.swunlp.printer.entity.User;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM `t_user` WHERE username = #{uid}")
    User selectByUid(String uid);
}
