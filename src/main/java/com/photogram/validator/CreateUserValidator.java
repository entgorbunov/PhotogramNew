package com.photogram.validator;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.util.LocalDateTimeFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CreateUserValidator implements Validator<UserDtoFromWeb> {
    @Getter
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();


    @Override
    public ValidationResult isValid(UserDtoFromWeb userDtoFromWeb) {
        ValidationResult validationResult = new ValidationResult();
        Optional<Gender> gender = Gender.find(userDtoFromWeb.getGender());
        if (gender.isEmpty()) {
            validationResult.add(Error.of("invalid.gender", "Gender is invalid"));
        }
        if (Boolean.FALSE.equals(LocalDateTimeFormatter.isValid(userDtoFromWeb.getBirthday()))) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        return validationResult;
    }

}
