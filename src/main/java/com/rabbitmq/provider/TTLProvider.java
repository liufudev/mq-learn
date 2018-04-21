package com.rabbitmq.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.constants.ExchangeType;
import java.util.HashMap;
import java.util.Map;

public class TTLProvider implements ExchangeType {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routing_key";
    private static final String QUEUE_NAME = "queue_name";

    public static void main(String args[]) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("exchange.dlx", DIRECT, true);
        channel.exchangeDeclare("exchange.normal", FANOUT, true);
        Map<String, Object> config = new HashMap<String, Object>();
        config.put("x-message-ttl", 6000);
        config.put("x-dead-letter-exchange", "exchange.dlx");
        config.put("x-dead-letter-routing-key", "routingkey");
        channel.queueDeclare("exchange.normal", true, false, false, config);
        channel.queueBind("exchange.normal", "exchange.normal", "");
        channel.queueDeclare("exchange.dlx", true, false, false, null);
        channel.queueBind("exchange.dlx", "exchange.dlx", "routingkey");
        channel.basicPublish("exchange.normal", "rk", MessageProperties.PERSISTENT_TEXT_PLAIN, "dlx".getBytes());
    }
}
