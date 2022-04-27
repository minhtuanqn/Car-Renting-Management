use master

CREATE database Assignment3_LeMinhTuan

use Assignment3_LeMinhTuan

create table Accounts (
	email varchar(50) primary key,
	password varchar(50),
	phone varchar(20),
	name varchar(50),
	address varchar(20),
	createDate date,
	status varchar(20),
	token varchar(30)
)

create table Discounts (
	id varchar(20) primary key,
	percentOfDiscount int,
	dateFrom date,
	dateTo date
)


create table Cars (
	name varchar(50) primary key,
	color varchar(50),
	year int,
	category varchar(30),
	price float, 
	quantity int
)

create table OrderHistory (
	id int IDENTITY(1,1) primary key,
	createDate date,
	totalBeforeDiscount float,
	totalAfterDiscount float,
	userEmail varchar(50),
	discountId varchar(20),
	orderStatus bit
)

create table OrderDetails(
	orderId int foreign key references OrderHistory(id),
	carId varchar(50) foreign key references Cars(name),
	dateFrom date,
	dateTo date,
	quantity int,
	price float,
	feedback varchar(200),
	rating int,
	Primary key(orderId, carId, dateFrom, dateTo)
)

ALTER TABLE OrderHistory
	ADD CONSTRAINT fk_History_Discount
	FOREIGN KEY (discountId)
	REFERENCES Discounts (id);

ALTER TABLE OrderHistory
	ADD CONSTRAINT fk_History_Accounts
	FOREIGN KEY (userEmail)
	REFERENCES Accounts (email);

insert into Cars(name, color, year, category, price, quantity) values('Accord', 'White', 2019, 'HONDA', 17, 20)
insert into Cars(name, color, year, category, price, quantity) values('Camry', 'Red, Black, Orange', 2021, 'TOYOTA', 20, 7)
insert into Cars(name, color, year, category, price, quantity) values('Civic', 'White', 2021, 'HONDA', 18, 15)
insert into Cars(name, color, year, category, price, quantity) values('Corolla Altis', 'White', 2019, 'TOYOTA', 25, 5)
insert into Cars(name, color, year, category, price, quantity) values('EcoSport', 'Red, Black', 2020, 'FORD', 13, 20)
insert into Cars(name, color, year, category, price, quantity) values('Everest', 'White, Black, Orange, Blue', 2020, 'FORD', 28, 3)
insert into Cars(name, color, year, category, price, quantity) values('Vios', 'Red, Black', 2019, 'TOYOTA', 12, 12)


insert into Discounts(id, percentOfDiscount, dateFrom, dateTo) values('test', 20, '2021-01-01', '2021-05-05')
insert into Discounts(id, percentOfDiscount, dateFrom, dateTo) values('km', 30, '2021-05-05', '2021-07-07')