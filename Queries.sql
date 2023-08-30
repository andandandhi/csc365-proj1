-- queries Eric is working on currently
-- Query for Employee
-- getEarned
select Earned
from Employees;
-- check

-- setEarned
Update Employees
Set earned=25
Where eid=1;
-- check

-- Table
-- setServer
Update Tables
Set tstate = 'Ordering',
	eid = 1
Where tid = 1;
-- check

-- addOrder
insert into Orders (tid, did)
values
	(2,6),
	(2,8),
	(2,9);
-- check

Update Tables
Set tstate = 'Waiting'
Where tid = 2;
-- checked

-- serveAllOrders
Delimiter //
Create Procedure serveAllOrder(IN tid INT, In eid INT)
Begin
	DECLARE total_amount DECIMAL(10, 2);
    
	SELECT SUM(Dishes.price) INTO total_amount
	FROM Orders
	JOIN Dishes ON Orders.did = Dishes.did
	WHERE Orders.tid = tid;
	
    Update Tables
	Set tstate = 'Served',
		eid = server_id
	Where tid=table_id;
	
    DELETE FROM Orders
	WHERE tid = table_id;

End;
//
Delimiter ; 


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
WHERE oid = (
	Select oid
    From Orders
    Where tid = table_id
    Limit 1
);

UPDATE Tables
SET tstate = 'Vacant', sid = NULL
WHERE tid = table_id;

-- clearAllOrder
Drop from Orders

-- vacateTable
update Tables
Set total = total + tip
	tstate = 'VACANT'
WHERE tid = table_id

Update Employees
Set earned = earned + (Select total * tip_percentage FROM Tables WHERE tid = table_id),
WHERE eid = employee_id;
	
-- payEmployee
Update Employees
Set earned = earned - payment_amount
Where eid = employee_id;

Insert Into Ledger (lid, date, note, balance)
Values

-- getLedgerEntries
Select * from Ledger; 


-- queries Tenzin is working on currently

-- Retrieving Dishes Queries:
-- Retrieve All Dishes:
SELECT * FROM Dishes;

-- Retrieve Dishes by Category:
SELECT * FROM Dishes WHERE category = 'APPETIZER';

-- Retrieve Dishes by Price Range:
SELECT * FROM Dishes WHERE price BETWEEN 10.00 AND 20.00;

-- Retrieve Dish Details by Name:
SELECT * FROM Dishes WHERE dname = 'Chicken Alfredo Pasta';

-- Retrieve Dish Names Only:
SELECT dname FROM Dishes;

/* -- Add New Dish
INSERT INTO Dishes(dname, description,category, price) 
VALUES (' ',' ' ,' ' , );
*/

/* -- Update Dishes
UPDATE Dishes
set  
where did = 
*/

-- Retrieve All Employees:
SELECT * FROM Employees;

-- Retrieve Employees by Role:
SELECT * FROM Employees WHERE role = 'WAITER';

-- Retrieve Employees by Earnings Range 
SELECT * FROM Employees WHERE earned > 1000.00;

-- Retrieve Employee Details by Name
SELECT * FROM Employees WHERE ename = 'John Smith';

/*-- Update Employees
UPDATE Employees
set  
where 
*/
-- Add New Employee

-- AssignServer:
UPDATE Tables
SET eid = 1,
    tstate = 'ORDERING'
WHERE tid = 1;


-- AddOrders:
INSERT INTO Orders (tid, did)
VALUES
(1, 1),  -- Table 1 ordered Dish 1
(1, 2),  -- Table 1 ordered Dish 2
(1, 3);  -- Table 1 ordered Dish 3

UPDATE Tables
SET tstate = 'WAITING'
WHERE tid = 1;

-- serveAllOrders
-- updates the tuple in the Tables table and clear orders
UPDATE Tables
SET total = (
    SELECT SUM(D.price) 
    FROM Orders AS O
    INNER JOIN Dishes AS D ON O.did = D.did
    WHERE O.tid = 1), 
    tstate = 'SERVED'
    WHERE tid = 1;

-- Clears all orders associated with the table
DELETE FROM Orders
WHERE tid = 1;



