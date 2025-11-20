package shop.me.back.end.Controller;

import shop.me.back.end.Dto.UserPersonalDetailsRequestDto;
import shop.me.back.end.Exeption.SomeThingIsWrongExeption;
import shop.me.back.end.Service.UserPersonalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal")
public class PersonalDetailsController {
    @Autowired
    private UserPersonalDetailsService personalDetailsService;


    @PostMapping("/add")
    public ResponseEntity<UserPersonalDetailsRequestDto> addDetails(Authentication authentication, @RequestBody UserPersonalDetailsRequestDto personalDetailsRequestDto){
        try {
            UserPersonalDetailsRequestDto personalDetailsRequestDto1=personalDetailsService.addUserDetails(authentication,personalDetailsRequestDto);
            return ResponseEntity.ok(personalDetailsRequestDto1);
        }catch (Exception e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<UserPersonalDetailsRequestDto> edit(Authentication authentication, @RequestBody UserPersonalDetailsRequestDto userPersonalDetailsRequestDto){
        try {
            UserPersonalDetailsRequestDto saved=personalDetailsService.editUserDetails(authentication,userPersonalDetailsRequestDto);
            return ResponseEntity.ok(saved);
        }catch (Exception e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(Authentication authentication){
        try {
            personalDetailsService.deleteUser(authentication);
            return ResponseEntity.ok("Personal Detail is deleted");
        }catch (Exception e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<UserPersonalDetailsRequestDto> findTheUser(Authentication authentication){
        try {
          UserPersonalDetailsRequestDto userPersonalDetailsRequestDto=personalDetailsService.getPersonalDetails(authentication);
          return ResponseEntity.ok(userPersonalDetailsRequestDto);
        }catch (SomeThingIsWrongExeption e){
            throw new SomeThingIsWrongExeption(e.getMessage());
        }
    }
}
