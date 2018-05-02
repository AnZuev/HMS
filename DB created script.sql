DROP TABLE ORDERS;
DROP TABLE ROOMS;
DROP TABLE ROOM_TYPES;
DROP TABLE CLIENTS;
DROP TABLE EMPLOYEES;
DROP TABLE HOTELS;
DROP TABLE STAFFS;

DROP SEQUENCE CLIENTS_SEQ;
DROP SEQUENCE ROOM_TYPES_SEQ;
DROP SEQUENCE ROOMS_SEQ;
DROP SEQUENCE ORDERS_SEQ;
DROP SEQUENCE STAFFS_SEQ;
DROP SEQUENCE HOTELS_SEQ;
DROP SEQUENCE EMPLOYEES_SEQ;

CREATE SEQUENCE CLIENTS_SEQ;
CREATE SEQUENCE ROOM_TYPES_SEQ;
CREATE SEQUENCE ROOMS_SEQ;
CREATE SEQUENCE ORDERS_SEQ;
CREATE SEQUENCE STAFFS_SEQ;
CREATE SEQUENCE HOTELS_SEQ;
CREATE SEQUENCE EMPLOYEES_SEQ;

CREATE TABLE CLIENTS (
  ID NUMBER,
  FIRST_NAME VARCHAR2(50 CHAR) NOT NULL,
  SECOND_NAME VARCHAR2(50 CHAR) NOT NULL,
  FATHER_NAME VARCHAR2(50 CHAR),
  EMAIL VARCHAR2(50 CHAR) NOT NULL,
  PHONE_NUMBER VARCHAR2(50 CHAR),
  HASH_PASSWORD VARCHAR2(100 CHAR) NOT NULL,
  CONSTRAINT CLIENTS_PK PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX CLIENT_EMAIL_INDEX ON CLIENTS(EMAIL);

CREATE TABLE ROOM_TYPES (
  ID NUMBER,
  STATUS VARCHAR2(20 CHAR) NOT NULL,
  HOTEL_ID NUMBER NOT NULL,
  TYPE_NAME VARCHAR2(100 CHAR) NOT NULL,
  DESCRIPTION VARCHAR2(500 CHAR),
  COST DECIMAL(8,2) NOT NULL CHECK (COST >= 0),
  PATH_TO_PHOTO VARCHAR2(256 CHAR),
  CONSTRAINT ROOMTYPES_PK PRIMARY KEY (ID)
);

CREATE INDEX ROOM_TYPES_HOTEL_ID_INDEX ON ROOM_TYPES(HOTEL_ID);

CREATE TABLE ROOMS (
  ID NUMBER,
  ROOM_NUMBER NUMBER NOT NULL,
  HOTEL_ID NUMBER NOT NULL,
  ROOM_TYPE_ID NUMBER NOT NULL,
  STATUS VARCHAR2(20),
  CONSTRAINT ROOMS_PK PRIMARY KEY (ID)
);

CREATE INDEX ROOMS_HOTEL_ID_INDEX ON ROOMS(HOTEL_ID);

CREATE TABLE ORDERS (
  ID NUMBER,
  CLIENT_ID NUMBER NOT NULL,
  HOTEL_ID NUMBER NOT NULL,
  ROOM_ID NUMBER NOT NULL,
  START_DATE DATE NOT NULL,
  FINISH_DATE DATE NOT NULL,
  STATUS VARCHAR2(20 CHAR),
  COST DECIMAL(8,2) CHECK (COST >= 0),
  CONSTRAINT ORDERS_PK PRIMARY KEY (ID),
  CONSTRAINT START_IS_LESS_FINISH_CHECK CHECK (START_DATE < FINISH_DATE)
);

CREATE INDEX ORDER_HOTEL_ID_INDEX ON ORDERS(HOTEL_ID);
CREATE INDEX CLIENT_ORDERS_INDEX ON ORDERS(CLIENT_ID);

CREATE TABLE EMPLOYEES (
  ID NUMBER,
  EMAIL VARCHAR2(50 CHAR) NOT NULL,
  HASH_PASSWORD VARCHAR2(100 CHAR) NOT NULL,
  FIRST_NAME VARCHAR2(50 CHAR) NOT NULL,
  SECOND_NAME VARCHAR2(50 CHAR) NOT NULL,
  FATHER_NAME VARCHAR2(50 CHAR),
  HOTEL_ID NUMBER NOT NULL,
  TYPE VARCHAR2(50 CHAR) NOT NULL,
  CONSTRAINT EMPLOYEES_PK PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX EMPLOYEES_EMAIL_INDEX ON EMPLOYEES(EMAIL);
CREATE INDEX  EMPLOYEES_HOTEL_ID_INDEX ON EMPLOYEES(HOTEL_ID);

CREATE TABLE STAFFS (
  ID NUMBER,
  EMAIL VARCHAR2(50 CHAR) NOT NULL,
  HASH_PASSWORD VARCHAR2(100 CHAR) NOT NULL,
  FIRST_NAME VARCHAR2(50 CHAR) NOT NULL,
  SECOND_NAME VARCHAR2(50 CHAR) NOT NULL,
  FATHER_NAME VARCHAR2(50 CHAR),
  CONSTRAINT STAFF_PK PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX STAFF_EMAIL_INDEX ON STAFFS(EMAIL);


CREATE TABLE HOTELS (
  ID NUMBER,
  NAME VARCHAR2(200 CHAR) NOT NULL,
  DESCRIPTION VARCHAR2(400 CHAR),
  PHONE_NUMBER VARCHAR2(50 CHAR) NOT NULL,
  ADDRESS VARCHAR2(200 CHAR) NOT NULL,
  EMAIL VARCHAR2(50 CHAR) NOT NULL,
  CONSTRAINT HOTELS_PK PRIMARY KEY (ID)
);


ALTER TABLE ROOM_TYPES ADD CONSTRAINT ROOMTYPES_FK0 FOREIGN KEY (HOTEL_ID) REFERENCES HOTELS(ID);

ALTER TABLE ROOMS ADD CONSTRAINT ROOMS_FK0 FOREIGN KEY (HOTEL_ID) REFERENCES HOTELS(ID);
ALTER TABLE ROOMS ADD CONSTRAINT ROOMS_FK1 FOREIGN KEY (ROOM_TYPE_ID) REFERENCES ROOM_TYPES(ID);

ALTER TABLE ORDERS ADD CONSTRAINT ORDERS_FK0 FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID);
ALTER TABLE ORDERS ADD CONSTRAINT ORDERS_FK1 FOREIGN KEY (HOTEL_ID) REFERENCES HOTELS(ID);
ALTER TABLE ORDERS ADD CONSTRAINT ORDERS_FK2 FOREIGN KEY (ROOM_ID) REFERENCES ROOMS(ID);

ALTER TABLE EMPLOYEES ADD CONSTRAINT EMPLOYEES_FK0 FOREIGN KEY (HOTEL_ID) REFERENCES HOTELS(ID);