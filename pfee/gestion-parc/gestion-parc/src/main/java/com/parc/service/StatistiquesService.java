package com.parc.service;

import com.parc.domain.enums.TypeTransaction;
import com.parc.repository.TransactionCarburantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatistiquesService {

    private final TransactionCarburantRepository transactionRepository;

    public Double getConsommationParc(Long parcId) {
        return transactionRepository.sumMontantByParc(parcId, TypeTransaction.CONSOMMATION);
    }

    public Double getConsommationVehicule(Long vehiculeId) {
        return transactionRepository.sumMontantByVehicule(vehiculeId, TypeTransaction.CONSOMMATION);
    }
}