package com.rabbitmq.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.constants.ExchangeType;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SendBase {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routing_key";
    private static final String QUEUE_NAME = "queue_name";
    private static final String IP_ADDRESS = "192.168.0.138";
    private static final Integer PORT = 5672;

    public static void main(String args[]) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        for (int i = 0; i < 10000; i++) {
            String message = "Hello World!" + i;
            Thread.sleep(1000);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        }
        // channel.close();
        //  connection.close();
    }
}
