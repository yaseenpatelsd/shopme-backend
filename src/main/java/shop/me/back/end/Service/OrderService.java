package shop.me.back.end.Service;

import shop.me.back.end.Config.RazorPayConfig;
import shop.me.back.end.Dto.*;
import shop.me.back.end.Entity.*;
import shop.me.back.end.Enums.DeliveryStatus;
import shop.me.back.end.Enums.PaymentStatus;
import shop.me.back.end.Exeption.IllegalArgumentException;
import shop.me.back.end.Exeption.ResourceNotAvailableExeption;
import shop.me.back.end.Exeption.SomeThingIsWrongExeption;
import shop.me.back.end.Exeption.UserNotFoundExeption;
import shop.me.back.end.Mapping.OrderMapping;
import shop.me.back.end.Mapping.ProductMapping;
import shop.me.back.end.Mapping.PurchaseDetailsMapping;
import shop.me.back.end.Repository.*;
import shop.me.back.end.Util.BuyingId;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import shop.me.back.end.Specification.OrderSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RazorPayConfig razorPayConfig;
 @Autowired
    private UserRepository userRepository;
@Autowired
    private RazorPayRepository razorPayRepository;
@Autowired
    private OrderMapping orderMapping;
@Autowired
    private ProductMapping productMapping;

@Autowired
    private EmailSander emailSander;
@Autowired
    private UserPersonalDetailsRepository userPersonalDetailsRepository;
@Autowired
    private PurchaseDetailsRepository purchaseDetailsRepository;
@Autowired
    private PurchaseDetailsMapping purchaseDetailsMapping;




@Transactional
    public OrderResponseDto buying(Authentication authentication, OrderRequestDto orderRequestDto)throws RazorpayException {
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundExeption("User not found"));
        ProductEntity productEntity=productRepository.findById(orderRequestDto.getId())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Can't find the product"));

        Long quantity=orderRequestDto.getQuentity();
        double totalPrice=quantity* productEntity.getPrice();

        if (orderRequestDto.getQuentity()==0){
            throw new IllegalArgumentException("Please select atleast one product");
        }
        if (productEntity.getQuantity()==0){
            throw new IllegalArgumentException("Out of stock");
        }
        if (productEntity.getQuantity()<quantity){
            throw new ResourceNotAvailableExeption("we apologize for not having enough stock");
        }
        if (productEntity.getIsActive().equals(false)){
            throw new SomeThingIsWrongExeption("Product is dilisted");
        }
        JSONObject object=new JSONObject();
        object.put("amount",(int)(totalPrice*100));
        object.put("currency","INR");
        object.put("receipt", "txn_" + System.currentTimeMillis());
        object.put("payment_capture", 1);


        Order order=razorPayConfig.razorpayClient().orders.create(object);

        RazorPayEntity entity = RazorPayEntity.builder()
                .amount(totalPrice)
                .currency("INR")
                .status(order.get("status"))
                .razorpayOrderId(order.get("id"))
                .razorpayPaymentId("Test-Payment"+ System.currentTimeMillis())
                .product(productEntity)
                .user(user)
                .build();
        razorPayRepository.save(entity);


        productEntity.setQuantity(productEntity.getQuantity()-quantity);
        productRepository.save(productEntity);


        OrdersEntity ordersEntity=new OrdersEntity();
        ordersEntity.setOrderNumber(BuyingId.GenerateId());
        ordersEntity.setRazorpayOrderId(entity.getRazorpayOrderId());
        ordersEntity.setRazorPayId(entity.getRazorpayPaymentId());
        ordersEntity.setQuantity(Double.valueOf(quantity));
        ordersEntity.setPaymentStatus(PaymentStatus.Pending);
        ordersEntity.setStatus(DeliveryStatus.Pending);
        ordersEntity.setTotalAmount(Double.valueOf(productEntity.getPrice()*orderRequestDto.getQuentity()));
        ordersEntity.setUser(user);
        ordersEntity.setProduct(productEntity);

        orderRepository.save(ordersEntity);

    Long savedOrderId = ordersEntity.getId();

    PersonalDetailsEntity personalDetails=userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                    .orElseThrow(()-> new ResourceNotAvailableExeption("Personal details not found"));

    PurchaseEmailDto purchaseEmailDto=new PurchaseEmailDto();
    purchaseEmailDto.setFullname(personalDetails.getFullName());
    purchaseEmailDto.setOrderNumber(ordersEntity.getOrderNumber());
    purchaseEmailDto.setProductName(productEntity.getName());
    purchaseEmailDto.setQuantity(Double.valueOf(quantity));
    purchaseEmailDto.setPricePerProduct(productEntity.getPrice().doubleValue());
    purchaseEmailDto.setOrderDate(LocalDateTime.now());


    purchaseEmailDto.setTotalAmount(totalPrice);
    purchaseEmailDto.setAddress(personalDetails.getAddress());

   PurchaseDetailsEntity purchaseDetailsEntity = purchaseDetailsMapping.toEntity(purchaseEmailDto);
    purchaseDetailsEntity.setOrders(ordersEntity);
    purchaseDetailsRepository.save(purchaseDetailsEntity);


    emailSander.EmailAfterPurchase(user.getEmail(), purchaseEmailDto);

      OrderResponseDto dto= orderMapping.toDto(ordersEntity);

      dto.setId(savedOrderId);

      return dto;

    }

     public String cancelOrder(Authentication authentication, OrderCancelDto orderCancelDto){
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundExeption("Username not found"));
        OrdersEntity ordersEntity=orderRepository.findById(orderCancelDto.getOrderId())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Order never happens"));

        if (ordersEntity.getStatus().equals(DeliveryStatus.Cancelled)){
            throw new IllegalArgumentException("Already canceled onces cant do it again");
        }

        if (!user.getUsername().equals(ordersEntity.getUser().getUsername())){
            throw new IllegalArgumentException("CANT CANCEL THE USERNAME AND TOKEN DONT MATCH");
        }

        if (ordersEntity.getStatus().equals(DeliveryStatus.Delivered)){
            throw new SomeThingIsWrongExeption("Cant cancel the product please apply for refund instead if available");
        }


        ordersEntity.setStatus(DeliveryStatus.Cancelled);

        orderRepository.save(ordersEntity);

        if(ordersEntity.getPaymentStatus().equals(PaymentStatus.Successful)){
                ordersEntity.setPaymentStatus(PaymentStatus.RefundInitiated);
                System.out.println("Refund simulated for backend-only mode: " + ordersEntity.getRazorPayId());

        }

       ProductEntity product=productRepository.findById(orderCancelDto.getProductId())
                       .orElseThrow(()-> new ResourceNotAvailableExeption("Product not available"));

         long restored = product.getQuantity() + ordersEntity.getQuantity().longValue();
         product.setQuantity(restored);
         productRepository.save(product);

         PersonalDetailsEntity personalDetails=userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                 .orElseThrow(()->new ResourceNotAvailableExeption("User personal details not found"));


         PurchaseDetailsEntity purchaseDetailsEntity = purchaseDetailsRepository
                 .findByOrders_Id(orderCancelDto.getOrderId())
                 .orElseThrow(()-> new IllegalArgumentException("Purchase Detials not found "));

         purchaseDetailsEntity.setFullname(personalDetails.getFullName());
         purchaseDetailsEntity.setOrderNumber(ordersEntity.getOrderNumber());
         purchaseDetailsEntity.setProductName(product.getName());
         purchaseDetailsEntity.setStatusUpDateDate(LocalDateTime.now());
         purchaseDetailsEntity.setQuantity(ordersEntity.getQuantity());
         purchaseDetailsEntity.setTotalAmount(ordersEntity.getTotalAmount());
         purchaseDetailsEntity.setOrders(ordersEntity);

         PurchaseEmailDto purchaseEmailDto = purchaseDetailsMapping.toDto(purchaseDetailsEntity);

         emailSander.EmailAfterCancellation(user.getEmail(), purchaseEmailDto);



         return "Please leave a review why you cancel the product for us to improve our service";
     }


     public List<OrderResponseDto> getAllOrders(DeliveryStatus status, Double minPrice, Double maxPrice){
         Specification<OrdersEntity> spec=null;

         if (status!=null){
             spec=(spec==null) ? OrderSpecification.findByStatus(status) : spec.and(OrderSpecification.findByStatus(status));
         }

         if (minPrice!=null && maxPrice!=null){
             spec=(spec==null)? OrderSpecification.findBetweenPrice(minPrice, maxPrice) : spec.and(OrderSpecification.findBetweenPrice(minPrice, maxPrice));
         }
          List<OrdersEntity> ordersEntities= (spec==null)? orderRepository.findAll(): orderRepository.findAll(spec);

         return ordersEntities.stream().map(orderMapping::toDto).collect(Collectors.toList());
     }
     //admin Service logic



    public String changePaymentStatus(Authentication authentication, OrderRequestPaymentStatusChangeDto orderRequestPaymentStatusChangeDto){
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundExeption("Username not found"));

        OrdersEntity ordersEntity=orderRepository.findById(orderRequestPaymentStatusChangeDto.getId())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Order not found"));


        ordersEntity.setPaymentStatus(orderRequestPaymentStatusChangeDto.getStatus());

        orderRepository.save(ordersEntity);
        return "Payment status is change successfully";

    }

    public String changeDeliveryStatus(OrderStatusChangeDto orderStatusChangeDto) {

        OrdersEntity orders=orderRepository.findById(orderStatusChangeDto.getOrderID())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Order details not found"));

        UserEntity user = userRepository.findByUsername(orders.getUser().getUsername())
                .orElseThrow(() -> new UserNotFoundExeption("Username not found"));

        PersonalDetailsEntity personalDetails=userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Personal details not found"));

       PurchaseDetailsEntity purchaseDetailsEntity=purchaseDetailsRepository.findByOrders_Id(orderStatusChangeDto.getOrderID())
                       .orElseThrow(()->new ResourceNotAvailableExeption("Purchase details cant be found"));

       ProductEntity productEntity=productRepository.findById(orderStatusChangeDto.getProductId())
                       .orElseThrow(()-> new ResourceNotAvailableExeption("product not found"));

        PurchaseEmailDto dto = new PurchaseEmailDto();
        dto.setFullname(personalDetails.getFullName());
        dto.setOrderNumber(orders.getOrderNumber());
        dto.setOrderDate(purchaseDetailsEntity.getOrderDate());
        dto.setStatusUpDateDate(LocalDateTime.now());
        dto.setDeliveryStatus(orderStatusChangeDto.getDeliveryStatus());
        dto.setProductName(productEntity.getName());
        dto.setQuantity((double) purchaseDetailsEntity.getQuantity());
        dto.setPricePerProduct(productEntity.getPrice());
        dto.setTotalAmount(orders.getTotalAmount());
        dto.setAddress(personalDetails.getAddress());

        // ⭐ Update order status
        orders.setStatus(orderStatusChangeDto.getDeliveryStatus());
        orderRepository.save(orders);

       emailSander.EmailAfterStatusUpdate(user.getEmail(),dto,orderStatusChangeDto.getDeliveryStatus());
        return "Delivery status Successfully change";
    }
}
