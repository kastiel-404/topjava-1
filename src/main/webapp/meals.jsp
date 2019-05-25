<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users</title>
    <style>
        .bgRed{
           background-color: red;
        }
    </style>
</head>
<body>
<table>
    <thead>
        <tr>
            <th>Date/Time</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${meals}" var="meal">
            <tr class="${meal.excess ? 'bgRed' : ''}">
                <td>${f:formatLocalDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>