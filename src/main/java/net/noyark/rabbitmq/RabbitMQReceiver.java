package net.noyark.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    @RabbitListener(queues="itemQueue")
    public void spendMsg(String msg){
        //异步监听获取的消息msg可以是id，关键字
        //打印
        System.out.println(msg);
    }

}
