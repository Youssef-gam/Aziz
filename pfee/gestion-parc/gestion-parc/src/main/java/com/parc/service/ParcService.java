package com.parc.service;

import com.parc.dto.ParcDTO;
import com.parc.domain.entity.Parc;
import com.parc.repository.ParcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcService {

    private final ParcRepository parcRepository;

    private ParcDTO toDTO(Parc parc) {
        return new ParcDTO(parc.getId(), parc.getNom(), parc.getAdresse(), parc.getCapacite());
    }

    private Parc toEntity(ParcDTO dto) {
        return new Parc(dto.getId(), dto.getNom(), dto.getAdresse(), dto.getCapacite());
    }

    public List<ParcDTO> getAll() {
        return parcRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ParcDTO getById(Long id) {
        return parcRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Parc non trouvé"));
    }

    public ParcDTO create(ParcDTO dto) {
        Parc parc = toEntity(dto);
        return toDTO(parcRepository.save(parc));
    }

    public ParcDTO update(Long id, ParcDTO dto) {
        Parc parc = parcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parc non trouvé"));
        parc.setNom(dto.getNom());
        parc.setAdresse(dto.getAdresse());
        parc.setCapacite(dto.getCapacite());
        return toDTO(parcRepository.save(parc));
    }

    public void delete(Long id) {
        parcRepository.deleteById(id);
    }
}