create database saper_database;

use saper_database;

#drop database saper_database;

create table results (
	id_result int unsigned not null auto_increment,
    user_name varchar(30) unique not null,
    stopwatch double not null,
    primary key (id_result)
);

update results set stopwatch = 300 where user_name = 'Tomek';

select * from results where user_name = 'Tomek';

select * from results;

select * from results group by stopwatch;

select user_name, stopwatch from results group by stopwatch;

delete from results where user_name = 'Tomek';

delete from results;

insert into results (user_name, stopwatch) values ('Tomek', 39.9);
insert into results (user_name, stopwatch) values ('Ania', 45);
insert into results (user_name, stopwatch) values ('Tina', 5.3);

select count(*) from results where stopwatch < 104;

select now();