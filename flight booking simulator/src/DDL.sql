DROP TABLE IF EXISTS Customer, Credit_Address, Airport, Airline, Flight, Price, Booking;

create table Customer 
	(email	varchar(20),
	 name	varchar(20) NOT NULL,
	 primary key (email)
	);
	
create table Credit_Address
	(credit_card_number	numeric(6,0),
	 address			varchar(50) NOT NULL,
	 email 				varchar(20) NOT NULL,
	 primary key (credit_card_number),
	 foreign key (email) references Customer (email)
	);
	
create table Airport
	(IATA 				char(3),
	 name				varchar(200) NOT NULL,
	 country_located	varchar(20) NOT NULL,
	 state_in 			varchar(20),
	 primary key (IATA)
	);	
	
create table Airline
	(airline_code		char(2),
	 airline_name		char(30) NOT NULL,
	 country_of_origin	char(30) NOT NULL,
	 primary key (airline_code)
	);
	
create table Flight
	(airline_code		char(2),
	 flight_number		integer,
	 date_of_flight		char(10),
	 departure_IATA		char(3),
	 destination_IATA 	char(3),
	 departure_time		numeric(4,0) check (departure_time < 2359),
	 arrival_time 		numeric(4,0) check (arrival_time < 2359),
	 time_diff			integer,
	 FC_max				numeric(5,0),
	 Econ_max			numeric(5,0),
	 primary key (flight_number),
	 foreign key (departure_IATA) references Airport (IATA),
	 foreign key (destination_IATA) references Airport (IATA)
	);
	
create table Price
	(flight_number		integer,
	 price_econ			numeric(8,2) check (price_FC > price_econ),
	 price_FC 			numeric(8,2) check (price_FC > price_econ),
	 foreign key (flight_number) references Flight (flight_number)
	);
	
create table Booking
	(email				varchar(20),
	 credit_card_number	numeric(6,0),
	 seat_type			varchar(5) check (seat_type in ('FC', 'Econ')),
	 flight_number		integer,
	 foreign key (email) references Customer (email),
	 foreign key (flight_number) references Flight (flight_number),
	 foreign key (credit_card_number) references Credit_Address (credit_card_number)
	);
	 
	 