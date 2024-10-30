package com.rodrigues.silva.marcos.balance_api.model.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Payload(
        String accountIdFrom,
        String accountIdTo,
        float balanceAccountIdFrom,
        float balanceAccountIdTo
) {}
