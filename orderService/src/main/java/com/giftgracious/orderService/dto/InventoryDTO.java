package com.giftgracious.orderService.dto;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class InventoryDTO {

    private String skucode;
    private boolean isINStock;
}
