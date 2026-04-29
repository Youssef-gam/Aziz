package com.parc.service;

import com.parc.domain.entity.Vehicule;
import com.parc.dto.VehiculeDTO;
import com.parc.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculeService {

    private final VehiculeRepository repository;

    public List<VehiculeDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VehiculeDTO getById(Long id) {
        Vehicule vehicule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        return toDTO(vehicule);
    }

    public List<VehiculeDTO> getByParc(Long parcId) {
        return repository.findByParcId(parcId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehiculeDTO create(VehiculeDTO dto) {
        Vehicule vehicule = toEntity(dto);
        return toDTO(repository.save(vehicule));
    }

    @Transactional
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        Vehicule vehicule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        updateEntity(vehicule, dto);
        return toDTO(repository.save(vehicule));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Conversion Entity -> DTO
    private VehiculeDTO toDTO(Vehicule entity) {
        VehiculeDTO dto = new VehiculeDTO();
        dto.setId(entity.getId());
        dto.setMatricule(entity.getMatricule());
        dto.setNumeroChassis(entity.getNumeroChassis());
        dto.setMarque(entity.getMarque());
        dto.setModele(entity.getModele());
        dto.setTypeCarburant(entity.getTypeCarburant());
        dto.setCouleur(entity.getCouleur());
        dto.setKilometre(entity.getKilometre());          // ← kilometre, pas kilometrage
        dto.setCapaciteReservoir(entity.getCapaciteReservoir());
        dto.setDateMiseEnService(entity.getDateMiseEnService());
        dto.setDateExpirationVisiteTechnique(entity.getDateExpirationVisiteTechnique());
        dto.setDateExpirationCarteGrise(entity.getDateExpirationCarteGrise());
        dto.setPuissanceFiscale(entity.getPuissanceFiscale()); // ← puissanceFiscale
        dto.setStatut(entity.getStatut());
        dto.setNomSocieteAssurance(entity.getNomSocieteAssurance());
        dto.setDateExpirationAssurance(entity.getDateExpirationAssurance());
        dto.setMontantAssurance(entity.getMontantAssurance());
        dto.setParcId(entity.getParc() != null ? entity.getParc().getId() : null); // ← parcId
        return dto;
    }

    // Conversion DTO -> Entity (sans gérer la relation Parc pour simplifier)
    private Vehicule toEntity(VehiculeDTO dto) {
        Vehicule vehicule = new Vehicule();
        vehicule.setMatricule(dto.getMatricule());
        vehicule.setNumeroChassis(dto.getNumeroChassis());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setTypeCarburant(dto.getTypeCarburant());
        vehicule.setCouleur(dto.getCouleur());
        vehicule.setKilometre(dto.getKilometre());
        vehicule.setCapaciteReservoir(dto.getCapaciteReservoir());
        vehicule.setDateMiseEnService(dto.getDateMiseEnService());
        vehicule.setDateExpirationVisiteTechnique(dto.getDateExpirationVisiteTechnique());
        vehicule.setDateExpirationCarteGrise(dto.getDateExpirationCarteGrise());
        vehicule.setPuissanceFiscale(dto.getPuissanceFiscale());
        vehicule.setStatut(dto.getStatut());
        vehicule.setNomSocieteAssurance(dto.getNomSocieteAssurance());
        vehicule.setDateExpirationAssurance(dto.getDateExpirationAssurance());
        vehicule.setMontantAssurance(dto.getMontantAssurance());
        // La relation Parc n'est pas affectée ici (elle devra être gérée séparément si besoin)
        return vehicule;
    }

    // Mise à jour partielle
    private void updateEntity(Vehicule entity, VehiculeDTO dto) {
        if (dto.getMatricule() != null) entity.setMatricule(dto.getMatricule());
        if (dto.getNumeroChassis() != null) entity.setNumeroChassis(dto.getNumeroChassis());
        if (dto.getMarque() != null) entity.setMarque(dto.getMarque());
        if (dto.getModele() != null) entity.setModele(dto.getModele());
        if (dto.getTypeCarburant() != null) entity.setTypeCarburant(dto.getTypeCarburant());
        if (dto.getCouleur() != null) entity.setCouleur(dto.getCouleur());
        if (dto.getKilometre() != null) entity.setKilometre(dto.getKilometre());
        if (dto.getCapaciteReservoir() != null) entity.setCapaciteReservoir(dto.getCapaciteReservoir());
        if (dto.getDateMiseEnService() != null) entity.setDateMiseEnService(dto.getDateMiseEnService());
        if (dto.getDateExpirationVisiteTechnique() != null) entity.setDateExpirationVisiteTechnique(dto.getDateExpirationVisiteTechnique());
        if (dto.getDateExpirationCarteGrise() != null) entity.setDateExpirationCarteGrise(dto.getDateExpirationCarteGrise());
        if (dto.getPuissanceFiscale() != null) entity.setPuissanceFiscale(dto.getPuissanceFiscale());
        if (dto.getStatut() != null) entity.setStatut(dto.getStatut());
        if (dto.getNomSocieteAssurance() != null) entity.setNomSocieteAssurance(dto.getNomSocieteAssurance());
        if (dto.getDateExpirationAssurance() != null) entity.setDateExpirationAssurance(dto.getDateExpirationAssurance());
        if (dto.getMontantAssurance() != null) entity.setMontantAssurance(dto.getMontantAssurance());
        // Ne pas toucher à la relation Parc ici
    }
}