package org.example.socks.storage.controller.sock;

import lombok.RequiredArgsConstructor;
import org.example.socks.storage.controller.sock.advice.SockControllerExceptionHandler;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.example.socks.storage.service.excel.SockExcelReader;
import org.example.socks.storage.service.sock.SockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
@SockControllerExceptionHandler
public class SockController {
    private final SockService sockService;
    private final SockExcelReader sockExcelReader;

    @PostMapping("/income")
    public ResponseEntity<String> incomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You added socks, total count - %s.", sockService.incomeSock(sockDTO)));
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> outcomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You took socks, total count - %s.", sockService.outcomeSock(sockDTO)));
    }

    @GetMapping
    public ResponseEntity<String> getCountByConstraints(@RequestParam(name = "color", defaultValue = "") String color, @RequestParam(name = "lessThan", defaultValue = Integer.MAX_VALUE + "") Integer lessThan, @RequestParam(name = "equal", defaultValue = "-1") Integer equal, @RequestParam(value = "moreThan", defaultValue = Integer.MIN_VALUE + "") Integer moreThan) {
        return ResponseEntity.ok(String.format("Socks count by selected constraints: %s", sockService.countByConstraints(color, lessThan, equal, moreThan)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> changeSocks(@PathVariable("id") Long id, @RequestParam(name = "color", defaultValue = "") String color, @RequestParam(name = "cottonCount", defaultValue = "-1") Integer cottonCount, @RequestParam(value = "count", defaultValue = "-1") Long count) {
        sockService.updateSocks(id, color, cottonCount, count);
        return ResponseEntity.ok("Socks has been updated.");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> batch(@RequestParam("file") MultipartFile file) {
        sockExcelReader.readSheet(file);
        return ResponseEntity.ok("File has been read");
    }
}
