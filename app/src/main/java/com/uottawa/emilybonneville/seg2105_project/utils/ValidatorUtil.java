package com.uottawa.emilybonneville.seg2105_project.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern VALID_EMAIL_ADDRESS_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9-.]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z][-',.]?[a-zA-Z]{1,25}");


    public static boolean validateEmail(String emailStr) {
        if(emailStr==null){
            return false;
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_PATTERN.matcher(emailStr);
        return matcher.find();
    }

    public static Boolean validateName(String name) {
        if(name==null){
            return false; }
        Matcher m = VALID_NAME_PATTERN.matcher(name);
        return (m.matches());
    }

    public static boolean validatePostalCode(String postalCode) {
        Pattern p = Pattern.compile("^[#.0-9a-zA-Z\\s,-]{3,}$");
        Matcher m = p.matcher(postalCode);
        return (m.matches());
    }

    public static boolean validateDateFormat(String date) {
        if(date==null){
            return false; }
        Pattern p = Pattern.compile("[1-9][0-9]{3}/[01][0-9]/[0-3][0-9]");
        Matcher m = p.matcher(date);
        return (m.matches());
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() > 5;
    }

    public static boolean validateNumber(String numberText){
        if(numberText==null){
            return false; }
        Pattern p = Pattern.compile("^\\d{9,12}$"); //number has to be 9-12 digits
        Matcher m = p.matcher(numberText);
        return (m.matches());
    }

}
