package com.zl.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.user.domain.dto.DoctorRegisterDTO;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.dto.RegisterDTO;
import com.zl.user.domain.po.Doctor;
import com.zl.user.domain.vo.DoctorLoginVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IDoctorService extends IService<Doctor> {
    public DoctorLoginVO login(LoginFormDTO loginDTO);

    void register(DoctorRegisterDTO doctorRegisterDTO);
}
