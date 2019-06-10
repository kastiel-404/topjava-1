package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl.ADMIN_ID;
import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl.USER_ID;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    // Map  userId -> (mealId-> meal)
    private Map<Integer, InMemoryBaseRepositoryImpl<Meal>> usersMealsMap = new ConcurrentHashMap<>();


    @Override
    public Meal save(Meal meal, int userId) {
        InMemoryBaseRepositoryImpl<Meal> meals = usersMealsMap.computeIfAbsent(userId, uid -> new InMemoryBaseRepositoryImpl<>());
        return meals.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        InMemoryBaseRepositoryImpl<Meal> meals = usersMealsMap.get(userId);
        return meals != null && meals.delete(id);
    }

    @Override
    public Meal get(int id, int userId) {
        InMemoryBaseRepositoryImpl<Meal> meals = usersMealsMap.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return getAllFiltered(userId, meal -> Util.isBetween(meal.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        InMemoryBaseRepositoryImpl<Meal> meals = usersMealsMap.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.getCollection().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    public void init() {
        usersMealsMap.clear();
        InMemoryBaseRepositoryImpl.setCounter(99999);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Breakfast", 500), USER_ID);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 14, 0), "Lunch", 1000), USER_ID);
        save(new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Admin Supper", 510), ADMIN_ID);
    }
}