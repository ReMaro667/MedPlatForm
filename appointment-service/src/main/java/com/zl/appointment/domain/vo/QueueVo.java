package com.zl.appointment.domain.vo;

import java.util.Set;

public class QueueVo {
    Set<String> normalQueue;
    Set<String> delayQueue;

    public QueueVo(Set<String> normalQueue, Set<String> delayQueue) {
        this.normalQueue = normalQueue;
        this.delayQueue = delayQueue;
    }
}
