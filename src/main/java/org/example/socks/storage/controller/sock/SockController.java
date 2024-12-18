package org.example.socks.storage.controller.sock;

import lombok.RequiredArgsConstructor;
import org.example.socks.storage.controller.sock.advice.SockControllerExceptionHandler;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.example.socks.storage.service.sock.SockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
@SockControllerExceptionHandler
public class SockController {
    private final SockService sockService;

    @PostMapping("/income")
    public ResponseEntity<String> incomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You added socks, total count - %s.", sockService.incomeSock(sockDTO)));
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> outcomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You took socks, total count - %s.", sockService.outcomeSock(sockDTO)));
    }

    @GetMapping
    public ResponseEntity<String> getCountByConstraints(@RequestParam(name = "color", defaultValue = "") String color, @RequestParam(name = "lessThan", defaultValue = Integer.MAX_VALUE+"") Integer lessThan, @RequestParam(name = "equal", defaultValue = "-1") Integer equal, @RequestParam(value = "moreThan", defaultValue = Integer.MIN_VALUE+"") Integer moreThan) {
        return ResponseEntity.ok(String.format("Socks count by selected constraints: %s", sockService.countByConstraints(color, lessThan, equal, moreThan)));
    }
}
