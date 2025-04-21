package com.zl.appointment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Queue {
    @TableId(type = IdType.AUTO)
    Long id;
    Long appointmentId;
    String queueNo;
    int status;
    LocalDateTime create_time;
}
