package com.giftgracious.orderService.service;

import com.giftgracious.orderService.dto.InventoryDTO;
import com.giftgracious.orderService.dto.ItemDTO;
import com.giftgracious.orderService.dto.OrderRequestDTO;
import com.giftgracious.orderService.model.Order;
import com.giftgracious.orderService.model.Item;
import com.giftgracious.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    private final WebClient.Builder orderwebClient;

    public String placeOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datetime = date.format(formatter);
        order.setOrderNumber("GG"+datetime+orderRepository.count());

        List<Item> orderItems = orderRequestDTO.getItemDTOS()
                        .stream().map(this::maptoDTO).toList();

        order.setItems(orderItems);
        List<String> skucodes = order.getItems().stream()
                .map(Item::getSkuCode).toList();

        log.info(skucodes.toString());

        InventoryDTO [] result = orderwebClient.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skucode",skucodes).build())
                .retrieve()
                .bodyToMono(InventoryDTO [].class)
                .block();
        log.info(Arrays.toString(result));
        boolean productinStock = false;
        if (result.length > 0){
            productinStock = Arrays.stream(result).allMatch(InventoryDTO::isINStock);
        }

        log.info(productinStock+"");
        if (productinStock){
            orderRepository.save(order);
            log.info(order.getId()+"");
        }
        else{
            throw new IllegalArgumentException("No more stock");
        }

        return "Order placed Successfully with Order ID: "+orderRequestDTO.getOrderNumber();
    }
    public Item maptoDTO (ItemDTO iTemDTO){
        Item items = new Item();
        items.setPrice(iTemDTO.getPrice());
        items.setQuantity(iTemDTO.getQuantity());
        items.setSkuCode(iTemDTO.getSkuCode());
        return items;

    }

    public List<OrderRequestDTO> getallCurrentOrders (){
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream().map(this::mapOrdersDTO).toList();

    }

    public OrderRequestDTO mapOrdersDTO (Order order){
        List<Item> items = order.getItems();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (Item i : items){
            itemDTOS.add(ItemDTO.builder()
                    .skuCode(i.getSkuCode())
                    .price(i.getPrice())
                    .quantity(i.getQuantity()).build());

        }
        return OrderRequestDTO.builder()
                .itemDTOS(itemDTOS)
                .orderNumber(order.getOrderNumber())
                .build();

    }
}
