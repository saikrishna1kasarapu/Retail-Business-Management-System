-- package project2 specification 
set serveroutput on 

create or replace package project2 as
type ref_cursor is ref cursor;
function show_logs return ref_cursor;
function show_employees return ref_cursor;
function show_customers return ref_cursor;
function show_products return ref_cursor;
function show_proddiscnt return ref_cursor;
function show_purchases return ref_cursor;

/* answer for question 4, a procedure to report the monthly sale activity information for any given employee */
procedure find_monthlysaleactivities(employee_id in employees.eid%type, rc out ref_cursor, status out integer);

/* answer for question 4, a procedure for adding tuples to the Employees table.*/
procedure add_employees(e_id in employees.eid%type, e_name in employees.name%type, e_telephone# in employees.telephone#%type, e_email in employees.email%type,show_message out varchar);

/* answer for question 5, a procedure for adding tuples to the Purchases table.*/ 
procedure add_purchase(e_id in purchases.eid%type, p_id in purchases.pid%type, c_id in purchases.cid%type, pur_qty in purchases.quantity%type, pur_unit_price in purchases.unit_price%type, status out integer, show_message out varchar);

end;
/
show errors

/* package body part */
create or replace package body project2 as

/* answer for question 2, functions to show the tuples in logs */
function show_logs
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from logs;
return rc;
end;

/* answer for question 2, functions to show the tuples in employees */
function show_employees
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from employees;
return rc;
end;

/* answer for question 2, functions to show the tuples in customers */
function show_customers
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from customers;
return rc;
end;

/* answer for question 2, functions to show the tuples in products */
function show_products
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from products;
return rc;
end;

/* answer for question 2, functions to show the tuples in discounts */
function show_proddiscnt
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from prod_discnt;
return rc;
end;

/* answer for question 2, functions to show the tuples in purchases */
function show_purchases
return ref_cursor is
rc ref_cursor;
begin
open rc for
select * from purchases;
return rc;
end;

procedure find_monthlysaleactivities(employee_id in employees.eid%type, rc out ref_cursor, status out integer) is
emp_id employees.eid%type;
begin
	open rc for
	select * from 
		(select eid, name from employees where eid = employee_id) a,  
		(select MON, YEAR, count(*) sales_times,sum(quantity) sales_qty, sum(payment) sales_amount from 
		(select to_char(pur_time,'MON') MON, to_char(pur_time, 'YYYY') YEAR, quantity, payment from purchases where eid = employee_id) 
		group by MON, YEAR) b;

	status := 0;
	select eid into emp_id from employees where eid = employee_id;
exception
	when NO_DATA_FOUND then
		raise_application_error(-20001,'Employee with the given ID not found!');
	when OTHERS then
        status := 2;
        raise_application_error(-20002, 'An error occurred: ' || SQLERRM);
end;

--procedure to add a tuple in employees table
procedure add_employees(e_id in employees.eid%type, e_name in employees.name%type, e_telephone# in employees.telephone#%type, e_email in employees.email%type, show_message out varchar)
is invalid_data exception;
begin
	if (REGEXP_LIKE(e_id, '^e[0-9]{2}$') AND REGEXP_LIKE(e_telephone#, '^(\d+-)*\d+$') AND REGEXP_LIKE(e_name, '^[A-Za-z ]+$')) THEN
		insert into employees values(e_id,e_name,e_telephone#,e_email); 
		show_message := 'employee has been added succesfully';
	else
		RAISE invalid_data;
	end if;

exception
    WHEN invalid_data THEN
        raise_application_error(-20001, 'Invalid data entered. Check employee ID, name, and telephone number format.');
end add_employees;

procedure add_purchase(e_id in purchases.eid%type, p_id in purchases.pid%type, c_id in purchases.cid%type, pur_qty in purchases.quantity%type, pur_unit_price in purchases.unit_price%type, status out integer, show_message out varchar) is 

t_price purchases.payment%type;
savings purchases.saving%type;
item_cost products.orig_price%type;
qty_limit products.qoh%type;
threshold products.qoh_threshold%type;
pup products.orig_price%type;
invalid_qty exception;
eno employees.eid%type;
pno products.pid%type;
cno customers.cid%type;

begin
	status := 2;
	select eid into eno from employees where eid = e_id;
	status := 3;
	select cid into cno from customers where cid = c_id;
	status := 4;	
	select qoh, pid, qoh_threshold into qty_limit, pno, threshold from products where pid = p_id;
	/*status := 5;
	select TO_CHAR(ROUND((orig_price * (1 - d.discnt_rate)),2)) into pup from products, prod_discnt d where pup = pur_unit_price;*/

	select (p.orig_price * (1 - d.discnt_rate)), (p.orig_price * (1 - d.discnt_rate)) , (p.orig_price *pur_qty*d.discnt_rate) into t_price , item_cost, savings from products p, prod_discnt d where p.pid = p_id and p.discnt_category = d.discnt_category;
	t_price := t_price * pur_qty;

	if(pur_qty > qty_limit) then 
		raise invalid_qty;
	else

		insert into purchases values (seq_pur#.nextval, e_id, p_id, c_id, sysdate, pur_qty, item_cost, t_price, savings);
		if(qty_limit - pur_qty < threshold) then
			show_message := 'The current qoh of the product is below the required threshold and new supply is required. The new value of qoh after supply is: ' || to_char(threshold + 20);
		else
			show_message := 'The current qoh of the product is above the required threshold.';
		end if;

	end if;

	status := 0;

	if(qty_limit - pur_qty < threshold) then
		status := threshold + 20;
	end if;

exception
	when invalid_qty then 
		dbms_output.put_line('Insufficient quantity in stock.');
		status := 1;
	when NO_DATA_FOUND then
		dbms_output.put_line('Invalid cid, pid or eid.');

end;

end;
/

show errors