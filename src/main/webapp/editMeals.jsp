<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
    <h1>Edit Meal Form</h1>
    <form method="POST" action='meals' name="meal">
        <input type="hidden" name="id" value="${meal.id}" required />
        Date/Time : <input type="datetime-local" name="dateTime"
                         value="${meal.dateTime}" required /> <br />
        Description : <input
            type="text" name="description"
            value="${meal.description}" required /> <br />
        Calories : <input
            type="text" name="calories"
            value="${meal.calories}" required /> <br />
        <input type="submit" value="Save Changes" required />
    </form>
</body>
</html>
