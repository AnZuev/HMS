-- Hotel-34. Добавить в ответ на запрос /client/rooms/getAvailableRooms поле cost
ALTER TABLE ROOMS DROP COLUMN COST;
ALTER TABLE ROOM_TYPES ADD COST DECIMAL(8,2);
-- Hotel-47. Реализовать удаление сущности "Тип комнаты" в модуле сотрудника
ALTER TABLE ROOM_TYPES ADD STATUS VARCHAR2(20 CHAR);
update ROOM_TYPES set STATUS = 'WORKED';
ALTER TABLE ROOM_TYPES MODIFY STATUS VARCHAR2(20 CHAR) NOT NULL;