package net.noyark.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FacUtil {


    public static Connection connect() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("noyark");
        factory.setPassword("027859");
        factory.setVirtualHost("noyark");//"/"不能缺
        //获取连接 获取返回结果的快捷键trl + shift +l
        Connection connection = factory.newConnection();

        return connection;
    }
}
