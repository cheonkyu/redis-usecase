package io.cheonkyu;

import org.springframework.stereotype.Component;

import io.cheonkyu.service.MessageProducer;

@Component
public class StartUp {
  private MessageProducer producer;

  StartUp(MessageProducer producer) {
    this.producer = producer;

    producer.sendMessage("Hello world");
  }
}
