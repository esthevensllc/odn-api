package com.claroetl.api.filters;

import java.time.LocalDateTime;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import com.claroetl.api.services.OdnAuthService;
import com.claroetl.api.services.OdnToken;
import com.github.benmanes.caffeine.cache.Cache;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthTokenFilter implements GlobalFilter, Ordered {
    
    private final OdnAuthService authService;
    private final Cache<String, Object> cache;
    private String cache_token_key = "access_token";

    public AuthTokenFilter(OdnAuthService authService, Cache<String, Object> cache) {
        this.authService = authService;
        this.cache = cache;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/rest/plat/smapp/v1/sessions")) {
            return chain.filter(exchange);
        }
        
        return this.getToken()
        .flatMap(accessSession -> {
            var exchangeMapped = exchange.mutate()
                    .request(r -> r.headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessSession)))
                    .build();
    
            return chain.filter(exchangeMapped);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Mono<String> getToken(){
        Object tokenObj = cache.getIfPresent(this.cache_token_key);
        OdnToken token;
        if(tokenObj != null){
            token = (OdnToken) tokenObj;
            var now = LocalDateTime.now();
            if (token.getExpiresAt().isBefore(now)){
                return this.authService.getToken()
                .map(tokenSession -> {
                    cache.put(this.cache_token_key, tokenSession);
                    return tokenSession.getAccessSession();
                });
            }
            return Mono.fromSupplier(() -> token.getAccessSession());
        } else {
            // token = this.authService.getToken();
            // cache.put(this.cache_token_key, token);
            return this.authService.getToken()
                .map(tokenSession -> {
                    cache.put(this.cache_token_key, tokenSession);
                    return tokenSession.getAccessSession();
                });
        }
    }
}
