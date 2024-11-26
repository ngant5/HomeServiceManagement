package net.codejava.helper;

import java.util.regex.Pattern;

public class EmployeeValidation {

    private static final Pattern FULLNAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,50}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^.{1,100}$");
    private static final Pattern BIO_PATTERN = Pattern.compile("^.{0,255}$");

    public static boolean isFullnameValid(String fullname) {
        return FULLNAME_PATTERN.matcher(fullname).matches();
    }

    public static boolean isPhoneValid(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isAddressValid(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isBioValid(String bio) {
        return BIO_PATTERN.matcher(bio).matches();
    }

    // Add more validations as needed
}