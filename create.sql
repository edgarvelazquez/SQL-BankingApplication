-- Student ID: 013524186
-- Velazquez, Edgar
-- In order to run this sql script with the ^ as your line delimiter, use the follow command options
-- db2 -td"^" -f create.clp
connect to cs157A^
DROP table P1.Customer^
DROP table P1.Account^
DROP view total_balance^
CREATE TABLE P1.Customer(ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 100, INCREMENT BY 1, NO CACHE) PRIMARY KEY, Name varchar(15) NOT NULL, Gender char(1) NOT NULL, Age INT NOT NULL check (age>=0), Pin INT NOT NULL check (pin>=10))^
CREATE TABLE P1.Account(NUMBER INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1, NO CACHE) PRIMARY KEY, ID int NOT NULL, Balance int NOT NULL check (balance>=0), Type char(1) check (Type='C' OR Type='S') NOT NULL, Status char(1) NOT NULL, Foreign Key customer_id (ID) References p1.customer(ID) on delete no action)^
CREATE VIEW TOTAL_BALANCE AS SELECT DISTINCT P1.CUSTOMER.ID, P1.CUSTOMER.NAME, P1.CUSTOMER.AGE, P1.CUSTOMER.GENDER, (SELECT COALESCE(SUM(BALANCE),0) AS TOTAL FROM P1.ACCOUNT WHERE (ID = P1.CUSTOMER.ID AND STATUS='A') ) AS BALANCE FROM P1.CUSTOMER LEFT JOIN P1.ACCOUNT ON P1.CUSTOMER.ID = P1.ACCOUNT.ID GROUP BY P1.CUSTOMER.ID, P1.CUSTOMER.NAME, P1.CUSTOMER.AGE, P1.CUSTOMER.GENDER^
terminate^
