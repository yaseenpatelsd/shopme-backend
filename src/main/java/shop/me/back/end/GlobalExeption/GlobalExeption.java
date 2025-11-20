package shop.me.back.end.GlobalExeption;

import shop.me.back.end.Dto.ApiResponseDto;
import shop.me.back.end.Exeption.IllegalArgumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shop.me.back.end.Exeption.*;

import java.time.LocalDateTime;
@ControllerAdvice
public class GlobalExeption {

    @ExceptionHandler(UserNotFoundExeption.class)
    public ResponseEntity<ApiResponseDto> find(UserNotFoundExeption ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST,"User not exist ", ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistExeption.class)
    public ResponseEntity<ApiResponseDto> find(UserAlreadyExistExeption ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST,"User Already exist ", ex.getMessage(), request);
    }

    @ExceptionHandler(SomeThingIsWrongExeption.class)
    public ResponseEntity<ApiResponseDto> find(SomeThingIsWrongExeption ex, HttpServletRequest request){
        return build(HttpStatus.INTERNAL_SERVER_ERROR,"Something is wrong please try again ", ex.getMessage(), request);
    }
    @ExceptionHandler(ResourceALreadyAvailableExeption.class)
    public ResponseEntity<ApiResponseDto> find(ResourceALreadyAvailableExeption ex, HttpServletRequest request){
        return build(HttpStatus.ALREADY_REPORTED,"Resource Exist", ex.getMessage(), request);
    }
    @ExceptionHandler(ResourceNotAvailableExeption.class)
    public ResponseEntity<ApiResponseDto> find(ResourceNotAvailableExeption ex, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND,"Resource Not Found ", ex.getMessage(), request);
    }
  @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> find(IllegalArgumentException ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST,"Error ", ex.getMessage(), request);
    }
    @ExceptionHandler(RazorPayExeption.class)
    public ResponseEntity<ApiResponseDto> find(RazorPayExeption ex, HttpServletRequest request){
        return build(HttpStatus.PRECONDITION_FAILED,"Something went wrong with razorpay", ex.getMessage(), request);
    }
   @ExceptionHandler(OtpRelatedExeptions.class)
    public ResponseEntity<ApiResponseDto> find(OtpRelatedExeptions ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST,"Something is wrong with otp", ex.getMessage(), request);
    }


    public ResponseEntity<ApiResponseDto> build(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ){
        ApiResponseDto apiResponseDto=new ApiResponseDto(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiResponseDto);
    }
}
