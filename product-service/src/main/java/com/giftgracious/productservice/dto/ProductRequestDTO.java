package com.giftgracious.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.sql.Blob;
import java.util.*;
import java.math.BigDecimal;
import java.awt.image.BufferedImage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;

    private List<Blob> imageBytes;

}
