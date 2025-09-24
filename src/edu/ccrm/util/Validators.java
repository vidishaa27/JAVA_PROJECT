package edu.ccrm.util;

public class Validators {
    public static boolean isEmailValid(String s) {
        return s != null && s.contains("@") && s.contains(".");
    }
}
