package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int limitCaloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayCount = new HashMap<>();
        List<UserMealWithExceed> filteredMealsWithExceedByTime = new ArrayList<>();
        mealList.forEach(u -> caloriesPerDayCount.merge(u.getDate(),
                u.getCalories(), (oldAmount, newAmount) -> oldAmount + newAmount));
        mealList.forEach(u -> {
            if (TimeUtil.isBetween(u.getTime(), startTime, endTime)) {
                filteredMealsWithExceedByTime.add(createUserMealWithExceed(u, caloriesPerDayCount.get(u.getDate()) > limitCaloriesPerDay));
            }
        });
        return filteredMealsWithExceedByTime;
    }

    private static UserMealWithExceed createUserMealWithExceed(UserMeal userMeal, boolean isExceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                userMeal.getCalories(), isExceed);
    }
}
