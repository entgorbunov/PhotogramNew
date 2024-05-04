package com.photogram.util;

public enum PageEnum {
    USERS("web/WEB-INF/jsp/users.jsp"),
    REGISTRATION("web/WEB-INF/jsp/registration.jsp"),
    LOGIN("web/WEB-INF/jsp/login.jsp");

    private final String value;

    PageEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
