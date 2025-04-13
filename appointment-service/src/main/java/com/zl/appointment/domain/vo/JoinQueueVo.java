package com.zl.appointment.domain.vo;

import lombok.Data;

@Data
public class JoinQueueVo {
    private Long userId;
    private String userName;
    private Long doctorId;
    private String doctorName;
    private String num;
    private int status;//1待诊，2已叫号，3已就诊 4完成 5过号
}
