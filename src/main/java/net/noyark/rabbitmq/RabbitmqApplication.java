package net.noyark.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }

    //声明路由模式的交换机 @Bean框架维护
    @Bean
    public DirectExchange getExchange(){
        return new DirectExchange("spring-boot-direct");
    }

    //声明队列，如果同一个交换有绑定队列，将会直接使用
    //没有绑定的，创建
    @Bean
    public Queue getQueue(){
        return new Queue("itemQueue");
    }
    //使用bind对象维护队列交换机的绑定
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(getQueue())
                .to(getExchange()).with("item.add");
    }
    //生产者代码，某个请求代码执行过程中，调用的生产者触发
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RequestMapping("itemAdd")
    public String send(String msg){
        System.out.println("接受到消息"+msg);
        //参数意义；交换机名称，消息路由key，消息
        rabbitTemplate.convertAndSend("spring-boot-direct","",msg);
        return "消息发送成功";
    }


}
