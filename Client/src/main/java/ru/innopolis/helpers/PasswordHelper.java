/**
 * Создано: Денис 
 * Дата: 04.01.15
 * Описание: Класс-помошник для работы с паролями
 */
package ru.innopolis.helpers;

import java.security.MessageDigest;

public class PasswordHelper {

    /**
     * Шифрование пароля
     *
     * @param password Пароль для шифрования
     * @return Зашифрованный пароль
     */
    public static String encrypt(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteData) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        password = hexString.toString();
        return password;
    }

}
