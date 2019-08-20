package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(mealRepository.get(id, userId), id);
    }

    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime  must not be null");
        return mealRepository.getBetween(startDateTime, endDateTime, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.getAll(userId);
    }

    @Override
    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(mealRepository.save(meal, userId), meal.getId());
    }

    @Override
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return mealRepository.save(meal, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return checkNotFoundWithId(mealRepository.getWithUser(id, userId), id);
    }
}