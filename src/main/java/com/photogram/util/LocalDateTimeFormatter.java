package com.photogram.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@UtilityClass
public class LocalDateTimeFormatter {
    private static final String PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public static LocalDate format(String date) {
        try {
            return LocalDate.parse(date, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Could not parse date: " + date, date, e.getErrorIndex());
        }
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

    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? FORMATTER.format(dateTime.toLocalDate()) : null;
    }

}
