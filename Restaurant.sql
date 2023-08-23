CREATE TABLE Dishes(
   did INTEGER PRIMARY KEY AUTO_INCREMENT,
   dname VARCHAR(50),
   description VARCHAR(500),
   price DOUBLE
   );

CREATE TABLE Category(
   cat_id int primary key auto_increment,
   did int,
   cat_type ENUM('Appetizers', 'Drinks', 'Entree', 'Dessert'),
   foreign key(did) references Dishes(did)
);

CREATE TABLE Employees(
   eid INTEGER PRIMARY KEY AUTO_INCREMENT,
   ename VARCHAR(100),
   earned DOUBLE(3,2),
   role VARCHAR(50)
   );


CREATE TABLE Tables(
    tid INTEGER PRIMARY KEY AUTO_INCREMENT,
    seats INTEGER,
    charge DOUBLE(3,2),
    State ENUM ('Waiting', 'Vacant', 'Served') 
);

CREATE TABLE Ledger(
	lid INTEGER PRIMARY KEY AUTO_INCREMENT,
	ldate date,
	note VARCHAR(200),
	balance DOUBLE);
    
ALTER TABLE Ledger
MODIFY balance Double;
CREATE TABLE Orders(
    oid INTEGER PRIMARY KEY AUTO_INCREMENT,
	tid INTEGER,
	did INTEGER,
    FOREIGN KEY (tid) REFERENCES Tables(tid),
    FOREIGN KEY (did) REFERENCES Dishes(did)
);

DROP TABLE Dishes;
DROP TABLE Employees;
DROP TABLE Tables;
DROP TABLE Ledger;
DROP TABLE Orders;

insert into Dishes (did, dname, description, price)
values
(1, 'Chicken Alfredo Pasta', 'Penne pasta in alfredo sauce with seasoned chicken breasts', 10.99),
(2, 'Margarita Pizza', '12in pizza featuring a bubbly crust, crushed San Marzano tomato sauce, fresh mozzarella and basil, a drizzle of olive oil, and a sprinkle of salt', 12.99),
(3, 'Pasta Carbonara', 'A pasta dish originating from Rome, made with spaghetti, eggs, grated Pecorino Romano cheese, guanciale (cured pork jowl), and black pepper', 15.49),
(4, 'Lasagna', 'A layered dish consisting of wide flat pasta noodles, ricotta cheese, mozzarella, Bolognese sauce (a meat-based sauce), and often other ingredients like spinach', 14.95),
(5, 'Risotto', 'Creamy rice dish cooked with a flavorful broth, usually enhanced with ingredients like saffron, mushrooms, seafood, or vegetables', 18.75),
(6, 'Spaghetti Bolognese', 'Spaghetti served with a rich and hearty Bolognese sauce made from minced meat, tomatoes, onions, carrots, celery, and red wine', 13.50),
(7, 'Ossobuco', 'A Milanese specialty made from braised veal shanks, typically cooked with white wine, broth, onions, carrots, celery, and gremolata (a mixture of lemon zest, garlic, and parsley)', 27.99),
(8, 'Bruschetta', 'Grilled bread rubbed with garlic and topped with fresh diced tomatoes, basil, olive oil, and sometimes mozzarella or other ingredients', 7.25),
(9, 'Tiramisu', 'A popular dessert made with layers of coffee-soaked ladyfingers (sponge biscuits), mascarpone cheese, cocoa powder, and sometimes a touch of liqueur', 8.99),
(10, 'Caprese Salad', 'A simple salad composed of fresh tomatoes, mozzarella cheese, basil leaves, olive oil, and a drizzle of balsamic vinegar', 9.50),
(11, 'Gnocchi', 'Soft dumplings made from potatoes, flour, and sometimes eggs, served with various sauces like tomato, pesto, or Gorgonzola cream', 16.50);

SELECT * FROM Dishes;

insert into Employees (eid, ename, earned, role)
values
(1, 'John Smith', 0.00, 'waiter'),
(2, 'Jane Smith', 0.00, 'waiter'),
(3, 'Bob Lee', 0.00, 'waiter'),
(4, 'Jill Wayne', 0.00, 'waiter'),
(5, 'Anna Fry', 0.00, 'waiter');

SELECT * FROM Employees;

insert into Ledger(lid, ldate, note, balance)
values
(1, '2023-08-23', 'starting amount', 3000.00);

SELECT * FROM Ledger;

insert into Tables(tid, seats, charge, State)
values
(1, 4, 0.00, 'VACANT'),
(2, 4, 0.00, 'VACANT'),
(3, 5, 0.00, 'VACANT'),
(4, 5, 0.00, 'VACANT'),
(5, 10, 0.00, 'VACANT');

SELECT * FROM Tables;

insert into Orders(tid, did)
values
(1,1),
(1,2),
(1,3),
(1,4),
(1,5);

SELECT * FROM Orders;