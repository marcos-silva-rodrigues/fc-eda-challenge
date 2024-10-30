package com.rodrigues.silva.marcos.balance_api.consumer;

import com.rodrigues.silva.marcos.balance_api.model.event.EventBalanceUpdated;
import com.rodrigues.silva.marcos.balance_api.service.BalanceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component

public class BalanceConsumer {

    private Logger log = Logger.getLogger(BalanceConsumer.class.getName());

    private BalanceService balanceService;

    public BalanceConsumer(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @KafkaListener(topics = "balances", groupId = "{spring.kafka.consumer.group-id}",
            properties = {
                    "spring.json.value.default.type=com.rodrigues.silva.marcos.balance_api.model.event.EventBalanceUpdated"
            }
        )
    public void updateBalance(EventBalanceUpdated balanceUpdated) {
        log.info("Start process to event: %s".formatted(balanceUpdated.name()));
        balanceService.process(balanceUpdated);
        log.info("End process event: %s".formatted(balanceUpdated.name()));
    }
}
