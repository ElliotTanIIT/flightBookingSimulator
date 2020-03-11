delete from Booking;
delete from Credit_Address;
delete from Customer;
delete from Price;
delete from Flight;
delete from Airport;
delete from Airline;

insert into Customer values ('john@hmail.com', 'John');
insert into Customer values ('ryan@hmail.com', 'Ryan');
insert into Customer values ('rick@hmail.com', 'Rick');
insert into Customer values ('jane@hmail.com', 'Jane');
insert into Customer values ('matt@hmail.com', 'Matt');

insert into Credit_Address values (123456, '1st Street', 'john@hmail.com');
insert into Credit_Address values (234567, '2nd Street', 'john@hmail.com');
insert into Credit_Address values (345678, '3rd Street', 'john@hmail.com');
insert into Credit_Address values (654321, 'A Street', 'rick@hmail.com');
insert into Credit_Address values (765432, 'B Street', 'rick@hmail.com');
insert into Credit_Address values (111111, 'hello street', 'matt@hmail.com');
insert into Credit_Address values (222222, 'maple street', 'jane@hmail.com');
insert into Credit_Address values (135313, 'google road', 'ryan@hmail.com');

insert into Airport values ('ORD', 'Chicago O Hare International Airport','USA', 'Illinois');
insert into Airport values ('HND', 'Tokyo Haneda International Airport', 'Japan');
insert into Airport values ('TPE', 'Taiwan Taoyuan International Airport', 'Taiwan');
insert into Airport values ('YYZ', 'Toronto Pearson International Airport', 'Canada', 'Ontario');

insert into Airline values ('AA' , 'American Airlines', 'USA'); 
insert into Airline values ('JP' , 'Japan Airlines' , 'Japan');

insert into Flight values('AA', 26, '22/10/2006', 'ORD', 'HND', 0700, 2100, 14, 30, 200);
insert into Flight values('AA', 62, '22/11/2006', 'HND', 'ORD', 0700, 2100, 14, 30, 200);
insert into Flight values('AA', 44, '23/10/2006', 'ORD', 'TPE', 0800, 2300, 15, 30, 200);
insert into Flight values('AA', 88, '23/11/2006', 'TPE', 'ORD', 0845, 2335, 15, 30, 200);
insert into Flight values('JP', 10, '24/10/2006', 'HND', 'YYZ', 0500, 1800, 13, 100, 600);
insert into Flight values('JP', 11, '24/11/2006', 'YYZ', 'HND', 0500, 1800, 13, 100, 600);
insert into Flight values('JP', 63, '25/10/2006', 'HND', 'TPE', 1400, 2000, 06, 50, 300);
insert into Flight values('JP', 36, '25/11/2006', 'TPE', 'HND', 1400, 2000, 06, 50, 300);
insert into Flight values('JP', 17, '26/10/2006', 'HND', 'ORD', 0000, 1800, 18, 75, 500);
insert into Flight values('JP', 71, '26/11/2006', 'ORD', 'HND', 0000, 1800, 18, 75, 500);

insert into Price values(26, 300, 2000);
insert into Price values(62, 300, 2000);
insert into Price values(44, 200, 4000);
insert into Price values(88, 200, 4000);
insert into Price values(10, 400, 2500);
insert into Price values(11, 400, 2500);
insert into Price values(63, 250, 6500);
insert into Price values(36, 250, 6500);
insert into Price values(17, 330, 4500);
insert into Price values(71, 330, 4500);

insert into Booking values('john@hmail.com', 123456, 'FC', 26);
insert into Booking values('john@hmail.com', 123456, 'FC', 62);