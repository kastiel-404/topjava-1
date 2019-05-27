package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MealServlet extends HttpServlet {

    private MealRepository repository;
    private static String ADD_OR_EDIT = "editMeals.jsp";
    private static String MEAL_LIST = "meals.jsp";

    public MealServlet() {
        super();
        repository = MealRepositoryImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = Optional.ofNullable(req.getParameter("action")).orElse("");
        String forward = MEAL_LIST;
        int mealId;
        switch (action) {
            case "delete":
                mealId = Integer.parseInt(req.getParameter("mealId"));
                repository.removeMeal(mealId);
                setAttribute(req,"meals", MealsUtil.convertDTO(repository.getAllMeals(), MealsUtil.limitCalories));
                break;
            case "edit":
                forward = ADD_OR_EDIT;
                mealId = Integer.parseInt(req.getParameter("mealId"));
                setAttribute(req,"meal", repository.getMealById(mealId));
                break;
            case "add":
                forward = ADD_OR_EDIT;
                break;
            default:
                setAttribute(req,"meals", MealsUtil.convertDTO(repository.getAllMeals(), MealsUtil.limitCalories));
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if(id.isEmpty()){
            repository.addMeal(new Meal(dateTime, description, calories));
        }else{
            Meal meal = new Meal(dateTime, description, calories);
            meal.setId(Integer.parseInt(id));
            repository.updateMeal(meal);
        }
        RequestDispatcher view = req.getRequestDispatcher(MEAL_LIST);
        setAttribute(req,"meals", MealsUtil.convertDTO(repository.getAllMeals(), MealsUtil.limitCalories));
        view.forward(req, resp);
    }

    private void setAttribute(HttpServletRequest req, String objName, Object obj){
        req.setAttribute(objName, obj);
    }
}
