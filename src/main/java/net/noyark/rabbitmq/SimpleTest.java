package net.noyark.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class SimpleTest {

    /**
     * 测试方法，信息生产者
     */
    @Test
    public void producter() throws Exception{
        //"/"不能缺
        //获取连接 获取返回结果的快捷键trl + shift +l
        Connection connection = FacUtil.connect();
        //从一个连接可以获取多个信道channel
        //connection的连接，基于底层tcp,socket
        Channel chan = connection.createChannel();
        //连接绑定生成queue队列，如果当前mq中存在，信道连接队列
        //不存在，信道会创建一个
        String queue = "simple1000";
        //绑定信道和队列
        //参数含义
        //queue是string类型，表示绑定到信道的queue队列
        //durable：boolean，是否持久化
        //exclusive boolean，是否专属化，true，表示专属，当前连接conn
        //创建的任何channel都可以连接queue false，其他conn创建的
        //arguments:Map 其他参数；
        chan.queueDeclare(queue,false,false,false,null);
        //exchange:简单模式下，空字符串默认使用交换机完成简单模式的信息发送
        //routingKey：简单模式，默认使用交换机 queue名称就是默认的key
        //props:属性，BasicProperties，例如调用它的属性值，
        //设定与持久化相关的配置，BasicProperties.deliveryMode
        //0是持久化，1是不持久化，配合durable
        //body:byte数组形式的消息
        String message="hello rabbitmq 1803";
        chan.basicPublish("",queue,null,message.getBytes());
        chan.close();
        connection.close();
    }
    /**
     * 结束监听消息队列，消费者代码
     */
    @Test
    public void consumer() throws Exception{
        Connection connection = FacUtil.connect();
        //从一个连接可以获取多个信道channel
        //connection的连接，基于底层tcp,socket
        Channel chan = connection.createChannel();
        //绑定
        String queue = "simple1000";
        chan.queueDeclare(queue,false,false,false,null);
        //创建一个消费者,将对象交给chan
        chan.basicQos(1);
        Consumer consumer = new DefaultConsumer(chan){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("customer 消费消息："+message);
            }
        };
        chan.basicConsume("hello", true,consumer);
        chan.basicConsume(queue,true,consumer);
        //QueueingConsumer consumer = new QueueingConsumer(chan);
        //消费消息，利用chan，绑定消费者和队列
        //autoAck:自动确认，true，确认，false，消费完消息后需要手动
        //调用代码确认消费完毕；chan.ack,chan.nack,chan.basicReject
        //chan.basicConsume(queue,true,consumer);
        //获取消息队列的信息
        //循环监听
        while (true){
            //拿大消息封装对象
            //Delivery dli =consumer.nextDelivery();
            //从dli获取消息，byte数组
            //String msg = new String(dli.getBody());
            //chan.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }

}
