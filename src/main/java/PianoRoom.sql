-- mysql script generated by mysql workbench
-- thu dec 21 12:38:32 2023
-- model: new model    version: 1.0
-- mysql workbench forward engineering

set @old_unique_checks=@@unique_checks, unique_checks=0;
set @old_foreign_key_checks=@@foreign_key_checks, foreign_key_checks=0;
set @old_sql_mode=@@sql_mode, sql_mode='only_full_group_by,strict_trans_tables,no_zero_in_date,no_zero_date,error_for_division_by_zero,no_engine_substitution';

-- -----------------------------------------------------
-- schema pianoroom
-- -----------------------------------------------------
-- create schema if not exists pianoroom default character set utf8mb4 collate utf8mb4_0900_ai_ci;
-- use pianoroom;

drop table if exists pianoroom.major;
drop table if exists pianoroom.user;
drop table if exists pianoroom.business_hour;
drop table if exists pianoroom.room;
drop table if exists pianoroom.reservation;

-- -----------------------------------------------------
-- table pianoroom.major
-- -----------------------------------------------------
create table if not exists pianoroom.major(
  id int auto_increment,
  major varchar(45) not null unique,
  primary key (id))
engine = innodb
default character set = utf8mb4
collate = utf8mb4_0900_ai_ci
auto_increment = 1;

-- -----------------------------------------------------
-- table pianoroom.user
-- -----------------------------------------------------
create table if not exists pianoroom.user(
  id int not null auto_increment,
  name varchar(45) not null,
  email varchar(60) not null unique,
  password varchar(45) not null,
  major_id int null default null,
  level int not null,
  avator varchar(800) not null default 'default.png',
  -- auth_type enum('local', 'github', 'google') default 'local',
  -- auth_id varchar(100),
  primary key (id),
  index fk_user_major_id_idx (major_id asc) visible,
  index idx_user_level (level asc),
  constraint fk_user_major_id
    foreign key (major_id)
    references pianoroom.major (id))
engine = innodb
auto_increment = 1
default character set = utf8mb4
collate = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- table pianoroom.business_hour
-- -----------------------------------------------------
create table if not exists pianoroom.business_hour(
  room_id int not null,
  day_of_week varchar(45) not null,
  opening_time time,
  closing_time time,
  index fk_businesshr_room_id_idx (room_id asc) visible,
  constraint fk_businesshr_room_id
    foreign key (room_id)
    references pianoroom.room (id))
engine = innodb
auto_increment = 1
default character set = utf8mb4
collate = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- table pianoroom.room
-- -----------------------------------------------------
create table if not exists pianoroom.room(
  id int not null auto_increment,
  name varchar(60) not null,
  dist varchar(60) not null,
  type varchar(60) not null,
  latitude double not null,
  longitude double not null,
  image varchar(800) not null default 'default.png',
  primary key (id))
engine = innodb
auto_increment = 1
default character set = utf8mb4
collate = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- table pianoroom.reservation
-- -----------------------------------------------------
create table if not exists pianoroom.reservation(
  id int not null auto_increment,
  user_id int not null,
  room_id int not null,
  start_time timestamp not null,
  end_time timestamp not null,
  checkin timestamp null default null,
  checkout timestamp null default null,
  primary key (id),
  index fk_reservation_user_id_idx (user_id asc) visible,
  index fk_reservation_room_id_idx (room_id asc) visible,
  constraint fk_reservation_room_id
    foreign key (room_id)
    references pianoroom.room (id),
  constraint fk_reservation_user_id
    foreign key (user_id)
    references pianoroom.user (id))
engine = innodb
auto_increment = 1
default character set = utf8mb4
collate = utf8mb4_0900_ai_ci;

set sql_mode=@old_sql_mode;
set foreign_key_checks=@old_foreign_key_checks;
set unique_checks=@old_unique_checks;


-- -----------------------------------------------------
-- 建立 major
-- -----------------------------------------------------
insert into pianoroom.major(major)
values ('鋼琴'), ('小提琴'), ('中提琴'), ('大提琴'), ('低音提琴'),
	    ('長笛'), ('雙簧管'), ('單簧管'), ('低音管'), ('法國號'), 
       ('小號'), ('長號'), ('低音號'), ('打擊'), ('聲樂'), ('作曲'), ('其他');
       
-- -----------------------------------------------------
-- 建立 user (Encrypt password with AES)
-- KEY = a1b2c3d4e5f6g7h88h7g6f5e4d3c2b1a  // PW = name+123
-- -----------------------------------------------------
insert into pianoroom.user(email, password, name, major_id, level, avator) 
values ('fjchengou@gmail.com', 'STBHSjCpTUfxCHpgXIIKWA==', 'admin', 2, 1, 'default.png'),
	   ('amy@gmail.com', '4PEaSpq2oEH7FZbrvcKRnA==', 'amy', 1, 2, '2amy-piano.png'),
	   ('ben@gmail.com', 'rVifyS3CWHtNxvORpvNwMA==', 'ben', 11, 2, '3ben-wind-instrument.png'),
       ('carl@gmail.com', 'TC1zTsewyqXKMtEvyPEXOA==', 'carl', 14, 2, '4carl-timpani.png');

-- -----------------------------------------------------
-- 建立 room
-- -----------------------------------------------------
insert into pianoroom.room(name, dist, type, latitude, longitude, image) 
values ('503', '台北民生校', '教學琴房', 25.05924488253332, 121.5425743262388, 'default.png'),
	   ('504', '台北民生校', '練習琴房', 25.057972977895904, 121.54228159001215, 'room2-CH.jpg'),
       ('505', '台北民生校', '練習琴房', 25.057972977895904, 121.54228159001215, 'room3-cover.jpg'),
       ('506', '台北民生校', '練習琴房', 25.057972977895904, 121.54228159001215, 'room4-piano.jpg'),
       ('音樂廳', '兩廳院校', '表演琴房', 25.03685242728929, 121.51897022545847, 'room9-NTCH-CH.jpg'),
       ('演奏廳', '兩廳院校', '表演琴房', 25.03685242728929, 121.51897022545847, 'room10-NTCH-RH.jpg'),
       ('貝多芬室', '台北中正校', '練習琴房', 25.057972977895904, 121.54228159001215, 'default.png'),
	   ('莫札特室', '台北中正校', '練習琴房', 25.057972977895904, 121.54228159001215, 'room2-CH.jpg'),
	   ('巴哈室', '台北中正校', '教學琴房', 25.045608340381392, 121.52511466517534, 'room3-cover.jpg'),
       ('海頓室', '台北中正校', '教學琴房', 25.045608340381392, 121.52511466517534, 'room4-piano.jpg');

-- -----------------------------------------------------
-- 建立 business_hour
-- -----------------------------------------------------
insert into pianoroom.business_hour(room_id, day_of_week, opening_time, closing_time) 
values ('1', 'monday', '01:00:00', '23:00:00'), ('1', 'tuesday', '01:00:00', '23:00:00'), ('1', 'wednesday', '01:00:00', '23:00:00'), ('1', 'thursday', '01:00:00', '23:00:00'), ('1', 'friday', '01:00:00', '23:00:00'), ('1', 'saturday', '01:00:00', '23:00:00'), ('1', 'sunday', '00:00:00', '23:00:00'),
       ('2', 'monday', '10:00:00', '20:00:00'), ('2', 'tuesday', '10:00:00', '20:00:00'), ('2', 'wednesday', '10:00:00', '20:00:00'), ('2', 'thursday', '10:00:00', '20:00:00'), ('2', 'friday', '10:00:00', '20:00:00'), ('2', 'saturday', null, null), ('2', 'sunday', null, null),
	   ('3', 'monday', '08:00:00', '20:00:00'), ('3', 'tuesday', '08:00:00', '20:00:00'), ('3', 'wednesday', '08:00:00', '20:00:00'), ('3', 'thursday', '08:00:00', '20:00:00'), ('3', 'friday', null, null), ('3', 'saturday', '08:00:00', '20:00:00'), ('3', 'sunday', '08:00:00', '20:00:00'),
	   ('4', 'monday', '01:00:00', '23:00:00'), ('4', 'tuesday', '01:00:00', '23:00:00'), ('4', 'wednesday', '01:00:00', '23:00:00'), ('4', 'thursday', '01:00:00', '23:00:00'), ('4', 'friday', '01:00:00', '23:00:00'), ('4', 'saturday', '01:00:00', '23:00:00'), ('4', 'sunday', '01:00:00', '23:00:00'),
       ('5', 'monday', '10:00:00', '20:00:00'), ('5', 'tuesday', '10:00:00', '20:00:00'), ('5', 'wednesday', '10:00:00', '20:00:00'), ('5', 'thursday', '10:00:00', '20:00:00'), ('5', 'friday', '10:00:00', '20:00:00'), ('5', 'saturday', '08:00:00', '20:00:00'), ('5', 'sunday', null, null),
       ('6', 'monday', '19:00:00', '20:00:00'), ('6', 'tuesday', '10:00:00', '20:00:00'), ('6', 'wednesday', '10:00:00', '20:00:00'), ('6', 'thursday', '10:00:00', '20:00:00'), ('6', 'friday', null, null), ('6', 'saturday', '08:00:00', '20:00:00'), ('6', 'sunday', null, null),
       ('7', 'monday', '10:00:00', '20:00:00'), ('7', 'tuesday', null, null), ('7', 'wednesday', '10:00:00', '20:00:00'), ('7', 'thursday', '10:00:00', '20:00:00'), ('7', 'friday', '10:00:00', '20:00:00'), ('7', 'saturday', '08:00:00', '20:00:00'), ('7', 'sunday', null, null),
	   ('8', 'monday', '08:00:00', '20:00:00'), ('8', 'tuesday', null, null), ('8', 'wednesday', '08:00:00', '20:00:00'), ('8', 'thursday', '08:00:00', '20:00:00'), ('8', 'friday', '08:00:00', '20:00:00'), ('8', 'saturday', '08:00:00', '20:00:00'), ('8', 'sunday', '08:00:00', '20:00:00'),
       ('9', 'monday', '01:00:00', '23:00:00'), ('9', 'tuesday', '10:00:00', '20:00:00'), ('9', 'wednesday', '08:00:00', '20:00:00'), ('9', 'thursday', '08:00:00', '20:00:00'), ('9', 'friday', '08:00:00', '20:00:00'), ('9', 'saturday', '08:00:00', '20:00:00'), ('9', 'sunday', '08:00:00', '20:00:00'),
       ('10', 'monday', '01:00:00', '23:00:00'), ('10', 'tuesday', '10:00:00', '20:00:00'), ('10', 'wednesday', '08:00:00', '20:00:00'), ('10', 'thursday', '08:00:00', '20:00:00'), ('10', 'friday', null, null), ('10', 'saturday', '08:00:00', '20:00:00'), ('10', 'sunday', '08:00:00', '20:00:00');
-- -----------------------------------------------------
-- 建立 reservation
-- -----------------------------------------------------
insert into pianoroom.reservation(user_id ,room_id, start_time, end_time, checkin, checkout)
values ('1', '1', '2024-01-01 14:00:00', '2024-01-01 16:00:00', '2024-01-01 14:01:00', '2024-01-01 15:59:32'),
	   ('1', '9', '2024-01-02 19:00:00', '2024-01-02 21:00:00', '2024-01-02 19:03:40', '2024-01-02 21:51:23'),
	   ('1', '7', '2024-01-03 10:00:00', '2024-01-03 12:00:00', '2024-01-03 10:00:02', '2024-01-03 11:50:55'),
	   ('2', '2', '2024-01-02 14:00:00', '2024-01-02 16:00:00', '2024-01-02 14:05:02', '2024-01-02 15:52:12'),
       ('3', '3', '2024-01-02 14:00:00', '2024-01-02 16:00:00', '2024-01-02 14:01:03', '2024-01-02 15:56:52'),
       ('4', '4', '2024-01-02 14:00:00', '2024-01-02 16:00:00', '2024-01-02 14:02:00', '2024-01-02 14:40:43'),
       ('1', '1', '2024-01-08 12:00:00', '2024-01-08 14:00:00', '2024-01-08 12:04:56', '2024-01-08 15:50:10'),
       ('2', '7', '2024-01-07 12:00:00', '2024-01-07 13:00:00', '2024-01-07 12:05:09', '2024-01-07 12:42:18'),
	   ('3', '8', '2024-01-07 12:00:00', '2024-01-07 13:00:00', '2024-01-07 12:02:12', '2024-01-07 12:57:25'),
       ('1', '2', '2024-01-04 14:00:00', '2024-01-04 18:00:00', '2024-01-04 14:01:23', '2024-01-04 17:59:32'),
       ('1', '1', '2024-01-05 14:00:00', '2024-01-05 16:00:00', '2024-01-05 14:12:15', '2024-01-05 15:57:32'),
       ('1', '9', '2024-01-06 14:00:00', '2024-01-06 19:00:00', '2024-01-06 14:01:04', '2024-01-06 18:46:32'),
       ('1', '2', '2024-01-07 14:00:00', '2024-01-07 16:00:00', '2024-01-07 14:14:03', '2024-01-07 15:54:43'),
       ('1', '5', '2024-01-09 14:00:00', '2024-01-09 18:00:00', '2024-01-09 14:05:29', '2024-01-09 17:55:32'),
       ('1', '3', '2024-01-10 14:00:00', '2024-01-10 16:00:00', '2024-01-10 14:02:38', '2024-01-10 15:48:12'),
       ('1', '7', '2024-01-11 14:00:00', '2024-01-11 16:00:00', '2024-01-11 14:02:46', '2024-01-11 15:58:52'),
       ('1', '4', '2024-01-12 14:00:00', '2024-01-12 17:00:00', '2024-01-12 14:01:23', '2024-01-12 16:54:43'),
       ('1', '6', '2024-01-13 14:00:00', '2024-01-13 16:00:00', '2024-01-13 14:02:28', null),
       ('1', '1', '2024-01-14 14:00:00', '2024-01-14 16:00:00', '2024-01-14 14:02:15', '2024-01-14 15:56:34'),
       ('1', '8', '2024-01-15 14:00:00', '2024-01-15 17:00:00', '2024-01-15 14:02:23', '2024-01-15 17:32:12'),
       ('1', '10', '2024-01-16 14:00:00', '2024-01-16 19:00:00', '2024-01-16 14:11:23', '2024-01-16 18:43:45'),
       ('1', '6', '2024-01-17 14:00:00', '2024-01-17 18:00:00', '2024-01-17 14:02:26', null);
       -- ('1', '1', '2024-01-18 14:00:00', '2024-01-18 16:00:00', '2024-01-18 14:02:49', null),
	   -- ('1', '9', '2024-01-19 14:00:00', '2024-01-19 16:00:00', '2024-01-19 14:02:51', null),
       -- ('1', '5', '2024-01-20 14:00:00', '2024-01-20 17:00:00', '2024-01-20 14:02:28', null),
       -- ('1', '6', '2024-01-21 14:00:00', '2024-01-21 16:00:00', '2024-01-21 14:12:46', null),
       -- ('1', '7', '2024-01-22 14:00:00', '2024-01-22 17:00:00', '2024-01-22 14:02:15', null),
	   -- ('1', '6', '2024-01-17 14:00:00', '2024-01-17 18:00:00', '2024-01-17 14:02:26', '2024-01-17 17:52:32'),
       -- ('1', '1', '2024-01-18 14:00:00', '2024-01-18 20:00:00', '2024-01-18 14:12:00', '2024-01-18 16:43:00'),
  	   -- ('1', '9', '2024-01-19 14:00:00', '2024-01-19 16:00:00', '2024-01-19 14:02:51', '2024-01-19 15:59:59'),
       -- ('1', '5', '2024-01-20 14:00:00', '2024-01-20 17:00:00', '2024-01-20 14:02:28', '2024-01-20 16:39:24'),
	   -- ('1', '6', '2024-01-21 14:00:00', '2024-01-21 16:00:00', '2024-01-21 14:12:46', '2024-01-21 15:58:52'),
	   -- ('1', '7', '2024-01-22 14:00:00', '2024-01-22 17:00:00', '2024-01-22 14:02:15', '2024-01-22 16:52:43'),
	   -- ('1', '3', '2024-01-23 14:00:00', '2024-01-23 16:00:00', '2024-01-23 14:02:28', '2024-01-23 15:58:12'),
	   -- ('1', '2', '2024-01-24 14:00:00', '2024-01-24 16:00:00', '2024-01-24 14:06:15', '2024-01-24 15:53:34'),
	   -- ('1', '2', '2024-01-25 14:00:00', '2024-01-25 16:00:00', '2024-01-25 14:02:49', '2024-01-25 15:37:12'),
	   -- ('1', '5', '2024-01-26 14:00:00', '2024-01-26 19:00:00', '2024-01-26 14:01:23', '2024-01-26 18:54:45'),
	   -- ('1', '4', '2024-01-27 14:00:00', '2024-01-27 17:00:00', '2024-01-27 14:02:26', '2024-01-27 16:52:32'),
	   -- ('1', '7', '2024-01-28 14:00:00', '2024-01-28 16:00:00', '2024-01-28 14:13:49', '2024-01-28 15:54:24'),
	   -- ('1', '8', '2024-01-29 14:00:00', '2024-01-29 16:00:00', '2024-01-29 14:02:51', '2024-01-29 15:43:59'),
	   -- ('1', '9', '2024-01-30 14:00:00', '2024-01-30 17:00:00', '2024-01-30 14:02:26', '2024-01-30 16:59:24'),
	   -- ('1', '10', '2024-01-31 14:00:00', '2024-01-31 17:00:00', '2024-01-31 14:09:28', '2024-01-31 16:46:24');
       

-- -----------------------------------------------------
-- 建立 view 所有琴房當日營業時間
-- -----------------------------------------------------
create or replace view curdatebusinesshoursview as 
select r.id as room_id, bh.day_of_week, bh.opening_time, bh.closing_time
from pianoroom.room r
join pianoroom.business_hour bh on r.id = bh.room_id
where bh.day_of_week = dayname(now());

-- -----------------------------------------------------
-- 建立 view 所有琴房當下使用狀況
-- -----------------------------------------------------
create or replace view currentroomstatusview as 
select r.id , r.name, r.dist, r.type, r.latitude, r.longitude,
    case
		when (curtime() not between bh.opening_time and bh.closing_time) 
			 or (bh.opening_time is null and bh.closing_time is null)
             then '未開放'
        when (curtime() between bh.opening_time and bh.closing_time) 
			 and (now() between rs.start_time and rs.end_time) then
			case
				when rs.checkin is null then '已預約'
                when (rs.checkin and rs.checkout) is not null then '使用完畢'  -- 如果已經簽退
				when now() < rs.end_time and rs.checkout is null then '使用中'
			end
		 else '空琴房'
    end as status
from pianoroom.room r
left join pianoroom.business_hour bh on r.id = bh.room_id
left join pianoroom.reservation rs on r.id = rs.room_id and (now() between rs.start_time and rs.end_time)
where (dayname(now()) = bh.day_of_week);

-- -----------------------------------------------------
-- 建立 view 當月資料狀況  -- 如果已經簽退，將狀態改為「空琴房」
-- -----------------------------------------------------
create or replace view monthlydatasview as 
select u.id as user_id, u.name, u.email, u.major_id, u.avator,
	count(rs.id) as counts,
	coalesce(sum(timestampdiff(minute, rs.checkin, rs.checkout)), 0) as minutes,
	rank() over (order by coalesce(sum(timestampdiff(minute, rs.checkin, rs.checkout)), 0) desc) as ranking
from pianoroom.user u
left join pianoroom.reservation rs on rs.user_id = u.id and year(rs.checkin) = year(curdate()) and month(rs.checkin) = month(curdate())
group by u.id;

-- -----------------------------------------------------
-- 建立 view 當月的日期臨時表
-- -----------------------------------------------------
create or replace view temporarydaytable as
select 
    curdate() - interval (day(curdate()) - 1) day + interval days day as date
  from (
    select 0 as days union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9 union select 10 union select 11 union select 12 union select 13 union select 14 union select 15 union select 16 union select 17 union select 18 union select 19 union select 20 union select 21 union select 22 union select 23 union select 24 union select 25 union select 26 union select 27 union select 28 union select 29 union select 30
  ) as date_sequence;
  
-- -----------------------------------------------------
-- 取得該月的日期對應使用者每日的練習時數 (Dao使用)
-- -----------------------------------------------------
-- select 
--   r.user_id,
--   day(tdt.date) as day,
--   coalesce(sum(timestampdiff(minute, r.checkin, r.checkout)), 0) as minutes
-- from pianoroom.temporarydaytable tdt
-- left join pianoroom.reservation r on date(tdt.date) = date(r.checkin) and r.user_id = 1
-- where month(tdt.date) = month(curdate())
-- group by tdt.date