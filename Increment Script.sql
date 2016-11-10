-- Hotel-34. Добавить в ответ на запрос /client/rooms/getAvailableRooms поле cost
ALTER TABLE ROOMS DROP COLUMN COST;
ALTER TABLE ROOM_TYPES ADD COST DECIMAL(8,2);