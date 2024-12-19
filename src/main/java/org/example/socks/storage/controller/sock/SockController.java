package org.example.socks.storage.controller.sock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Sock API", description = "Controller, provides manipulations with socks.")
public class SockController {
    private final SockService sockService;
    private final SockExcelReader sockExcelReader;

    @Operation(summary = "Registering new groups of socks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucks group successfully registered."),
            @ApiResponse(responseCode = "400", description = "There was a problem while registering socks.")
    })
    @PostMapping("/income")
    public ResponseEntity<String> incomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You added socks, total count - %s.", sockService.incomeSock(sockDTO)));
    }

    @Operation(summary = "Takes some socks from the storage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The group of socks has been taken from the storage."),
            @ApiResponse(responseCode = "400", description = "There was a problem while taking socks.")
    })
    @PostMapping("/outcome")
    public ResponseEntity<String> outcomeSocks(@RequestBody SockDTO sockDTO) {
        return ResponseEntity.ok(String.format("You took socks, total count - %s.", sockService.outcomeSock(sockDTO)));
    }

    @Operation(summary = "Get count of socks by constraints, like color, cotton percent...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The count has been gotten."),
            @ApiResponse(responseCode = "400", description = "There is invalid constraints or no one sock found by this constraints.")
    })
    @GetMapping
    public ResponseEntity<String> getCountByConstraints(@RequestParam(name = "color", defaultValue = "") String color, @RequestParam(name = "lessThan", defaultValue = Integer.MAX_VALUE + "") Integer lessThan, @RequestParam(name = "equal", defaultValue = "-1") Integer equal, @RequestParam(value = "moreThan", defaultValue = Integer.MIN_VALUE + "") Integer moreThan) {
        return ResponseEntity.ok(String.format("Socks count by selected constraints: %s", sockService.countByConstraints(color, lessThan, equal, moreThan)));
    }

    @Operation(summary = "Change information about socks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socks has been changed."),
            @ApiResponse(responseCode = "400", description = "There was a problem while changing socks.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> changeSocks(@PathVariable("id") Long id, @RequestParam(name = "color", defaultValue = "") String color, @RequestParam(name = "cottonCount", defaultValue = "-1") Integer cottonCount, @RequestParam(value = "count", defaultValue = "-1") Long count) {
        sockService.updateSocks(id, color, cottonCount, count);
        return ResponseEntity.ok("Socks has been updated.");
    }

    @Operation(summary = "Read a Excel file and registering the data as new socks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File has been read and the data were written."),
            @ApiResponse(responseCode = "400", description = "There was a problem while reading file or writing the data.")
    })
    @PostMapping("/batch")
    public ResponseEntity<String> batch(@RequestParam("file") MultipartFile file) {
        sockExcelReader.readSheet(file);
        return ResponseEntity.ok("File has been read.");
    }
}
