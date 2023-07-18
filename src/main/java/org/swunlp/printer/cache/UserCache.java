package org.swunlp.printer.cache;

import org.springframework.stereotype.Component;
import org.swunlp.printer.entity.User;

@Component
public class UserCache extends BaseCache<User>{
    @Override
    protected String domain() {
        return "user";
    }

}
