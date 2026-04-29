package com.parc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class GestionParcApplicationTests {

    // Mock du UserDetailsService pour que Spring puisse démarrer le contexte
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void contextLoads() {
        // Test minimal pour vérifier le démarrage du contexte
    }
}   