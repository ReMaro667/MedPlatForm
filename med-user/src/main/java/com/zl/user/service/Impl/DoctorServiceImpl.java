package com.zl.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.exception.BadRequestException;
import com.zl.user.config.JwtProperties;
import com.zl.user.domain.dto.DoctorRegisterDTO;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.po.Doctor;
import com.zl.user.domain.vo.DoctorLoginVO;
import com.zl.user.mapper.DoctorMapper;
import com.zl.user.service.IDoctorService;
import com.zl.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.zl.user.utils.NicknameGenerator.generateRandomNickname;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public DoctorLoginVO login(LoginFormDTO loginDTO) {

        // 2. 根据手机号查用户
        Doctor doctor = lambdaQuery()
                .eq(Doctor::getPhone, loginDTO.getPhone())
                .one();
        Assert.notNull(doctor, "用户不存在");

        // 3. 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), doctor.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }

        // 4. 从 user 里拿到角色
        String role = "doctor"; // 假设你的 User 实体里有 getRole()

        // 5. 生成带 role 的 token
        String token = jwtTool.createToken(
                doctor.getDoctorId(),
                role,
                jwtProperties.getTokenTTL()
        );

        // 6. 缓存用户信息（可选，可以缓存 role）
        stringRedisTemplate.opsForValue().set(
                "userInfo:" + doctor.getDoctorId(),
                doctor.toString(),
                30, TimeUnit.MINUTES
        );

        // 7. 封装返回 VO
        DoctorLoginVO vo = new DoctorLoginVO();
        vo.setUserId(doctor.getDoctorId());
        vo.setPhone(doctor.getPhone());
        vo.setUsername(doctor.getName());
        vo.setRole(role);      // ← 设置角色
        vo.setToken(token);
        return vo;
    }

    @Override
    public void register(DoctorRegisterDTO registerDTO) {
        // 1.数据校验
        String phone = registerDTO.getPhone();
        // 2.根据用户名或手机号查询
        System.out.println("phone:" + phone);
        Doctor doctor = lambdaQuery().eq(Doctor::getPhone, phone).one();
        Assert.isNull(doctor, "用户已存在");
        // 3.加密密码
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        System.out.println("encodedPassword:" + encodedPassword);
        Doctor newDoctor = Doctor.builder()
                .doctorId(registerDTO.getDoctorId())
                .name(registerDTO.getName())
                .departmentId(registerDTO.getDepartmentId())
                .password(encodedPassword)
                .phone(registerDTO.getPhone())
                .build();
//         5. 保存用户
        save(newDoctor);
    }
}
