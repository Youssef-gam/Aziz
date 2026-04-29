package com.parc.service;

import com.parc.domain.entity.Utilisateur;
import com.parc.dto.UtilisateurDTO;
import com.parc.mapper.UtilisateurMapper;
import com.parc.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository repository;
    private final UtilisateurMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = normalizeEmail(email);

        Utilisateur user = repository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + normalizedEmail));

        return User.builder()
                .username(user.getEmail())
                .password(user.getMotDePasse())
                .authorities("ROLE_" + user.getRole().name())
                .disabled(!user.isActif())
                .build();
    }

    @Transactional
    public boolean authenticateLegacyPasswordAndUpgrade(String email, String rawPassword) {
        if (rawPassword == null) {
            return false;
        }

        Utilisateur user = repository.findByEmailIgnoreCase(normalizeEmail(email)).orElse(null);
        if (user == null || user.getMotDePasse() == null || !user.isActif()) {
            return false;
        }

        String storedPassword = user.getMotDePasse();

        // Compatibilite legacy: ancien mot de passe stocke en clair.
        if (!isBcryptHash(storedPassword) && storedPassword.equals(rawPassword)) {
            user.setMotDePasse(passwordEncoder.encode(rawPassword));
            repository.save(user);
            return true;
        }

        return false;
    }

    private boolean isBcryptHash(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }

    @Transactional
    public UtilisateurDTO create(UtilisateurDTO dto) {
        dto.setEmail(normalizeEmail(dto.getEmail()));

        String rawPassword = dto.getMotDePasse();

        if (rawPassword == null || rawPassword.isEmpty()) {
            rawPassword = "default123";
        }

        // Toujours stocker un mot de passe chiffre (BCrypt).
        dto.setMotDePasse(passwordEncoder.encode(rawPassword));

        Utilisateur user = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(user));
    }

    public List<UtilisateurDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Transactional
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {

        Utilisateur user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mettre à jour l'entité (sauf le mot de passe)
        dto.setEmail(normalizeEmail(dto.getEmail()));
        mapper.updateEntity(user, dto);

        // Mettre à jour le mot de passe UNIQUEMENT s'il est fourni
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            // Hashage sécurisé du mot de passe
            user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        return mapper.toDTO(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public UtilisateurDTO getByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        return repository.findByEmailIgnoreCase(normalizedEmail)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec email : " + normalizedEmail));
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}