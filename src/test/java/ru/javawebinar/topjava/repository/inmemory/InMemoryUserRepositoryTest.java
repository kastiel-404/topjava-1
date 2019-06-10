package ru.javawebinar.topjava.repository.inmemory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;

@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
public class InMemoryUserRepositoryTest {

    @Autowired
    private InMemoryUserRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        repository.init();
    }

    @Test
    public void delete() throws Exception {
        repository.delete(USER_ID);
        Collection<User> users = repository.getAll();
        assertMatch(users.size(), 1);
        assertMatch(users, ADMIN);
    }

    @Test
    public void get() throws Exception {
        User user = repository.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test
    public void getByEmail() throws Exception {
        User user = repository.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    public void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        repository.save(updated);
        assertMatch(repository.get(USER_ID), updated);
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = repository.getAll();
        assertMatch(all, ADMIN, USER);
    }
}