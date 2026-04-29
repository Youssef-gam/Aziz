package com.parc.controller;

import com.parc.dto.FcmTokenRequest;
import com.parc.dto.GarageDTO;
import com.parc.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GarageController {

    private final GarageService garageService;

    // ========== Méthodes spécifiques (sans {id}) ==========
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping("/{id}/fcm-token")
    public ResponseEntity<Void> registerFcmToken(@PathVariable Long id,
                                                 @RequestBody FcmTokenRequest request) {
        garageService.updateFcmToken(id, request.getToken());
        return ResponseEntity.ok().build();
    }

    // ========== CRUD générique ==========
    @PostMapping
    public ResponseEntity<GarageDTO> create(@RequestBody GarageDTO dto) {
        return ResponseEntity.ok(garageService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<GarageDTO>> getAll() {
        return ResponseEntity.ok(garageService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> update(@PathVariable Long id, @RequestBody GarageDTO dto) {
        return ResponseEntity.ok(garageService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        garageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}