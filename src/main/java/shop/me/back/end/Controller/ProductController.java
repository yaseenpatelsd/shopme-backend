package shop.me.back.end.Controller;

import shop.me.back.end.Dto.ProductDto;
import shop.me.back.end.Entity.ProductEntity;
import shop.me.back.end.Exeption.SomeThingIsWrongExeption;
import shop.me.back.end.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import shop.me.back.end.Mapping.ProductMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapping productMapping;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add")
    public ResponseEntity<ProductDto> addProduct(Authentication authentication, @RequestBody ProductDto productDto){
        try {
            ProductDto productDto1=productService.addProduct(authentication,productDto);
            return ResponseEntity.ok(productDto1);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<ProductDto> editProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        try {
            ProductDto product=productService.editProduct(id,productDto);
            return ResponseEntity.ok(product);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delist/{id}")
    public ResponseEntity<?> delistProduct(@PathVariable long id , Authentication authentication){
        try {
            productService.deleteProduct(authentication, id);
            return ResponseEntity.ok("product is been dilisted now no buyer can see it");
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getAll(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) Double minPrice,
                                                   @RequestParam(required = false) Double maxPrice){
        try {
            List<ProductEntity> product=productService.findall(name, minPrice, maxPrice);
            List<ProductDto> productDtos=product.stream().map(productMapping::toDto).collect(Collectors.toList());


            return ResponseEntity.ok(productDtos);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-products")
    public ResponseEntity<List<ProductDto>> getAllProductForAdmin(Authentication authentication){
        try {
            List<ProductDto> productEntityList=productService.adminAllProduct(authentication);
            return ResponseEntity.ok(productEntityList);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test(Authentication auth) {
        return auth.getAuthorities().toString();
    }

}
