drop table T_POST if exists;

create table T_POST (ID bigint identity primary key, NUMBER varchar(9), THREAD varchar(9),
                         SUBJECT varchar(50) not null, BODY varchar(100), unique(NUMBER));

ALTER TABLE T_POST ALTER COLUMN THREAD SET DEFAULT 666666666;
