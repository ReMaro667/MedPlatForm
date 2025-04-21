package com.zl.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.domain.Result;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.dto.RegisterDTO;
import com.zl.user.domain.po.User;
import com.zl.user.domain.vo.UserLoginVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IUserService extends IService<User> {
    UserLoginVO login(LoginFormDTO loginFormDTO);
    void register(RegisterDTO registerDTO);
    Result<?> sendCode(String phone);

    void postmassage(Long userId);
}
