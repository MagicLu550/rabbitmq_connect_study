package net.noyark.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;


public class TopicTest {

    @Test
    public void pro() throws Exception{
        Connection conn = FacUtil.connect();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare("exchange1","topic");
        for(int i =0;i<1000000;i++)
            channel.basicPublish("exchange1","net.update",null,("hello"+i).getBytes());
        conn.close();
        channel.close();
    }

    @Test
    public void con1() throws Exception{
        Connection conn = FacUtil.connect();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare("exchange1","topic");
        channel.queueDeclare("topic1",false,false,false,null);
        channel.queueBind("topic1","echange1","net.update");
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("topic1"+new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        while (true){
            channel.basicConsume("topic1",consumer);
        }
    }
    @Test
    public void con2() throws Exception{
        Connection conn = FacUtil.connect();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare("exchange1","topic");
        channel.queueDeclare("topic2",false,false,false, null);
        channel.queueBind("topic2","exchange1","net.add");
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费2"+new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        while (true) {
            channel.basicConsume("topic2", consumer);
        }
    }

}
