package com.finflow.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class PaymentGatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> paymentRoute(){
        return route("payment-service")
                .route(path("/api/payments/**"),http())
                .before(uri("http://localhost:8084"))
                .filter(
                        circuitBreaker(
                                "paymentCircuitBreaker",
                                URI.create("forward:/gateway-error")
                        )
                ).build() ;
    }
}
