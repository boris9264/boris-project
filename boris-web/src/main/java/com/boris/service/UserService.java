package com.boris.service;

import com.boris.common.exception.BorisException;
import com.boris.common.vo.ResponseVo;
import com.boris.model.User;

public interface UserService {
    ResponseVo insert(User user) throws BorisException;

    ResponseVo queryUser();
}
