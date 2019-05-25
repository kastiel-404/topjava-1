package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {

    private static int limitCalories = 2000;

    private static List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Breakfast", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Lunch", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Supper", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Breakfast", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Lunch", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Supper", 510)
    );

    public static List<MealTo> convertDTO(){
        return getWithExcess(meals, meal -> true);
    }

    private static List<MealTo> getWithExcess(List<Meal> meals, Predicate<Meal> predicate) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(predicate)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > limitCalories))
                .collect(Collectors.toList());
    }

    public static List<MealTo> getFilteredWithExcess(List<Meal> meals, LocalTime startTime, LocalTime endTime) {
        return getWithExcess(meals, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}