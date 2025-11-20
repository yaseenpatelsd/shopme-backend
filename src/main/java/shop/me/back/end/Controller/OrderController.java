package shop.me.back.end.Controller;

import shop.me.back.end.Enums.DeliveryStatus;
import shop.me.back.end.Exeption.RazorPayExeption;
import shop.me.back.end.Exeption.ResourceNotAvailableExeption;
import shop.me.back.end.Exeption.SomeThingIsWrongExeption;
import shop.me.back.end.Exeption.UserNotFoundExeption;
import shop.me.back.end.Service.OrderService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import shop.me.back.end.Mapping.OrderMapping;
import shop.me.back.end.Dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapping orderMapping;


    @PostMapping("/buy")
    public ResponseEntity<?> buyProduct(Authentication authentication, @RequestBody OrderRequestDto orderRequestDto){
        try {
            OrderResponseDto responseDto=orderService.buying(authentication,orderRequestDto);
            return ResponseEntity.ok(responseDto);
        } catch (RazorPayExeption | RazorpayException e) {
            throw new RazorPayExeption(e.getMessage());
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelProduct(Authentication authentication, @RequestBody OrderCancelDto orderCancelDto){
        try {
            orderService.cancelOrder(authentication,orderCancelDto);
            return ResponseEntity.ok("Order cancel successfully please check more info in order section");
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@RequestParam(required = false) DeliveryStatus status,
                                                               @RequestParam(required = false) Double minPrice,
                                                               @RequestParam(required = false) Double maxPrice){
        try {
            List<OrderResponseDto> findAll=orderService.getAllOrders(status,minPrice,maxPrice);
            return ResponseEntity.ok(findAll);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }catch (UserNotFoundExeption e){
            throw new UserNotFoundExeption(e.getMessage());
        }catch (ResourceNotAvailableExeption e){
            throw new ResourceNotAvailableExeption(e.getMessage());
        }
    }

    //Admin pannel

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/payment/status/change")
    public ResponseEntity<String> changePaymentStatus(Authentication authentication, @RequestBody OrderRequestPaymentStatusChangeDto orderRequestPaymentStatusChangeDto){
        try {
            orderService.changePaymentStatus(authentication, orderRequestPaymentStatusChangeDto);
            return ResponseEntity.ok("Change status successfully");
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/status/change")
    public ResponseEntity<String> changeDeliveryStatus(@RequestBody OrderStatusChangeDto orderRequstForStatusEditDto){
        try {
            orderService.changeDeliveryStatus(orderRequstForStatusEditDto);
            return ResponseEntity.ok("Delivery status change");
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

}
