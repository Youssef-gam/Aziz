package com.parc.controller;

import com.parc.dto.AuthRequest;
import com.parc.dto.AuthResponse;
import com.parc.dto.UtilisateurDTO;
import com.parc.security.JwtUtil;
import com.parc.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getMotDePasse())
            );
        } catch (BadCredentialsException ex) {
            boolean legacyAuthenticated = utilisateurService.authenticateLegacyPasswordAndUpgrade(
                    email,
                    request.getMotDePasse()
            );
            if (!legacyAuthenticated) {
                throw new BadCredentialsException("Identifiants incorrects", ex);
            }
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Identifiants incorrects", ex);
        }

        String token = jwtUtil.generateToken(email);
        UtilisateurDTO utilisateur = utilisateurService.getByEmail(email);
        AuthResponse response = new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getRole().name(),
                utilisateur.getNom(),
                utilisateur.getPrenom()
        );
        return ResponseEntity.ok(response);
    }
}
