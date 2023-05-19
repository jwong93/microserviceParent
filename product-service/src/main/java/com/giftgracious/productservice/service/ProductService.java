package com.giftgracious.productservice.service;


import com.giftgracious.productservice.dto.ProductRequestDTO;
import com.giftgracious.productservice.dto.ProductResponseDTO;
import com.giftgracious.productservice.model.Image;
import com.giftgracious.productservice.model.Product;
import com.giftgracious.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService{

    private final ProductRepository productRepository;

    public void create (ProductRequestDTO req) throws SQLException, IOException {
        Product product = Product.builder().name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

        List<Image> imageObject = imageUrlConverter(req.getImageBytes(),product.getId()).stream().map(this::maptoImage).toList();
        product.setImage(imageObject);
        productRepository.save(product);

    }

    public List<String> imageUrlConverter (List<Blob> imageBlob, String productId) throws SQLException, IOException {
        List<byte []> byteBlobs = new ArrayList<>();
        for (Blob b : imageBlob){
            byteBlobs.add(b.getBytes(0,(int)b.length()));
        }
        List<String> imageUrl = new ArrayList<>();
        String path = "\\product-service\\src\\main\\resources\\static\\images\\"+productId;
        File f1 = new File (path);
        boolean existance = f1.mkdir();
        if (existance){
            for (int i = 0; i < byteBlobs.size(); i++){
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteBlobs.get(i)));
                String imagePath = path+"\\000"+i+".jpg";
                imageUrl.add(imagePath);
                File imageN = new File(imagePath);
                ImageIO.write(image,"jpg", imageN);
            }
        }
        return imageUrl;
    }

    public void update (ProductResponseDTO res){
        Product prod = Product.builder().id(res.getId())
                .price(res.getPrice()).description(res.getDescription())
                .name(res.getName()).build();
        productRepository.save(prod);
    }

    public void remove (String id){
        productRepository.deleteById(id);
    }

    public Product findById(ProductResponseDTO dto){
        Optional<Product> prod = productRepository.findById(dto.getId());
        if (prod.isPresent()){
            Product prod1 = prod.get();
            return prod1;
        }
        return null;
    }

    public ProductResponseDTO returnDto(String id){
        Optional<Product> p = productRepository.findById(id);
        Product m = p.get();
        return mapToProductRes(m);
    }

    public Image maptoImage (String url){
        Image image = new Image();
        image.setUrl(url);
        return image;

    }

    public List<ProductResponseDTO> getAllItems (){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductRes).toList();
    }

    public ProductResponseDTO mapToProductRes (Product product){
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
