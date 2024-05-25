package com.photogram.validator;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.util.LocalDateTimeFormatter;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class CreateUserValidator implements Validator<UserDtoFromWeb> {

    private static final AtomicReference<CreateUserValidator> CREATE_USER_VALIDATOR_ATOMIC_REFERENCE = new AtomicReference<>();

    public static CreateUserValidator getCreateUserValidatorAtomicReference() {
        CREATE_USER_VALIDATOR_ATOMIC_REFERENCE.get();
        if (CREATE_USER_VALIDATOR_ATOMIC_REFERENCE.get() == null) {
            CreateUserValidator validator = new CreateUserValidator();
            if (CREATE_USER_VALIDATOR_ATOMIC_REFERENCE.compareAndSet(null, validator)) {
                return validator;
            } else {
                return CREATE_USER_VALIDATOR_ATOMIC_REFERENCE.get();
            }
        }
        return CREATE_USER_VALIDATOR_ATOMIC_REFERENCE.get();
    }


    @Override
    public ValidationResult isValid(UserDtoFromWeb userDtoFromWeb) {
        ValidationResult validationResult = new ValidationResult();
        Optional<Gender> gender = Gender.find(String.valueOf(userDtoFromWeb.getGender()));
        if (gender.isEmpty()) {
            validationResult.add(Error.of("invalid.gender", "Gender is invalid"));
        }
        if (Boolean.FALSE.equals(LocalDateTimeFormatter.isValid(String.valueOf(userDtoFromWeb.getBirthday())))) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        return validationResult;
    }

}
