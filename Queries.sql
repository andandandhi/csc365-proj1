
-- queries Eric is working on currently
-- Query for Employee
-- getEarned
select Earned
from Employees;
--check

-- setEarned
Update Employees
Set earned=new_earned_amount
Where eid=employee_id;
--check

-- Table
-- setServer
Update Tables
Set State = 'Ordering',
	sid = server_id
Where tid = table_id;


-- addOrder
insert into Orders (tid, did)
values
	(2,6),
	(2,8),
	(2,9);

Update Tables
Set State = 'Waiting'
Where tid = 2;

-- serveAllOrders
DECLARE total_amount DECIMAL(10, 2);
SELECT SUM(Dishes.price) INTO total_amount
FROM Orders
JOIN Dishes ON Orders.did = Dishes.did
WHERE Orders.tid = table_id;

DELETE FROM Orders
WHERE tid = table_id;

Update Tables
Set State = 'Served'
	sid = server_id
Where tid=table_id

-- vacate method
SELECT SUM(Dishes.price) INTO @total_amount
FROM Orders
JOIN Dishes ON Orders.did = Dishes.did
WHERE Orders.tid = table_id;

SET @tip_percentage = 0.15; -- Example: 15% tip
SET @tip_amount = @total_amount * @tip_percentage;

SET @grand_total = @total_amount + @tip_amount;

UPDATE Ledger
SET total = @grand_total
WHERE tid = table_id;

UPDATE Tables
SET State = 'Vacant', sid = NULL
WHERE tid = table_id;

--clearAllOrder
Drop from Orders

--vacateTable
update Tables
Set total = total + tip
	State = 'VACANT'
WHERE tid = table_id

Update Employees
Set earned = earned + (Select total * tip_percentage FROM Tables WHERE tid = table_id),
WHERE eid = employee_id;
	
--payEmployee
Update Employees
Set earned = earned - payment-amount
Where eid = employee_id;

Insert Into Ledger (lid, date, note, balance)
Values

--getLedgerEntries
Select * from Ledger


-- queries Tenzin is working on currently

/*Retrieving Dishes Queries:


Retrieve All Dishes:
SELECT * FROM Dishes;


Retrieve Dishes by Category:
SELECT * FROM Dishes WHERE did IN (
            SELECT did FROM Category WHERE cat_type = 'Appetizers'
            );


Retrieve Dishes by Price Range:
            SELECT * FROM Dishes WHERE price BETWEEN 10.00 AND 20.00;


Retrieve Dish Details by Name:
SELECT * FROM Dishes WHERE dname = 'Chicken Alfredo Pasta';


Retrieve Dish Names Only:
SELECT dname FROM Dishes;


Retrieving Ledger Queries:
Retrieve All Ledger Entries:
SELECT * FROM Ledger;


Retrieve Ledger Entries by Date Range:
SELECT * FROM Ledger WHERE ldate BETWEEN '2023-08-01' AND '2023-08-15';


Retrieve Ledger Entries by Note Keyword:
SELECT * FROM Ledger WHERE note LIKE '%starting%';


Retrieve Ledger Balance for a Specific Date:
SELECT balance FROM Ledger WHERE ldate <= '2023-08-10' ORDER BY ldate DESC LIMIT 1;


Retrieve the Latest Ledger Entry:
SELECT * FROM Ledger ORDER BY ldate DESC LIMIT 1;

*/


