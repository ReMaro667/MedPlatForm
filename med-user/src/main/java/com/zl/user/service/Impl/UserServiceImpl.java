package com.zl.user.service.Impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.domain.Result;
import com.zl.exception.BadRequestException;
import com.zl.user.config.JwtProperties;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.dto.RegisterDTO;
import com.zl.user.domain.po.User;
import com.zl.user.domain.vo.UserLoginVO;
import com.zl.user.mapper.UserMapper;
import com.zl.user.service.IUserService;
import com.zl.user.utils.JwtTool;
import com.zl.utils.RedisConstants;
import com.zl.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import static com.zl.user.utils.NicknameGenerator.generateRandomNickname;
import static com.zl.utils.RedisConstants.LOGIN_CODE_TTL;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<?> sendCode(String phone) {
        // TODO 校验手机号码是否合法
        if (!RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail(400,"手机号码不合法");
        }
        // TODO 生成验证码
        String code = RandomUtil.randomNumbers(6);
        System.out.println("生成验证码成功：" + code);
        // TODO 保存验证码到redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // TODO 发送验证码
        System.out.println("发送验证码成功：" + code);
        return Result.success();
    }

    @Override
    public void postmassage(Long userId) {
        //发送短信通知用户
        System.out.println("---------------发送短信通知用户成功");
    }


    @Override
    public UserLoginVO login(LoginFormDTO loginDTO) {
        // 1. 参数校验略...

        // 2. 根据手机号查用户
        User user = lambdaQuery()
                .eq(User::getPhone, loginDTO.getPhone())
                .one();
        Assert.notNull(user, "用户不存在");

        // 3. 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }

        // 4. 从 user 里拿到角色
        String role = "user"; // 假设你的 User 实体里有 getRole()

        // 5. 生成带 role 的 token
        String token = jwtTool.createToken(
                user.getUserId(),
                role,
                jwtProperties.getTokenTTL()
        );

        // 6. 缓存用户信息（可选，可以缓存 role）
        stringRedisTemplate.opsForValue().set(
                "userInfo:" + user.getUserId(),
                user.toString(),
                30, TimeUnit.MINUTES
        );

        // 7. 封装返回 VO
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getUserId());
        vo.setPhone(user.getPhone());
        vo.setUsername(user.getUsername());
        vo.setRole(role);      // ← 设置角色
        vo.setToken(token);
        return vo;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 1.数据校验
        String phone = registerDTO.getPhone();
        // 2.根据用户名或手机号查询
        System.out.println("phone:" + phone);
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        Assert.isNull(user, "用户已存在");
        // 3.加密密码
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        System.out.println("encodedPassword:" + encodedPassword);
        // 4. 构建用户实体
        String nickname = generateRandomNickname();
        User newUser = User.builder()
                .realName(registerDTO.getName())
                .username(nickname)
                .password(encodedPassword)
                .phone(registerDTO.getPhone())
                .build();
//         5. 保存用户
        save(newUser);
    }
}
