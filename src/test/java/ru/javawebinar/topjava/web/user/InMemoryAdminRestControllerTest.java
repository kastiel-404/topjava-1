package ru.javawebinar.topjava.web.user;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

public class InMemoryAdminRestControllerTest {
    private static ConfigurableApplicationContext appCtx;
    private static AdminRestController controller;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller = appCtx.getBean(AdminRestController.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() throws Exception {
        // re-initialize

        InMemoryUserRepositoryImpl repository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
        repository.init();
    }

    @Test
    public void delete() throws Exception {
        controller.delete(USER_ID);
        Collection<User> users = controller.getAll();
        assertMatch(users.size(), 1);
        assertMatch(users, ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        controller.delete(10);
    }

    @Test
    public void get() throws Exception {
        User user = controller.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        controller.get(1);
    }

    @Test
    public void getByEmail() throws Exception {
        User user = controller.getByMail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    public void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        controller.update(updated, USER_ID);
        assertMatch(controller.get(USER_ID), updated);
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = controller.getAll();
        assertMatch(all, ADMIN, USER);
    }
}