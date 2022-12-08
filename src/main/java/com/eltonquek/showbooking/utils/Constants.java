package com.eltonquek.showbooking.utils;

public class Constants {

    public static String PHONE_NUMBER_REGEX = "\\d{8}";
    public static String SEAT_NUMBER_LIST_REGEX = "([A-Z]([1-9]|10))(,[A-Z]([1-9]|10))*$";
    public static String COMMA = ",";

    public static String SHOW_NUMBER_DOES_NOT_EXIST_FORMAT = "The show with show number: %d does not exist.%n";
    public static String SHOW_NUMBER_EXIST_FORMAT = "The show with show number: %d already exist.%n";
    public static String BOOKING_DOES_NOT_EXIST_FORMAT = "The booking with ticket number: %d and phone number: %s does not exist.%n";
    public static String SHOW_NUMBER_FORMAT = "Show number: %d.%n";


    public static String DIGITS_INVALID_MESSAGE = "The digits in your command are invalid.";
}
