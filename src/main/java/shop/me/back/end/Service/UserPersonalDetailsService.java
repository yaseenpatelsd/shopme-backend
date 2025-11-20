package shop.me.back.end.Service;

import shop.me.back.end.Dto.UserPersonalDetailsRequestDto;
import shop.me.back.end.Entity.PersonalDetailsEntity;
import shop.me.back.end.Entity.UserEntity;
import shop.me.back.end.Exeption.IllegalArgumentException;
import shop.me.back.end.Exeption.ResourceALreadyAvailableExeption;
import shop.me.back.end.Exeption.ResourceNotAvailableExeption;
import shop.me.back.end.Exeption.UserNotFoundExeption;
import shop.me.back.end.Mapping.PersonalDetailsMapping;
import shop.me.back.end.Mapping.ProductMapping;
import shop.me.back.end.Repository.UserPersonalDetailsRepository;
import shop.me.back.end.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersonalDetailsService {
    @Autowired
    private UserPersonalDetailsRepository userPersonalDetailsRepository;
    @Autowired
    private UserRepository userRepository;

 @Autowired
    private PersonalDetailsMapping personalDetailsMapping;


    public UserPersonalDetailsRequestDto addUserDetails(Authentication authentication, UserPersonalDetailsRequestDto userPersonalDetailsRequestDto){
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UserNotFoundExeption("Cant find username"));

        Optional<PersonalDetailsEntity> optionalPersonalDetailsEntity=userPersonalDetailsRepository.findByUser_Username(user.getUsername());

        if (optionalPersonalDetailsEntity.isPresent()){
            throw new ResourceALreadyAvailableExeption("Your details is been already make if you want to edit something please go to edit personal details");
        }

        PersonalDetailsEntity personalDetailsEntity= new PersonalDetailsEntity();
        personalDetailsEntity.setUser(user);
        personalDetailsEntity.setFullName(userPersonalDetailsRequestDto.getFullName());
        personalDetailsEntity.setGender(userPersonalDetailsRequestDto.getGender());
        personalDetailsEntity.setMobileNo(userPersonalDetailsRequestDto.getMobileNo());
        personalDetailsEntity.setDateOfBirth(userPersonalDetailsRequestDto.getDateOfBirth());
        personalDetailsEntity.setAddress(userPersonalDetailsRequestDto.getAddress());
        personalDetailsEntity.setCity(userPersonalDetailsRequestDto.getCity());
        personalDetailsEntity.setState(userPersonalDetailsRequestDto.getState());
        personalDetailsEntity.setCountry(userPersonalDetailsRequestDto.getCountry());

         PersonalDetailsEntity personalDetailsSaved=userPersonalDetailsRepository.save(personalDetailsEntity);


        return personalDetailsMapping.toDto(personalDetailsSaved);
    }


    public UserPersonalDetailsRequestDto editUserDetails(Authentication authentication, UserPersonalDetailsRequestDto dto) {

        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundExeption("Cant find username"));

        PersonalDetailsEntity personal = userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                .orElseThrow(() -> new ResourceNotAvailableExeption("No personal details found. Please create first."));

        personal.setUser(user);

        if (dto.getFullName() != null) personal.setFullName(dto.getFullName());
        if (dto.getGender() != null) personal.setGender(dto.getGender());
        if (dto.getMobileNo() != null) personal.setMobileNo(dto.getMobileNo());
        if (dto.getDateOfBirth() != null) personal.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getAddress() != null) personal.setAddress(dto.getAddress());
        if (dto.getCity() != null) personal.setCity(dto.getCity());
        if (dto.getState() != null) personal.setState(dto.getState());
        if (dto.getCountry() != null) personal.setCountry(dto.getCountry());

        PersonalDetailsEntity saved = userPersonalDetailsRepository.save(personal);

        return personalDetailsMapping.toDto(saved);
    }


    public void deleteUser(Authentication authentication){

        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UserNotFoundExeption("User not found"));

        PersonalDetailsEntity personalDetails=userPersonalDetailsRepository.findByUser_Username(authentication.getName())
                .orElseThrow(()-> new ResourceNotAvailableExeption("Personal details not found"));


        if (!personalDetails.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("Not authorized to delete someone else personal details");
        }

        userPersonalDetailsRepository.delete(personalDetails);

    }

    public UserPersonalDetailsRequestDto getPersonalDetails(Authentication authentication){
        UserEntity user=userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UserNotFoundExeption("User not found"));

        PersonalDetailsEntity personalDetails=userPersonalDetailsRepository.findByUser_Username(user.getUsername())
                .orElseThrow(()-> new ResourceNotAvailableExeption("User profile does not esist"));

        return personalDetailsMapping.toDto(personalDetails);
    }
}
