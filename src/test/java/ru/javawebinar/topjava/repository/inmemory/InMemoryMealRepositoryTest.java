package ru.javawebinar.topjava.repository.inmemory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
public class InMemoryMealRepositoryTest {

    @Autowired
    private InMemoryMealRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        repository.init();
    }

    @Test
    public void get() {
        assertMatch(repository.get(USER_FIRST_MEAL_ID, USER_ID), FIRST_USER_MEAL);
    }

    @Test
    public void delete() {
        repository.delete(USER_FIRST_MEAL_ID, USER_ID);
        assertMatch(repository.getAll(USER_ID), SECOND_USER_MEAL);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> resultList = repository.getBetween(LocalDateTime.of(2015, Month.MAY, 28, 10,0), LocalDateTime.of(2015, Month.MAY, 30,16,0), USER_ID);
        assertMatch(resultList, FIRST_USER_MEAL);
    }

    @Test
    public void getAll() {
        assertMatch(repository.getAll(USER_ID), SECOND_USER_MEAL, FIRST_USER_MEAL);
    }

    @Test
    public void update() {
        Meal updatedMeal = repository.get(USER_FIRST_MEAL_ID, USER_ID);
        updatedMeal.setCalories(1200);
        repository.save(updatedMeal, USER_ID);
        assertMatch(repository.getAll(USER_ID), updatedMeal, SECOND_USER_MEAL);
    }

    @Test
    public void create() {
        Meal newUserMeal = new Meal(LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Lunch", 1000);
        Meal newCreatedUserMeal = repository.save(newUserMeal, USER_ID);
        newUserMeal.setId(newCreatedUserMeal.getId());
        assertMatch(repository.getAll(USER_ID), newUserMeal, SECOND_USER_MEAL, FIRST_USER_MEAL);
    }
}