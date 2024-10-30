package com.rodrigues.silva.marcos.balance_api.consumer;

import com.rodrigues.silva.marcos.balance_api.model.event.EventBalanceUpdated;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BalanceConsumer {


    @KafkaListener(topics = "balances", groupId = "{spring.kafka.consumer.group-id}",
            properties = {
                    "spring.json.value.default.type=com.rodrigues.silva.marcos.balance_api.model.event.EventBalanceUpdated"
            }
        )
    public void updateBalance(EventBalanceUpdated balanceUpdated) {
        System.out.println("Evento recebido em dto: " + balanceUpdated.toString());
    }
}
