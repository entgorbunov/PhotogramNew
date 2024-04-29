package com.photogram.validator;

import com.photogram.dto.CreateUserDto;
import com.photogram.entity.Gender;
import com.photogram.util.LocalDateFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CreateUserValidator implements Validator<CreateUserDto> {
    @Getter
    private static final CreateUserValidator INSTANCE = new CreateUserValidator();


    @Override
    public ValidationResult isValid(CreateUserDto createUserDto) {
        ValidationResult validationResult = new ValidationResult();
        Optional<Gender> gender = Gender.find(createUserDto.getGender());
        if (!LocalDateFormatter.isValid(createUserDto.getBirthday())) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        if (gender.isEmpty()) {
            validationResult.add(Error.of("invalid.gender", "Gender is invalid"));
        }
        return validationResult;
    }

}
