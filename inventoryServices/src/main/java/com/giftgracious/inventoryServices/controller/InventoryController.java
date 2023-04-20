package com.giftgracious.inventoryServices.controller;

import com.giftgracious.inventoryServices.dto.InventoryDTO;
import com.giftgracious.inventoryServices.model.Inventory;
import com.giftgracious.inventoryServices.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryDTO> isInStock (@RequestParam List<String> skucode) throws Exception {
        return inventoryService.isInStock(skucode);
    }
}
