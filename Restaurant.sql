CREATE TABLE Dishes(
   did INTEGER PRIMARY KEY AUTO_INCREMENT,
   dname VARCHAR(64),
   description VARCHAR(1024),
   category VARCHAR(16),
   price DOUBLE
   );

CREATE TABLE Employees(
   eid INTEGER PRIMARY KEY AUTO_INCREMENT,
   ename VARCHAR(64),
   earned DOUBLE,
   role VARCHAR(16)
   );

CREATE TABLE Tables(
    tid INTEGER PRIMARY KEY AUTO_INCREMENT,
    eid INTEGER,
    seats INTEGER,
    total DOUBLE,
    tstate VARCHAR(16),
    FOREIGN KEY (eid) REFERENCES Employees(eid)
    
);

CREATE TABLE Ledger(
	lid INTEGER PRIMARY KEY AUTO_INCREMENT,
	ldate date,
	note VARCHAR(512),
	balance DOUBLE
);
   
   CREATE TABLE Orders(
    oid INTEGER PRIMARY KEY AUTO_INCREMENT,
	tid INTEGER,
	did INTEGER,
    FOREIGN KEY (tid) REFERENCES Tables(tid),
    FOREIGN KEY (did) REFERENCES Dishes(did)
);




