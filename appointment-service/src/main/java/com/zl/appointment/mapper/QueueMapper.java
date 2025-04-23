package com.zl.appointment.mapper;

import com.zl.appointment.domain.po.Queue;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QueueMapper {
    @Insert("insert into queue (queue_no,status,appointment_id,schedule_id) " +
            "values (#{queueNo},#{status},#{appointmentId},#{scheduleId})")
    void queueJoin(Queue queue);

    @Update("update queue q set q.status = #{status} where q.appointment_id = #{appointmentId} ")
    void updateQueue(Long appointmentId,int status);
}
