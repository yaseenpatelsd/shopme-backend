package shop.me.back.end.Util;

import org.springframework.stereotype.Component;

@Component
public class OtpGenerator {

    public String generateOtp(){
        int otp=100000+(int)(Math.random()*900000);
        return String.valueOf(otp);
    }

    public Long otpExpireTime(long minute){
        return System.currentTimeMillis()+(minute*60*1000);
    }
}
