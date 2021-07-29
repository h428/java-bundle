package com.demo.common.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static String email_pattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";


    public static boolean emailLegal(String email) {
        return Pattern.matches(email_pattern, email);
    }


    public static void main(String[] args) {
        System.out.println(emailLegal("aaa@111.com"));
    }

}
