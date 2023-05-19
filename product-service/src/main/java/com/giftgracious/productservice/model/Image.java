package com.giftgracious.productservice.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "image")
@Getter
@Setter
@Data
@NoArgsConstructor
public class Image {

    @Id
    private long id;

    private String url;
}
