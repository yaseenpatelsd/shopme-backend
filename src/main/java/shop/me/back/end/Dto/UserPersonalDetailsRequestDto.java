package shop.me.back.end.Dto;


import shop.me.back.end.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPersonalDetailsRequestDto {
    private String fullName;
    private Gender gender;
    private String mobileNo;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String country;

}
