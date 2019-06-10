DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE seq_users RESTART WITH 100000;
ALTER SEQUENCE seq_meals RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
                                                 ('User', 'user@yandex.ru', 'password'),
                                                 ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO meals (date_time, description, calories, user_id) VALUES
                                                                     ('2015-05-30 10:00:00', 'Breakfast', 500, 100000),
                                                                     ('2015-05-31 14:00:00', 'Lunch', 1000, 100000),
                                                                     ('2015-06-01 14:00:00', 'Admin Supper', 510, 100001);

INSERT INTO user_roles (role, user_id) VALUES
                                              ('ROLE_USER', 100000),
                                              ('ROLE_ADMIN', 100001);