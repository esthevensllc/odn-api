package com.claroetl.api.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
// import com.fasterxml.jackson.databind.ObjectReader;

@Service
public class OdnAuthService {
    private final WebClient webClient;
    private final ObjectMapper mapper;

    @Value("${odnapi.username}")
    private String odnUsername;

    @Value("${odnapi.password}")
    private String odnPassword;

    public OdnAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://192.168.57.45:26335").build();
        this.mapper = new ObjectMapper();
    }

    public Mono<OdnToken> getToken() {
        /*return Mono.fromSupplier(() -> {
            String tokenString = "{\"accessSession\": \"x-qq2ng7ukrs84056r7yql5eemnsbz45tffy6nhghfhjk5lfo8oa89g5kbrzg7hcaqfs5hg708jtth1fkanuc8mlqmqoli3s6k3s0aobmnnytgakeps5dftgrx473x1idj\", \"roaRand\": \"d52a220e0297fca5594d0b7b52894d00ebd0601eb8c8dc6e\", \"expires\": 1800, \"additionalInfo\": null}";
            return tokenString;
        })
        .map(tokenString -> {
            try {
                var token = mapper.readValue(tokenString, OdnToken.class);
                token.setExpiresAt(LocalDateTime.now().plusSeconds(token.getExpires() - 60));
                return token;
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });*/

        return webClient.put()
            .uri("/rest/plat/smapp/v1/sessions")
            .bodyValue(new AuthRequest("password", odnUsername, odnPassword))
            .retrieve()
            .bodyToMono(String.class)
            .map(tokenString -> {
                try {
                    var token = mapper.readValue(tokenString, OdnToken.class);
                    token.setExpiresAt(LocalDateTime.now().plusSeconds(token.getExpires() - 60));
                    return token;
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private static class AuthRequest{
        private String grantType;
        private String userName;
        private String value;

        public AuthRequest(String grantType, String userName, String value){
            this.grantType = grantType;
            this.userName = userName;
            this.value = value;
        }

        public String getGrantType() {
            return grantType;
        }

        public String getUserName() {
            return userName;
        }

        public String getValue() {
            return value;
        }
    }
}
