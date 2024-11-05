package com.rodrigues.silva.marcos.balance_api.controller;

import com.rodrigues.silva.marcos.balance_api.model.Balance;
import com.rodrigues.silva.marcos.balance_api.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    private Logger log = Logger.getLogger(BalanceController.class.getName());

    private BalanceService service;

    public BalanceController(BalanceService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBalanceByAccountId(@PathVariable String id) {
        log.info("Start Search by account id: %s".formatted(id));
        var balanceOpt = service.getBalanceById(id);
        if (balanceOpt.isPresent()) {
            log.info("Account founded by id: %s".formatted(id));
            return ResponseEntity.ok(balanceOpt.get());
        }
        log.info("Not found account by id: %s".formatted(id));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Not found account with id: %s".formatted(id));
    }

}
