package ru.javawebinar.topjava.service;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(USER_FIRST_MEAL_ID, USER_ID), FIRST_USER_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal meal = new Meal(FIRST_USER_MEAL);
        meal.setCalories(1000);
        service.update(meal, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound(){
        service.delete(1, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_FIRST_MEAL_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), SECOND_USER_MEAL);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> resultList = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 28), LocalDate.of(2015, Month.MAY, 30), USER_ID);
        assertMatch(resultList, FIRST_USER_MEAL);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), SECOND_USER_MEAL, FIRST_USER_MEAL);
    }

    @Test
    public void update() {
        Meal updatedMeal = service.get(USER_FIRST_MEAL_ID, USER_ID);
        updatedMeal.setCalories(1200);
        service.update(updatedMeal, USER_ID);
        assertMatch(service.getAll(USER_ID), updatedMeal, SECOND_USER_MEAL);
    }

    @Test
    public void create() {
        Meal newUserMeal = new Meal(LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Lunch", 1000);
        Meal newCreatedUserMeal = service.create(newUserMeal, USER_ID);
        newUserMeal.setId(newCreatedUserMeal.getId());
        assertMatch(service.getAll(USER_ID), newUserMeal, SECOND_USER_MEAL, FIRST_USER_MEAL);
    }
}