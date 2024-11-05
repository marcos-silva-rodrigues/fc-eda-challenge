package com.rodrigues.silva.marcos.balance_api.service;

import com.rodrigues.silva.marcos.balance_api.consumer.BalanceConsumer;
import com.rodrigues.silva.marcos.balance_api.model.Balance;
import com.rodrigues.silva.marcos.balance_api.model.event.EventBalanceUpdated;
import com.rodrigues.silva.marcos.balance_api.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BalanceService {

    private Logger log = Logger.getLogger(BalanceService.class.getName());

    private BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Transactional
    public void process(EventBalanceUpdated event) {
        var from = event.payload().accountIdFrom();
        var to = event.payload().accountIdTo();
        var balanceFrom = event.payload().balanceAccountIdFrom();
        var balanceTo = event.payload().balanceAccountIdTo();

        Balance accountFrom = getAccount(from);
        Balance accountTo = getAccount(to);

        accountFrom.setBalance(balanceFrom);
        log.info("Update account with id: %s to balance: %f".formatted(from, balanceFrom));
        accountTo.setBalance(balanceTo);
        log.info("Update account with id: %s to balance: %f".formatted(to, balanceTo));

        balanceRepository.save(accountFrom);
        balanceRepository.save(accountTo);
    }

    private Balance getAccount(String id) {
        log.info("Find account in repository with id: %s".formatted(id));
        return balanceRepository
                .findById(id)
                .orElseGet(() -> {
                    log.warning("Not found account, creating new account with id: %s".formatted(id));
                    var newAccount = new Balance();
                    newAccount.setId(id);
                    newAccount.setUpdatedAt(LocalDateTime.now());
                    return newAccount;
                });
    }

    public Optional<Balance> getBalanceById(String id) {
        return balanceRepository.findById(id);
    }
}
