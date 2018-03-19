package com.boris.service.impl;

import com.boris.common.exception.BorisException;
import com.boris.common.transactional.TransactionalService;
import com.boris.common.vo.PageResultVo;
import com.boris.common.vo.ResponseVo;
import com.boris.dao.UserMapper;
import com.boris.model.User;
import com.boris.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl  extends TransactionalService implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo insert(User user) throws BorisException {
        try {
            prepareTransaction();
            userMapper.insert(user);
            if(user.getName().equals("11")) {
                user.setName("1234567890123");
                userMapper.insert(user);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new BorisException(e, "保存失败!");
        }
        return new ResponseVo();
    }

    @Override
    public ResponseVo queryUser() {
        ResponseVo responseVo = new ResponseVo();
        PageResultVo pageResultVo = new PageResultVo();
        List<User> results = new ArrayList<>();
        PageHelper.startPage(2,2);
        results = userMapper.queryUsers();
        PageInfo<User> pageInfo = new PageInfo<>(results);
        System.out.println(pageInfo.getTotal());
        pageResultVo.setCount(pageInfo.getTotal());
        pageResultVo.setResults(results);
        responseVo.setData(pageResultVo);
        return responseVo;
    }
}
