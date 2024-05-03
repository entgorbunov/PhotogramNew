package com.photogram.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@UtilityClass
public class LocalDateTimeFormatter {
    public static final String PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public LocalDateTime format(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    public Boolean isValid(String date) {
        try {
            return Optional.ofNullable(date)
                    .map(LocalDateTimeFormatter::format)
                    .isPresent();
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
