package shop.me.back.end.Util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class BuyingId {

    public static String GenerateId(){
      String timeStamp= LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
      int random= 10000+(int)(Math.random()*90000);
      return "ORC-"+timeStamp+"-"+random;
    }
}
