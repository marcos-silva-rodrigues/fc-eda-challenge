package com.rodrigues.silva.marcos.balance_api.repository;

import com.rodrigues.silva.marcos.balance_api.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, String> {

    Optional<Balance> findById(String id);
}
