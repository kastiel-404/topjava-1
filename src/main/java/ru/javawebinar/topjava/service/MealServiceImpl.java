package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }
    @Override
    public Meal create(Meal meal, int userId) throws NotFoundException{
        checkNew(meal);
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id,userId),id);
    }

    @Override
    public void update(Meal meal, int userId) {
        assureIdConsistent(repository.save(meal, userId), meal.getId());
    }

    @Override
    public Collection<MealTo> getAllFilteredByDateAndTime(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int userId, int caloriesPerDay) {
        return repository.getAllFilteredByDateAndTime(fromDate, toDate, fromTime, toTime, userId, caloriesPerDay);
    }

    @Override
    public Collection<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getWithExcess(repository.getAll(userId), caloriesPerDay);
    }
}