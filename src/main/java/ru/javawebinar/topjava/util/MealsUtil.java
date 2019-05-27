package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {

    public static int limitCalories = 2000;

    public static Collection<MealTo> convertDTO(Collection<Meal> meals, int limitCalories){
        return getWithExcess(meals, meal -> true, limitCalories);
    }

    private static Collection<MealTo> getWithExcess(Collection<Meal> meals, Predicate<Meal> predicate, int limitCalories) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(predicate)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > limitCalories))
                .collect(Collectors.toList());
    }

    public static Collection<MealTo> getFilteredWithExcess(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int limitCalories) {
        return getWithExcess(meals, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime), limitCalories);
    }

    private static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}