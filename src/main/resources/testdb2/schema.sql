drop table T_POST if exists;

create table T_POST (ID bigint identity primary key, NUMBER varchar(9), THREAD varchar(9),
                        SUBJECT varchar(100) not null, BODY varchar(200));

ALTER TABLE T_POST ALTER COLUMN THREAD SET DEFAULT 111111111;