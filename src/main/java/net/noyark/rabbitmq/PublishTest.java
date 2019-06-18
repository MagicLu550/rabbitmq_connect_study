package net.noyark.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class PublishTest {
    @Test
    public void pro() throws Exception{
        Connection connection = FacUtil.connect();
        Channel chan = connection.createChannel();
        //看不到生产者与队列的关系了
        //声明交换机
        //参数：都是String类型
        //exchange 交换机名称，交换机类型名称
        //发布订阅：fanout 路由：direct 主题 topic
        chan.exchangeDeclare("1083pEx","fanout");
        for(int i=0;i<10000;i++){
            String msg = "hello rm message"+i;
            chan.basicPublish("1083pEx","",null,msg.getBytes());
        }
        chan.close();
        connection.close();
    }

    //生产者->声明交换机->生产->||||->声明交换机->声明队列->交换机绑定队列->声明消费者->队列给消费者
    //exchangeDeclare->basicPublish->|||->exchangeDeclare->queueDeclare->queueBind->DefaultConsumer->basicConsume
    /**
     * 消费者
     */
    @Test
    public void con() throws Exception{
        Connection connection = FacUtil.connect();
        Channel channel = connection.createChannel();
        //声明一个同名交换机
        channel.exchangeDeclare("1083pEx","fanout");
        //交换机绑定队列，声明队列
        channel.queueDeclare("work1",false,false,false,null);
        channel.queueBind("work1","1083pEx","");
        //消费者,只有在消费者空闲时，交换机才会发送消息给队列(prefetchCount=1一次一条)
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println(message);
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        while (true) {
            channel.basicConsume("work", consumer);
        }
    }
}
