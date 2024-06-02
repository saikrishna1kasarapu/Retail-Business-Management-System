drop sequence seq_pur#;
drop sequence seq_log#;

create sequence seq_pur# start with 10001 increment by 1 nocache nocycle;
create sequence seq_log# start with 1001 increment by 1 nocache nocycle;

select seq_pur#.nextval from purchases;