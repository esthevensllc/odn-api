package com.claroetl.api.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

// @WebFluxTest(OdnAuthService.class)
public class OdnAuthServiceTest {
    // @Autowired
    // WebClient.Builder Webbuilder;

    private OdnAuthService api;
    
    @BeforeEach
    void setUp(){
        this.api = new OdnAuthService(WebClient.builder());
    }

    @Test
    public void testShouldGetToken(){
        var token = api.getToken();
        // String token = "123";
        Assertions.assertNotNull(token);
    }
}
