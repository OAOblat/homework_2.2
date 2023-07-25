package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class CardDeliveryTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldHappyPath() {
        // Получаем текущую дату
        LocalDate currentDate = LocalDate.now();
        // Добавляем 3 дня к текущей дате
        LocalDate futureDate = currentDate.plusDays(3);
        // Форматируем дату для записи в тестовые данные
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String futureDateStr = futureDate.format(formatter);

        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Великий Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(futureDateStr);
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        SelenideElement notification = $("[data-test-id=notification]");
        notification.$(withText("Встреча успешно забронирована на")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldHappyPathWhenOpenCityPopup() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String futureDateStr = futureDate.format(formatter);
        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Ор");
        $$(".popup_visible .menu_mode_radio-check .menu-item__control").find(exactText("Оренбург")).click();
        form.$("[data-test-id=date] input").sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(futureDateStr);
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        SelenideElement notification = $("[data-test-id=notification]");
        notification.$(withText("Встреча успешно забронирована на")).shouldBe(visible, Duration.ofSeconds(15));
    }

}