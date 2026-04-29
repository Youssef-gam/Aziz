package com.parc.service;

import com.parc.dto.GarageDTO;
import com.parc.domain.entity.Garage;
import com.parc.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;

    // Conversion DTO
    private GarageDTO toDTO(Garage garage) {
        GarageDTO dto = new GarageDTO();
        dto.setId(garage.getId());
        dto.setNom(garage.getNom());
        dto.setSpecialite(garage.getSpecialite());
        dto.setAdresse(garage.getAdresse());
        dto.setCapacite(garage.getCapacite());
        dto.setTelephone(garage.getTelephone());
        return dto;
    }

    private Garage toEntity(GarageDTO dto) {
        Garage garage = new Garage();
        garage.setNom(dto.getNom());
        garage.setSpecialite(dto.getSpecialite());
        garage.setAdresse(dto.getAdresse());
        garage.setCapacite(dto.getCapacite());
        garage.setTelephone(dto.getTelephone());
        return garage;
    }

    // CRUD
    public GarageDTO create(GarageDTO dto) {
        Garage garage = toEntity(dto);
        return toDTO(garageRepository.save(garage));
    }

    public List<GarageDTO> getAll() {
        return garageRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public GarageDTO getById(Long id) {
        return garageRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
    }

    public GarageDTO update(Long id, GarageDTO dto) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
        garage.setNom(dto.getNom());
        garage.setSpecialite(dto.getSpecialite());
        garage.setAdresse(dto.getAdresse());
        garage.setCapacite(dto.getCapacite());
        garage.setTelephone(dto.getTelephone());
        return toDTO(garageRepository.save(garage));
    }

    public void delete(Long id) {
        garageRepository.deleteById(id);
    }

    // Gestion du token FCM
    public Garage getGarageEntityById(Long id) {
        return garageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
    }

    public void updateFcmToken(Long id, String token) {
        Garage garage = getGarageEntityById(id);
        garage.setFcmToken(token);
        garageRepository.save(garage);
    }
}