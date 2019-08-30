package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request){
        int userId = SecurityUtil.authUserId();
        service.delete(getId(request), userId);
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String updateMeal(HttpServletRequest request, Model model){
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meal", service.get(getId(request), userId));
        return "mealForm";
    }

    @GetMapping("/create")
    public String createMeal(Model model){
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping("/save")
    public String saveMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            checkNew(meal);
            service.create(meal, userId);
        } else {
            assureIdConsistent(meal, getId(request));
            service.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @PostMapping
    public String filterMeal(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
