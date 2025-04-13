package com.zl.user.controller;
import com.zl.domain.Result;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.dto.RegisterDTO;
import com.zl.user.domain.po.User;
import com.zl.user.domain.vo.UserLoginVO;
import com.zl.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("{id}")
    public User getUserInfo(@PathVariable("id") Long userId) {
        return userService.getById(userId);
    }

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result<?> sendCode(@RequestParam("phone") String phone) {
        // TODO 发送短信验证码并保存验证码
        System.out.println("phone: " + phone);
        return userService.sendCode(phone);
    }

    @ApiOperation("用户登录接口")
    @PostMapping("login")
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return userService.login(loginFormDTO);
    }

    @ApiOperation("用户注册接口")
    @PostMapping("register")
    public Result<?> register(@RequestBody @Validated RegisterDTO loginFormDTO){
        System.out.println("registerDTO: " + loginFormDTO);
        userService.register(loginFormDTO);
        return Result.success();
    }

//    @ApiOperation("扣减余额")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pw", value = "支付密码"),
//            @ApiImplicitParam(name = "amount", value = "支付金额")
//    })
//    @PutMapping("/money/deduct")
//    public void deductMoney(@RequestParam("pw") String pw,@RequestParam("amount") Integer amount){
//        userService.deductMoney(pw, amount);
//    }
}

