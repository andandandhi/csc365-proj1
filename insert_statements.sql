USE restaurant;

insert into Dishes (did, dname, category, description, price)
values
(1, 'Bruschetta', 'APPETIZER', 'Grilled bread rubbed with garlic and topped with fresh diced tomatoes, basil, olive oil, and sometimes mozzarella or other ingredients', 7.25),
(2, 'Caprese Salad', 'APPETIZER', 'A simple salad composed of fresh tomatoes, mozzarella cheese, basil leaves, olive oil, and a drizzle of balsamic vinegar', 9.50),
(3, 'Gnocchi', 'APPETIZER', 'Soft dumplings made from potatoes, flour, and sometimes eggs, served with various sauces like tomato, pesto, or Gorgonzola cream', 16.50),
(4, 'Chicken Alfredo Pasta','ENTREE', 'Penne pasta in alfredo sauce with seasoned chicken breasts', 10.99),
(5, 'Margarita Pizza', 'ENTREE', '12in pizza featuring a bubbly crust, crushed San Marzano tomato sauce, fresh mozzarella and basil, a drizzle of olive oil, and a sprinkle of salt', 12.99),
(6, 'Pasta Carbonara','ENTREE', 'A pasta dish originating from Rome, made with spaghetti, eggs, grated Pecorino Romano cheese, guanciale (cured pork jowl), and black pepper', 15.49),
(7, 'Lasagna','ENTREE', 'A layered dish consisting of wide flat pasta noodles, ricotta cheese, mozzarella, Bolognese sauce (a meat-based sauce), and often other ingredients like spinach', 14.95),
(8, 'Risotto','ENTREE', 'Creamy rice dish cooked with a flavorful broth, usually enhanced with ingredients like saffron, mushrooms, seafood, or vegetables', 18.75),
(9, 'Spaghetti Bolognese','ENTREE', 'Spaghetti served with a rich and hearty Bolognese sauce made from minced meat, tomatoes, onions, carrots, celery, and red wine', 13.50),
(10, 'Ossobuco','ENTREE', 'A Milanese specialty made from braised veal shanks, typically cooked with white wine, broth, onions, carrots, celery, and gremolata (a mixture of lemon zest, garlic, and parsley)', 27.99),
(11, 'Tiramisu', 'DESSERT', 'A popular dessert made with layers of coffee-soaked ladyfingers (sponge biscuits), mascarpone cheese, cocoa powder, and sometimes a touch of liqueur', 8.99),
(12,'Soda','DRINK', 'Assorted carbonated soft drinks', 2.50 ),
(13,'Iced Tea','DRINK', 'Chilled brewed tea with ice and optional lemon', 2.75),
(14,'Lemonade','DRINK', 'Freshly squeezed lemon juice, sugar, and water', 3.00),
(15,'Cappuccino','DRINK', 'Espresso mixed with hot milk and topped with steamed milk foam', 4.25),
(16,'Mocha','DRINK', 'Espresso with hot milk, cocoa powder, and whipped cream', 4.50),
(17,'Mineral Water','DRINK', 'Bottled mineral water', 2.00);
SELECT * FROM Dishes;


insert into Employees (eid, ename, earned, role)
values
(1, 'John Smith', 0.00, 'WAITER'),
(2, 'Jane Smith', 0.00, 'WAITER'),
(3, 'Bob Lee', 0.00, 'WAITER'),
(4, 'Jill Wayne', 0.00, 'WAITER'),
(5, 'Anna Fry', 0.00, 'WAITER'),
(6, 'Chef Gordon', 0.00, 'CHEF'),
(7, 'Dishwasher Amy', 0.00, 'DISHWASHER'),
(8, 'Owner Mark', 0.00, 'OWNER');

SELECT * FROM Employees;

insert into Ledger(lid, ldate, note, balance)
values
(1, '2023-08-23', 'starting amount', 3000.00);
SELECT * FROM Ledger;


insert into Tables(tid, seats, total, tstate)
values
(1, 4, 0.00, 'VACANT'),
(2, 4, 0.00, 'VACANT'),
(3, 5, 0.00, 'VACANT'),
(4, 5, 0.00, 'VACANT'),
(5,10, 0.00, 'VACANT');
SELECT * FROM Tables;

