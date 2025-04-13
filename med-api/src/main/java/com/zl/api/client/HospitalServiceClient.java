package com.zl.api.client;

import com.zl.domain.Result;
import com.zl.api.domain.dto.ScheduleDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

// 医院服务客户端（声明式调用）
public interface HospitalServiceClient {

    @ApiOperation("")
    @ApiImplicitParam(name = "ids", value = "购物车条目id集合")
    @DeleteMapping("/carts")
    Result<ScheduleDTO> getSchedule(@PathVariable Long scheduleId);

    @PostMapping("/schedules/{scheduleId}/lock")
    Result<Void> lockScheduleStock(
            @PathVariable Long scheduleId,
            @RequestParam Integer count
    );
}
