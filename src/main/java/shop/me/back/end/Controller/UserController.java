package shop.me.back.end.Controller;

import shop.me.back.end.Entity.UserEntity;
import shop.me.back.end.Exeption.IllegalArgumentException;
import shop.me.back.end.Jwt.JwtUtil;
import shop.me.back.end.Repository.UserRepository;
import shop.me.back.end.Service.EmailSander;
import shop.me.back.end.Util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.me.back.end.Dto.*;
import shop.me.back.end.Exeption.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
       @Autowired
    private OtpGenerator otpGenerator;

         @Autowired
    private EmailSander emailSander;



    @PostMapping("/admin/register")
    public ResponseEntity<?> adminRegisterUser(@RequestBody UserRequestDto userRequestDto){
       try {
           Optional<UserEntity> optionalUserEntity=userRepository.findByUsername(userRequestDto.getUsername());

           if (optionalUserEntity.isPresent()){
               throw new UserAlreadyExistExeption("Username is already register");
           }

           UserEntity user= new UserEntity();
           user.setUsername(userRequestDto.getUsername());
           user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
           user.setEmail(userRequestDto.getEmail());
           user.setRole("ADMIN");

           String otp=otpGenerator.generateOtp();
           Long optExpireTime=otpGenerator.otpExpireTime(10);

           emailSander.EmailVerificationOtpSand(user.getEmail(), otp);


           user.setOtp(otp);
           user.setOtpExpireTime(optExpireTime);
           user.setIsValid(false);
           userRepository.save(user);
           return ResponseEntity.ok("User Register successfully");
       }catch (Exception e){
           e.printStackTrace();
           throw new SomeThingIsWrongExeption(e.getMessage());
       }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto){
        try {
            Optional<UserEntity> optionalUserEntity=userRepository.findByUsername(userRequestDto.getUsername());

            if (optionalUserEntity.isPresent()){
                throw new UserAlreadyExistExeption("Username is already register");
            }

            UserEntity user= new UserEntity();
            user.setUsername(userRequestDto.getUsername());
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            user.setEmail(userRequestDto.getEmail());
            user.setRole("USER");

            String otp=otpGenerator.generateOtp();
            Long optExpireTime=otpGenerator.otpExpireTime(10);

            emailSander.EmailVerificationOtpSand(user.getEmail(), otp);


            user.setOtp(otp);
            user.setOtpExpireTime(optExpireTime);
            user.setIsValid(false);
            userRepository.save(user);
            return ResponseEntity.ok("User Register successfully");
        }catch (Exception e){
            e.printStackTrace();
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @PostMapping("/account-verification")
    public ResponseEntity<?> accountVerification(@RequestBody EmailVerificationDto emailVerificationDto){
      try {

          if (emailVerificationDto.getUsername()==null|| emailVerificationDto.getOtp()==null || emailVerificationDto.getUsername().isEmpty()|| emailVerificationDto.getOtp().isEmpty()){
              throw new OtpRelatedExeptions("Cant process with null input");
          }

          UserEntity user=userRepository.findByUsername(emailVerificationDto.getUsername())
                  .orElseThrow(()->new UserNotFoundExeption("Username not found"));

          String storeOtp= user.getOtp();
          Long otpExpireTime= user.getOtpExpireTime();

          if (storeOtp==null || !storeOtp.equals(emailVerificationDto.getOtp())){
              throw new OtpRelatedExeptions("Incorrect otp please reEnter Correct otp");
          }
          if (otpExpireTime<System.currentTimeMillis()){
              throw new OtpRelatedExeptions("Otp is expire please request new otp");
          }

          user.setOtp(null);
          user.setOtpExpireTime(null);
          user.setIsValid(true);
          userRepository.save(user);
          return ResponseEntity.ok("Account is verified successfully");
      }catch (SomeThingIsWrongExeption e){
          e.printStackTrace();
          throw new SomeThingIsWrongExeption(e.getMessage());
      }
    }


    @PostMapping("/otp-request")
    public ResponseEntity<?> otpRequest(@RequestBody OtpRequestDto otpRequestDto){
       try {
           UserEntity user=userRepository.findByUsername(otpRequestDto.getUsername())
                   .orElseThrow(()-> new UserNotFoundExeption("Username cant be found "));

           String newOtp= otpGenerator.generateOtp();
           Long newExpireTime= otpGenerator.otpExpireTime(10);

           user.setOtp(newOtp);
           user.setOtpExpireTime(newExpireTime);

           emailSander.EmailForPasswordReset(user.getEmail(), newOtp);

           userRepository.save(user);
           return ResponseEntity.ok("Otp sand successfully");
       }catch (SomeThingIsWrongExeption e){
           e.printStackTrace();
           throw new SomeThingIsWrongExeption(e.getMessage());
       }
    }


    @PostMapping("/password-reset")
    public ResponseEntity<?> ResetPassword(@RequestBody PasswordResetDto passwordResetDto){
      try {


          if (passwordResetDto.getOtp()==null|| passwordResetDto.getUsername()==null || passwordResetDto.getUsername().isEmpty()|| passwordResetDto.getOtp().isEmpty() ){
              throw new OtpRelatedExeptions("Can't process with null input");
          }

          UserEntity user=userRepository.findByUsername(passwordResetDto.getUsername())
                  .orElseThrow(()-> new UserNotFoundExeption("Username not found"));

          String storeOtp=user.getOtp();
          Long storeExpireDate= user.getOtpExpireTime();

          if (storeOtp==null|| !storeOtp.equals(passwordResetDto.getOtp())){
              throw new OtpRelatedExeptions("Incorrect Otp please try again ");
          }
          if (storeExpireDate<System.currentTimeMillis()){
              throw new OtpRelatedExeptions("Otp is expire please request another otp and try again");
          }

          user.setOtp(null);
          user.setOtpExpireTime(null);
          user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));

          userRepository.save(user);
          return ResponseEntity.ok("the process of resitting  password is successful");
      }catch (SomeThingIsWrongExeption e){
          e.printStackTrace();
          throw new SomeThingIsWrongExeption(e.getMessage());
      }
    }
    @PostMapping("/login")
    public ResponseEntity<?> logInUser(@RequestBody UserRequestDto userRequestDto){
        try {
            UserEntity user=userRepository.findByUsername(userRequestDto.getUsername())
                    .orElseThrow(()-> new UserNotFoundExeption("Username not found"));

            if (user.getIsValid().equals(false)){
                throw new IllegalArgumentException("You are registered user but please verify email first to log in");
            }
            try {
                authenticationManager.authenticate(
                        new  UsernamePasswordAuthenticationToken(userRequestDto.getUsername(),userRequestDto.getPassword())
                );
            }catch (Exception e){
                throw new SomeThingIsWrongExeption(e.getMessage());
            }
            String token=jwtUtil.tokenGenerate(userRequestDto.getUsername());
            return ResponseEntity.ok(new UserResponseDto(token));
        }catch (Exception e){
            e.printStackTrace();
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

}
