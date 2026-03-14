create database Bank_Project;
use Bank_Project;

create table BankInfo(
    Acct_No        varchar(12) primary key,
    Cust_name      varchar(20),
    Cust_phone     varchar(10),
    Cust_Address   varchar(20),
    Acct_type      varchar(10),
    Acct_balance   decimal(10,2),
    Acct_PIN       int null
);
CREATE TABLE Transactions(
    Trans_ID INT AUTO_INCREMENT PRIMARY KEY ,
    Acct_hold_name VARCHAR(50) not null,
    Acct_No VARCHAR(12) not null,
    Trans_method VARCHAR(20),
    Trans_date DATE,
    Trans_time TIME,
    Trans_type VARCHAR(20),
    Amount DECIMAL(10,2),
    
    FOREIGN KEY (Acct_No)
    REFERENCES BankInfo(Acct_No)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);
