package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


class CardDeliveryTest {

    private String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldHappyPath() {
        String planningDate = generateDate(3, "dd.MM.yyyy");
        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Великий Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldHappyPathWhenOpenCityPopup() {
        String planningDate = generateDate(3, "dd.MM.yyyy");
        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Ор");
        $$(".popup_visible .menu_mode_radio-check .menu-item__control").find(exactText("Оренбург")).click();
        form.$("[data-test-id=date] input").sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldHappyPathWhenOpenCalendarPopupV1() {
        String planningDate = generateDate(30, "dd.MM.yyyy");
        String planningDateForSearch = generateDate(30, "d");
        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Ор");
        $$(".popup_visible .menu_mode_radio-check .menu-item__control").find(exactText("Оренбург")).click();
        form.$(".input__icon .icon_name_calendar").click();
        SelenideElement dayElement = $$("[data-day]").find(exactText(planningDateForSearch));
        if (!dayElement.exists()) {
            SelenideElement button = $(".calendar-input__calendar-wrapper .calendar__arrow_direction_right[data-step='1']");
            button.click();

            dayElement = $$("[data-day]").find(exactText(planningDateForSearch));
            dayElement.shouldBe(visible).click();
        } else {
            dayElement.shouldBe(visible).click();
        }
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Универсальный тест")
    public void shouldHappyPathWhenOpenCalendarPopupV2() {
        int days = 366; //здесь выбираем количество дней, через которое необходимо запланировать дату встречи
        String planningDate = generateDate(days, "dd.MM.yyyy");
        String planningDateForSearch = generateDate(days, "d");
        SelenideElement form = $(".form_theme_alfa-on-white");
        form.$("[data-test-id=city] input").setValue("Ор");
        $$(".popup_visible .menu_mode_radio-check .menu-item__control").find(exactText("Оренбург")).click();
        form.$(".input__icon .icon_name_calendar").click();

        LocalDate currentDate = LocalDate.now();
        LocalDate plannedDate = currentDate.plusDays(days);

        long daysDifference = ChronoUnit.DAYS.between(currentDate, plannedDate);

        if (daysDifference > 0) {
            long monthsDifference = ChronoUnit.MONTHS.between(currentDate, plannedDate);
            if (plannedDate.getDayOfMonth() < currentDate.getDayOfMonth()) {
                monthsDifference++;
            }
            for (int i = 0; i < monthsDifference; i++) {
                $(".calendar-input__calendar-wrapper .calendar__arrow_direction_right[data-step='1']").shouldBe(visible).click();
            }
        }
        $$("[data-day]").find(exactText(planningDateForSearch)).shouldBe(visible).click();
        form.$("[data-test-id=name] input").setValue("Анна-Мария Иванова");
        form.$("[data-test-id=phone] input").setValue("+79990000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button_theme_alfa-on-white").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}