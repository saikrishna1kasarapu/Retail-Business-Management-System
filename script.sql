drop table purchases;
drop table employees;
drop table customers;
drop table products;
drop table prod_discnt;
drop table logs;

create table employees
(eid char(3) primary key,
name varchar2(15) not null,
telephone# char(12),
email varchar2(20) unique);

create table customers
(cid char(4) primary key,
first_name varchar2(10),
last_name varchar2(10),
phone# char(12),
visits_made number(4) check (visits_made >= 1),
last_visit_date date);

create table prod_discnt
(discnt_category number(1) primary key check (discnt_category in (1, 2, 3, 4, 5)),
discnt_rate number(3,2));

create table products
(pid char(4) primary key,
name varchar2(15),
qoh number(4),
qoh_threshold number(4),
orig_price number(6,2),
discnt_category number(1) references prod_discnt(discnt_category));

create table purchases
(pur# number(5) primary key,
eid char(3) references employees(eid),
pid char(4) references products(pid),
cid char(4) references customers(cid),
pur_time date,
quantity number(5),
unit_price number(6,2),
payment number(7,2),
saving number(6,2),
unique(eid, pid, cid, pur_time));

create table logs
(log# number(4) primary key,
user_name varchar2(12) not null,
operation varchar2(6) not null,
op_time date not null,
table_name varchar2(20) not null,
tuple_pkey varchar2(6)); 

insert into employees values ('e01', 'David', '666-555-1234', 'david@rb.com');
insert into employees values ('e02', 'Mike', '777-555-2341', 'peter@rb.com');
insert into employees values ('e03', 'Susan', '888-555-3412', 'susan@rb.com');
insert into employees values ('e04', 'Anne', '666-555-4123', 'anne@rb.com');
insert into employees values ('e05', 'Mike', '444-555-4231', 'mike@rb.com');

insert into customers values ('c001', 'Kathy', 'Cook', '666-555-4567', 3, '20-FEB-23');
insert into customers values ('c002', 'John', 'Hall', '888-555-7456', 1, '08-OCT-22');
insert into customers values ('c003', 'Chris', 'Lewis', '666-555-6745', 3, '18-FEB-23');
insert into customers values ('c004', 'Mike', 'Kim', '999-555-5674', 1, '15-JAN-23');
insert into customers values ('c005', 'Kartik', 'Puri', '777-555-4657', 2, '30-AUG-22');
insert into customers values ('c006', 'Connie', 'Chen', '777-555-7654', 2, '08-MAR-23');
insert into customers values ('c007', 'Katie', 'Boyce', '888-555-6574', 1, '12-DEC-22');
insert into customers values ('c008', 'Joe', 'Land', '666-555-5746', 1, '14-NOV-22');

insert into prod_discnt values (1, 0.1);
insert into prod_discnt values (2, 0.15);
insert into prod_discnt values (3, 0.2);
insert into prod_discnt values (4, 0.25);
insert into prod_discnt values (5, 0.3);

insert into products values ('p001', 'stapler', 60, 20, 9.99, 1);
insert into products values ('p002', 'TV', 6, 5, 249, 2);
insert into products values ('p003', 'camera', 15, 3, 148, 3);
insert into products values ('p004', 'pencil', 100, 10, 0.99, 1);
insert into products values ('p005', 'chair', 10, 8, 49.98, 3);
insert into products values ('p006', 'lamp', 10, 6, 19.95, 1);
insert into products values ('p007', 'tablet', 50, 10, 199, 1);
insert into products values ('p008', 'computer', 5, 3, 499, 4);
insert into products values ('p009', 'facemask', 25, 20, 18.50, 1);
insert into products values ('p010', 'powerbank', 20, 5, 30, 1);

insert into purchases values (10001, 'e01', 'p002', 'c001', to_date('12-AUG-2022', 'DD-MON-YYYY'), 1, 211.65, 211.65, 37.35);
insert into purchases values (10002, 'e01', 'p003', 'c001', to_date('20-DEC-2022', 'DD-MON-YYYY'), 1, 118.40, 118.40, 29.60);
insert into purchases values (10003, 'e02', 'p004', 'c002', to_date('08-OCT-2022', 'DD-MON-YYYY'), 5, 0.99, 4.45, 0.5);
insert into purchases values (10004, 'e01', 'p005', 'c003', to_date('23-NOV-2022', 'DD-MON-YYYY'), 2, 39.98, 79.96, 20);
insert into purchases values (10005, 'e04', 'p007', 'c004', to_date('15-JAN-2023', 'DD-MON-YYYY'), 1, 179.10, 179.10, 19.90);
insert into purchases values (10006, 'e03', 'p009', 'c001', to_date('20-FEB-2023', 'DD-MON-YYYY'), 2, 16.65, 33.30, 3.70);
insert into purchases values (10007, 'e03', 'p009', 'c003', to_date('10-JAN-2023', 'DD-MON-YYYY'), 3, 16.65, 49.95, 5.55);
insert into purchases values (10008, 'e03', 'p006', 'c005', to_date('16-AUG-2022', 'DD-MON-YYYY'), 1, 17.96, 17.96, 1.99);
insert into purchases values (10009, 'e03', 'p001', 'c007', to_date('12-DEC-2022', 'DD-MON-YYYY'), 1, 8.99, 8.99, 1.0);
insert into purchases values (10010, 'e04', 'p002', 'c006', to_date('19-OCT-2022', 'DD-MON-YYYY'), 1, 211.65, 211.65, 37.35);
insert into purchases values (10011, 'e02', 'p004', 'c006', to_date('08-MAR-2023', 'DD-MON-YYYY'), 10, 0.99, 8.90, 1.0);
insert into purchases values (10012, 'e02', 'p008', 'c003', to_date('18-FEB-2023', 'DD-MON-YYYY'), 2, 374.25, 748.50, 249.50);
insert into purchases values (10013, 'e04', 'p009', 'c005', to_date('30-AUG-2022', 'DD-MON-YYYY'), 2, 16.65, 33.30, 3.70);
insert into purchases values (10014, 'e03', 'p010', 'c008', to_date('14-NOV-2022', 'DD-MON-YYYY'), 3, 27, 81, 9);