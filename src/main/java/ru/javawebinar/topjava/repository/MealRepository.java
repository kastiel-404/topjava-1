package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    void addMeal(Meal meal);
    void removeMeal(int idMeal);
    void updateMeal(Meal meal);
    Collection<Meal> getAllMeals();
    Meal getMealById(int idMeal);
}
