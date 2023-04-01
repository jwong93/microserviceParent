package com.giftgracious.orderService.dto;

import com.giftgracious.orderService.model.Item;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {

    private String orderNumber;
    private List<ItemDTO> itemDTOS;
}
