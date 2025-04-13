package com.zl.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zl.user.enums.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户基础信息抽象类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("user")
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    protected Long userId;
    protected String username;
    @TableField("password_hash")
    protected String password;
    protected String phone;
    protected String realName;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
