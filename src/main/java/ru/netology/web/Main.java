package ru.netology.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }
}