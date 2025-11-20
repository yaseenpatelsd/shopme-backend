package shop.me.back.end.Mapping;

import org.springframework.stereotype.Component;
import shop.me.back.end.Dto.UserPersonalDetailsRequestDto;
import shop.me.back.end.Entity.PersonalDetailsEntity;

@Component
public class PersonalDetailsMapping {

    public PersonalDetailsEntity toEntity(UserPersonalDetailsRequestDto dto) {
        if (dto == null) return null;

        return PersonalDetailsEntity.builder()
                .fullName(dto.getFullName())
                .gender(dto.getGender())
                .mobileNo(dto.getMobileNo())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .build();
    }

    public UserPersonalDetailsRequestDto toDto(PersonalDetailsEntity entity) {
        if (entity == null) return null;

        UserPersonalDetailsRequestDto dto = new UserPersonalDetailsRequestDto();
        dto.setFullName(entity.getFullName());
        dto.setGender(entity.getGender());
        dto.setMobileNo(entity.getMobileNo());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());

        return dto;
    }
}
