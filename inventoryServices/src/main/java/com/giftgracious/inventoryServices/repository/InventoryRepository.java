package com.giftgracious.inventoryServices.repository;


import com.giftgracious.inventoryServices.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByskucodeIn(List<String> skucode);
}
