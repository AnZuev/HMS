-- Hotel-34. Добавить в ответ на запрос /client/rooms/getAvailableRooms поле cost
ALTER TABLE ROOM_TYPES DROP COLUMN COST;
ALTER TABLE ROOMS ADD COST DECIMAL(8,2) NOT NULL;