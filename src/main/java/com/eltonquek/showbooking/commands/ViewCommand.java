package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.eltonquek.showbooking.utils.CommandUtils.showNumberDoesNotExist;
import static com.eltonquek.showbooking.utils.Constants.*;

@Component
public class ViewCommand extends Command {

    @Override
    String key() {
        return "View";
    }

    @Override
    int numberOfInputs() {
        return 2;
    }

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean commandValidation(String[] inputs) {
        // View <Show Number>
        try {
            int showNumber = Integer.parseInt(inputs[1]);

            // Check that show exists
            if (showNumberDoesNotExist(memory, showNumber)) {
                System.out.format(SHOW_NUMBER_DOES_NOT_EXIST_FORMAT, showNumber);
                return false;
            }
        } catch (NumberFormatException nfe) {
            System.out.println(DIGITS_INVALID_MESSAGE);
            return false;
        }

        return true;
    }

    @Override
    public void run(String[] inputs) {
        // (To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)

        int showNumber = Integer.parseInt(inputs[1]);
        System.out.format(SHOW_NUMBER_FORMAT, showNumber);

        List<Booking> bookingList = memory.getBookingList().stream().filter(booking -> booking.getShowNumber() == showNumber).toList();
        if (bookingList.size() == 0) {
            System.out.println("No bookings have been found for this show.");
        } else {
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.format("%18s %25s %35s", "Ticket Number", "Buyer Phone Number", "Seat Numbers");
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------");
            // Iterates over the booking list
            for (Booking booking : bookingList) {
                String seatList = booking.getSeatList().stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
                System.out.format("%18s %25s %35s", booking.getTicketNumber(), booking.getPhoneNumber(), seatList);
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------------");
        }
    }
}
