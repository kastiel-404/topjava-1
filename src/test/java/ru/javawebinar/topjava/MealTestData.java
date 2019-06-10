package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_FIRST_MEAL_ID = START_SEQ;
    public static final int USER_SECOND_MEAL_ID = START_SEQ + 1;
    public static final int ADMIN_MEAL_ID = START_SEQ + 2;

    public static final Meal FIRST_USER_MEAL = new Meal(USER_FIRST_MEAL_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Breakfast", 500);
    public static final Meal SECOND_USER_MEAL = new Meal(USER_SECOND_MEAL_ID, LocalDateTime.of(2015, Month.MAY, 31, 14, 0), "Lunch", 1000);
    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Admin Supper", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertThat(actual).usingFieldByFieldElementComparator().contains(expected);
    }

    public static void assertMatch(int actualSize, int expectedSize){
        assertThat(actualSize).isEqualTo(expectedSize);
    }
}
