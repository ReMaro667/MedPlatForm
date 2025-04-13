package com.zl.advice;
import com.zl.domain.Result;
import com.zl.exception.BadRequestException;
import com.zl.exception.CommonException;
import com.zl.utils.BusinessException;
import com.zl.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.security.sasl.AuthenticationException;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMsg());
    }

    // 处理参数校验异常（@Valid触发）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return processResponse(new BadRequestException(msg));
    }

    // 处理Spring Security认证异常
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthException(AuthenticationException e) {
        return Result.fail(ErrorCode.UNAUTHORIZED);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleOtherException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail(ErrorCode.INTERNAL_ERROR);
    }

    private ResponseEntity<Result<Void>> processResponse(CommonException e){
        return ResponseEntity.status(e.getCode()).body(Result.error(e));
    }
}
