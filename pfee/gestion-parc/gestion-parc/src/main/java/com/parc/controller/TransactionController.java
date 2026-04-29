package com.parc.controller;

import com.parc.dto.TransactionCarburantDTO;
import com.parc.service.TransactionCarburantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionCarburantService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionCarburantDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/carte/{carteId}")
    public ResponseEntity<List<TransactionCarburantDTO>> getByCarte(@PathVariable Long carteId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCarte(carteId));
    }

    @GetMapping("/mission/{missionId}")
    public ResponseEntity<List<TransactionCarburantDTO>> getByMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(transactionService.getTransactionsByMission(missionId));
    }
}