package ru.yandex.praktikum.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserRandomData {

    public String randomUserEmail(){
        return (RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(5) + ".com")
                .toLowerCase();
    }

    public String randomUserPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public String randomUserName() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
