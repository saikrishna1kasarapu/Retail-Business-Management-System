drop trigger insert_logs;
drop trigger update_qoh;
drop trigger update_customers;
drop trigger update_last_visit_date;
drop trigger update_visits_made;
drop trigger insert_purchase;
drop trigger update_qoh_log;

/*      Question 4     */
 -- Trigger that insert values into log when tuple is inserted in employees table.
create or replace trigger insert_logs
AFTER insert on employees
referencing new as N for each row
begin
insert into logs values (seq_log#.nextval, USER, 'insert', sysdate, 'employees', :N.eid);
end;
/
show errors

create or replace trigger update_qoh 
after insert on purchases
for each row
declare
  qty products.qoh%type;
  lim products.qoh_threshold%type;
begin
  UPDATE products SET qoh = qoh - :new.quantity WHERE pid = :new.pid;
  select qoh, qoh_threshold into qty, lim from products where pid = :new.pid;
  if(qty < lim) then
    dbms_output.put_line('The current qoh of the product is below the required threshold and new supply is required');
    update products set qoh = lim + 20 where pid = :new.pid;
    dbms_output.put_line('The new value of qoh after supply is: ' || qty );
  else
    dbms_output.put_line('The current qoh of the product is above the required threshold');
  end if;
end;
/
show errors

CREATE OR REPLACE TRIGGER update_customers
AFTER INSERT ON purchases
FOR EACH ROW
BEGIN
  update customers set visits_made = visits_made + 1 where cid = :new.cid;
	update customers set last_visit_date = sysdate where cid = :new.cid and trunc(last_visit_date) <> trunc(sysdate);
END;
/
show errors

CREATE OR REPLACE TRIGGER update_last_visit_date
AFTER UPDATE OF last_visit_date ON customers
FOR EACH ROW
BEGIN
    INSERT INTO logs VALUES (seq_log#.NEXTVAL, USER, 'update', SYSDATE, 'customers', :NEW.cid);
END;
/
show errors

CREATE OR REPLACE TRIGGER update_visits_made
AFTER UPDATE OF visits_made ON customers
FOR EACH ROW
BEGIN
    INSERT INTO logs VALUES (seq_log#.NEXTVAL, USER, 'update', SYSDATE, 'customers', :NEW.cid);
END;
/
show errors

CREATE OR REPLACE TRIGGER insert_purchase
AFTER INSERT ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO logs VALUES (seq_log#.NEXTVAL, USER, 'insert', SYSDATE, 'purchases', :NEW.pur#);
END;
/
show errors

CREATE OR REPLACE TRIGGER update_qoh_log
AFTER UPDATE OF qoh ON products
FOR EACH ROW
BEGIN
    INSERT INTO logs values (seq_log#.NEXTVAL, USER, 'update', SYSDATE, 'products', :NEW.pid);
END;
/

show errors
