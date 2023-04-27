package com.giftgracious.productservice.Controller;


import com.giftgracious.productservice.dto.ProductRequestDTO;
import com.giftgracious.productservice.model.Product;
import com.giftgracious.productservice.service.ProductService;
import com.giftgracious.productservice.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @PostMapping
    public ResponseEntity<?> createProduct (@RequestBody ProductRequestDTO productRequest){
        productService.create(productRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts(){
        List<ProductResponseDTO> productResponseDTOS = productService.getAllItems();
        if (productResponseDTOS.isEmpty()){
            return new ResponseEntity<>("Selected Product does not exist",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productResponseDTOS,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getOneItem(@PathVariable String id){
        return new ResponseEntity<>(productService.returnDto(id),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteProduct(@RequestParam List<String> id){
        if (id.isEmpty()){
            return new ResponseEntity<>("No items to delete", HttpStatus.NOT_FOUND);
        }
        for (String s : id){
            productService.remove(s);
        }
        return new ResponseEntity<>("All items deleted",HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateProductDetails(@RequestBody ProductResponseDTO responseDTO){
        Product product = productService.findById(responseDTO);
        if (product == null) {
            return new ResponseEntity<>("No such item exist", HttpStatus.NOT_FOUND);
        }
        productService.update(responseDTO);
        return new ResponseEntity<>("Item updated!",HttpStatus.OK);

    }

}
