package shop.me.back.end.Service;

import shop.me.back.end.Dto.ProductDto;
import shop.me.back.end.Entity.PersonalDetailsEntity;
import shop.me.back.end.Entity.ProductEntity;
import shop.me.back.end.Entity.UserEntity;
import shop.me.back.end.Exeption.ResourceNotAvailableExeption;
import shop.me.back.end.Exeption.UserNotFoundExeption;
import shop.me.back.end.Mapping.ProductMapping;
import shop.me.back.end.Repository.ProductRepository;
import shop.me.back.end.Repository.UserPersonalDetailsRepository;
import shop.me.back.end.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import shop.me.back.end.Specification.Product;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapping productMapping;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPersonalDetailsRepository userPersonalDetailsRepository;


    public ProductDto addProduct(Authentication authentication, ProductDto productDto) {
        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundExeption("Username not found"));

        PersonalDetailsEntity personalDetails = userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                .orElseThrow(() -> new ResourceNotAvailableExeption("personal information of Seller not found"));

        ProductEntity productEntity = productMapping.toEntity(productDto);
        productEntity.setSellerName(personalDetails.getFullName());
        productEntity.setSellerAddress(personalDetails.getAddress());
        productEntity.setQuantity(productDto.getQuantity());
        productEntity.setUser(user);
        productEntity.setIsActive(true);
        ProductEntity saved = productRepository.save(productEntity);
        return productMapping.toDto(saved);
    }

    public ProductDto editProduct(Long id, ProductDto productDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableExeption("Product don't  exist"));
        if (productDto.getName() != null) {
            productEntity.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            productEntity.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            productEntity.setPrice(productDto.getPrice());
        }
        if (productDto.getImageUrl() != null) {
            productEntity.setImageUrl(productDto.getImageUrl());
        }if (productDto.getQuantity()!=null){
            productEntity.setQuantity(productDto.getQuantity());
        }
        ProductEntity saved = productRepository.save(productEntity);
        return productMapping.toDto(saved);
    }

    private Specification<ProductEntity> isActive() {
        return (root, query, cb) ->
                cb.equal(root.get("isActive"), true);
    }

    public List<ProductEntity> findall(String name, Double minPrice, Double maxPrice) {

        Specification<ProductEntity> spec = null;

        spec=(spec==null)? Product.findByIsActive(true):
                spec.and(Product.findByIsActive(true));

        if (name != null && !name.isEmpty()) {
            spec = (spec == null) ? Product.findByName(name)
                    : spec.and(Product.findByName(name));
        }
        if (minPrice != null || maxPrice != null) {
            spec = (spec == null) ? Product.findByBetweenPrice(minPrice, maxPrice)
                    : spec.and(Product.findByBetweenPrice(minPrice, maxPrice));
        }

        return (spec == null) ? productRepository.findAll() : productRepository.findAll(spec);
    }


    public void deleteProduct(Authentication authentication, Long id) {
        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundExeption("User not found"));

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableExeption("Product don't  exist"));

       productEntity.setIsActive(false);

       productRepository.save(productEntity);
    }


    public List<ProductDto> adminAllProduct(Authentication authentication){
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundExeption("User not found"));

        List<ProductEntity> products=productRepository.findAll();

        return products.stream().map(productMapping::toDto).collect(Collectors.toList());
    }


}
