package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {

    private static final Logger log = LoggerFactory.getLogger(MealsUtil.class);

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Завтрак", 500 ),
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 13, 0), "Обед", 1000 ),
            new Meal(LocalDateTime.of(2019, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 10, 0), "Завтрак", 1000 ),
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 13, 0), "Обед", 500 ),
            new Meal(LocalDateTime.of(2019, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> getWithExcess(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> true, meal -> true);
    }

    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, LocalDate fromDate, LocalDate toDate,
                                                     LocalTime fromTime, LocalTime toTime) {
        return getFilteredWithExcess(meals, caloriesPerDay,
                meal -> DateTimeUtil.isBetweenDate(meal.getDate(), fromDate, toDate),
                meal -> DateTimeUtil.isBetweenTime(meal.getTime(), fromTime, toTime));
    }

    private static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filterDate, Predicate<Meal> filterTime) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );


        return meals.stream()
                .filter(filterDate)
                .filter(filterTime)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}