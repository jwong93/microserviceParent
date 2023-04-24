package com.giftgracious.inventoryServices.services;

import com.giftgracious.inventoryServices.dto.InventoryDTO;
import com.giftgracious.inventoryServices.model.Inventory;
import com.giftgracious.inventoryServices.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryDTO> isInStock (List<String> skucode) throws Exception{
        return inventoryRepository.findByskucodeIn(skucode)
                .stream()
                .map(inventory -> InventoryDTO.builder()
                        .skucode(inventory.getSkucode())
                        .isINStock(inventory.getQuantity() > 0)
                        .build())
                .toList();

    }
}
