package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Seat;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ViewCommand implements Command {

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean validate(String[] inputs) {
        // View <Show Number>
        if (inputs.length != 2 || !Objects.equals(inputs[0], "View")) {
            return false;
        }

        try {
            int showNumber = Integer.parseInt(inputs[1]);
            if (memory.getShowList().stream().map(Show::getShowNumber).noneMatch(currentShowNumber -> currentShowNumber == showNumber)) {
                System.out.println("The show with show number: " + showNumber + " does not exist.");
                return false;
            }
        } catch (NumberFormatException nfe) {
            System.out.println("The digits in your command are invalid.");
            return false;
        }

        return true;
    }

    @Override
    public void run(String[] inputs) {
        // (To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)

        int showNumber = Integer.parseInt(inputs[1]);
        Optional<Show> showOptional = memory.getShowList().stream().filter(currentShow -> currentShow.getShowNumber() == showNumber).findFirst();
        if (showOptional.isPresent()) {
            Show show = showOptional.get();
            System.out.printf("Show number: %d.\n", showNumber);

            List<Booking> bookingList = memory.getBookingList().stream().filter(booking -> booking.getShowNumber() == showNumber).toList();
            if (bookingList.size() == 0) {
                System.out.println("No bookings have been found for this show.");
            } else {
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.printf("%18s %25s %35s", "Ticket Number", "Buyer Phone Number", "Seat Numbers");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------------------------");
                //iterates over the list
                for (Booking booking : bookingList) {
                    String seatList = booking.getSeatList().stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
                    System.out.format("%18s %25s %35s", booking.getTicketNumber(), booking.getPhoneNumber(), seatList);
                    //TODO: Beautify seat list when seat list is big
                    System.out.println();
                }
                System.out.println("---------------------------------------------------------------------------------------------");
            }
        } else {
            throw new RuntimeException("Show not found.");
        }
    }
}
