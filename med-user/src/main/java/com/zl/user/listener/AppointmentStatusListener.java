package com.zl.user.listener;

import com.zl.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentStatusListener {

    private final IUserService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "appointment.create.success.queue", durable = "true"),
            exchange = @Exchange(name = "appointment.topic"),
            key = "appointment.success"
    ))
    public void listenAppointmentSuccess(Long userId){
        orderService.postmassage(userId);
    }
}
