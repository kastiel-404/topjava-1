package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryImpl implements MealRepository{

    private static final MealRepositoryImpl instanse =  new MealRepositoryImpl();

    private static AtomicInteger counterId = new AtomicInteger(6);

    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private MealRepositoryImpl(){
        meals.put(1, new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Breakfast", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Lunch", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Supper", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Breakfast", 1000));
        meals.put(5, new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Lunch", 500));
        meals.put(6, new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Supper", 510));
    }
    public static MealRepositoryImpl getInstance(){
       return instanse;
    }

    @Override
    public void addMeal(Meal meal) {
        int currentId = counterId.incrementAndGet();
        meal.setId(currentId);
        meals.put(currentId, meal);
    }

    @Override
    public void removeMeal(int idMeal) {
        meals.remove(idMeal);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.replace(meal.getId(), meal);
    }

    @Override
    public Collection<Meal> getAllMeals() {
        return meals.values();
    }

    @Override
    public Meal getMealById(int idMeal) {
        return meals.get(idMeal);
    }
}
