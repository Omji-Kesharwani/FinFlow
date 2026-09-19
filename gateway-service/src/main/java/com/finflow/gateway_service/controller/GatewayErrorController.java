package com.finflow.gateway_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class GatewayErrorController {

    @RequestMapping("/gateway-error")
    public ResponseEntity<Map<String,Object>> gatewayError(){
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "SERVICE_UNAVAILABLE",
                        "message", "The requested service is currently unavailable",
                        "timestamp", Instant.now().toString()
                ));
    }
}
