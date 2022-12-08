package com.eltonquek.showbooking.utils;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Show;

import java.util.Objects;
import java.util.Optional;

import static com.eltonquek.showbooking.utils.Constants.BOOKING_DOES_NOT_EXIST_FORMAT;
import static com.eltonquek.showbooking.utils.Constants.SHOW_NUMBER_DOES_NOT_EXIST_FORMAT;

public class CommandUtils {

    public static boolean showNumberDoesNotExist(SystemMemory memory, int showNumber) {
        return memory.getShowList().stream().map(Show::getShowNumber).noneMatch(currentShowNumber -> currentShowNumber == showNumber);
    }

    public static boolean showNumberExist(SystemMemory memory, int showNumber) {
        return memory.getShowList().stream().map(Show::getShowNumber).anyMatch(currentShowNumber -> currentShowNumber == showNumber);
    }

    public static Show getShowFromMemory(SystemMemory memory, int showNumber) {
        Optional<Show> showOptional = memory.getShowList().stream().filter(currentShow -> currentShow.getShowNumber() == showNumber).findFirst();
        if (showOptional.isPresent()) {
            return showOptional.get();
        } else {
            throw new RuntimeException(String.format(SHOW_NUMBER_DOES_NOT_EXIST_FORMAT, showNumber));
        }
    }

    public static Booking getBookingFromMemory(SystemMemory memory, int ticketNumber, String phoneNumber) {
        Optional<Booking> bookingOptional = getBookingOptionalFromMemory(memory, ticketNumber, phoneNumber);
        if (bookingOptional.isPresent()) {
            return bookingOptional.get();
        } else {
            throw new RuntimeException(String.format(BOOKING_DOES_NOT_EXIST_FORMAT, ticketNumber, phoneNumber));
        }
    }

    public static Optional<Booking> getBookingOptionalFromMemory(SystemMemory memory, int ticketNumber, String phoneNumber) {
        return memory.getBookingList().stream().filter(currentBooking ->
                currentBooking.getTicketNumber() == ticketNumber && Objects.equals(currentBooking.getPhoneNumber(), phoneNumber)).findFirst();
    }

}
