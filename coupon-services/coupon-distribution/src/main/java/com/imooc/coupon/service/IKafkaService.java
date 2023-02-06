package com.imooc.coupon.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Service interface for Kafka
 */
public interface IKafkaService {

    /**
     * Consume the coupon message in kafka
     * @param record {@link ConsumerRecord}
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}
