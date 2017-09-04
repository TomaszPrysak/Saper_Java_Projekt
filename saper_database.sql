create database saper_database;

use saper_database;

drop database saper_database;

create table results (
	id_result int unsigned not null auto_increment,
    user_name varchar(50) not null,
    stopwatch double not null,
    qty_mine int not null,
    primary key (id_result)
);

update results set stopwatch = 300 where user_name = 'Tomek';

select * from results where user_name = 'Tomek';

select * from results where user_name = 'Tomek' and qty_mine = 15;

select * from results where qty_mine = 15;

select count(*) from results where stopwatch < 45 and qty_mine = 15;

select * from results group by stopwatch;

select * from results;

select user_name, stopwatch from results group by stopwatch;

delete from results where user_name = 'Tomek';

delete from results;

insert into results (user_name, stopwatch, qty_mine) values ('Tomek', 39, 10);
insert into results (user_name, stopwatch, qty_mine) values ('Tomek', 60, 15);
insert into results (user_name, stopwatch, qty_mine) values ('Ania', 45, 10);
insert into results (user_name, stopwatch, qty_mine) values ('Ania', 55, 15);
insert into results (user_name, stopwatch, qty_mine) values ('Wojtek', 5, 10);
insert into results (user_name, stopwatch, qty_mine) values ('Wojtek', 2, 15);

select count(*) from results where stopwatch < 104;

select now();