create database saper_database;

use saper_database;

#drop database saper_database;

create table results (
	id_result int unsigned not null auto_increment,
    user_name varchar(30) unique not null,
    stopwatch float not null,
    primary key (id_result)
);

select * from results where user_name = 'tomi';

select * from results;

delete from results where user_name = 'Tomek';

insert into results (user_name, stopwatch) values ('Tomek', 39.9);

select now();