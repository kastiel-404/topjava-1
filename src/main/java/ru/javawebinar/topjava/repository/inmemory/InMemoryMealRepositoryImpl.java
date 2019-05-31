package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }
        return repository.computeIfAbsent(userId, k -> new HashMap<>()).put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.get(userId).keySet().remove(id);
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.getOrDefault(userId, new HashMap<>()).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId)
    {
        return repository.getOrDefault(userId, new HashMap<>()).values()
                .stream().sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MealTo> getAllFilteredByDateAndTime(LocalDate fromDate, LocalDate toDate,
                                                          LocalTime fromTime, LocalTime toTime, int userId, int caloriesPerDay) {
        fromDate = Optional.ofNullable(fromDate).orElse(LocalDate.MIN);
        toDate = Optional.ofNullable(toDate).orElse(LocalDate.MAX);
        fromTime = Optional.ofNullable(fromTime).orElse(LocalTime.MIN);
        toTime = Optional.ofNullable(toTime).orElse(LocalTime.MAX);
        return MealsUtil.getFilteredWithExcess(getAll(userId), caloriesPerDay, fromDate, toDate, fromTime, toTime);
    }
}
