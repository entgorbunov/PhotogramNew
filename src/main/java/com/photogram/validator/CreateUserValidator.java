package com.photogram.validator;

import com.photogram.dto.UserDto;
import com.photogram.entity.Gender;
import com.photogram.util.LocalDateTimeFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CreateUserValidator implements Validator<UserDto> {
    @Getter
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();


    @Override
    public ValidationResult isValid(UserDto userDto) {
        ValidationResult validationResult = new ValidationResult();
        Optional<Gender> gender = Gender.find(userDto.getGender());
        if (gender.isEmpty()) {
            validationResult.add(Error.of("invalid.gender", "Gender is invalid"));
        }
        if (Boolean.FALSE.equals(LocalDateTimeFormatter.isValid(userDto.getBirthday()))) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        return validationResult;
    }

}
