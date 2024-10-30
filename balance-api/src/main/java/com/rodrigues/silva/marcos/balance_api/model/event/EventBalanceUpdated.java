package com.rodrigues.silva.marcos.balance_api.model.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record EventBalanceUpdated(
        String name,
        Payload payload
) {
}

