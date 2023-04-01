package com.giftgracious.orderService.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemDTO {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
