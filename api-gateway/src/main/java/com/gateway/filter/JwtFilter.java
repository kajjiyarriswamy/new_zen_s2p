package com.gateway.filter;

import com.gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethodValue();
        
        logger.info("=== JWT Filter Processing ===");
        logger.info("Request Path: {}", path);
        logger.info("Request Method: {}", method);

        // Skip authentication for login endpoint
        if (path.contains("/auth/login")) {
            logger.info("Skipping JWT validation for auth/login endpoint");
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        logger.info("Authorization Header Present: {}", authHeader != null);
        
        if (authHeader != null) {
            logger.debug("Authorization Header Value: {}", authHeader);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header missing or invalid format for path: {}", path);
            logger.warn("Expected format: Bearer <token>");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        logger.debug("Token extracted from header: {}", token.substring(0, Math.min(20, token.length())) + "...");

        try {
            if (!jwtService.validateToken(token)) {
                logger.warn("Token validation failed for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            logger.info("Token validation successful");

            Claims claims = jwtService.extractClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            logger.info("Token Claims - Username: {}, Role: {}", username, role);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            logger.info("Authentication set for user: {} with role: {}", username, role);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    
        } catch (Exception e) {
            logger.error("Error processing JWT token: {}", e.getMessage(), e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;  // Execute filter before other filters
    }
}

