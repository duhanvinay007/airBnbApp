package com.duhanvinay.projects.airBnbApp.controller;


import com.duhanvinay.projects.airBnbApp.dto.InventoryDto;
import com.duhanvinay.projects.airBnbApp.dto.UpdateInventoryRequestDto;
import com.duhanvinay.projects.airBnbApp.service.InventoryServices;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServices inventoryService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<InventoryDto>> getAllInventoryByRoom(@PathVariable Long roomId) {
        return  ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }


    @PatchMapping("/rooms/{roomId}")
       @Operation(summary = "Update the inventory of a room", tags = {"Admin Inventory"})
    public ResponseEntity<Void> updateInventory(@PathVariable Long roomId,
                                                @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto) {
        inventoryService.updateInventory(roomId, updateInventoryRequestDto);
        return ResponseEntity.noContent().build();
    }
}
