package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Seat;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import static com.eltonquek.showbooking.utils.Constants.PHONE_NUMBER_REGEX;
import static com.eltonquek.showbooking.utils.Constants.SEAT_NUMBER_LIST_REGEX;

@Component
public class BookCommand implements Command {

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean validate(String[] inputs) {
        // Book <Show Number> <Phone#> <Comma separated list of seats>
        if (inputs.length != 4 || !Objects.equals(inputs[0], "Book")) {
            return false;
        }

        try {
            int showNumber = Integer.parseInt(inputs[1]);
            String phoneNumber = inputs[2];
            String stringOfSeats = inputs[3];

            if (memory.getShowList().stream().map(Show::getShowNumber).noneMatch(currentShowNumber -> currentShowNumber == showNumber)) {
                System.out.println("The show with show number: " + showNumber + " does not exist.");
                return false;
            }
            Optional<Show> showOptional = memory.getShowList().stream().filter(currentShow -> currentShow.getShowNumber() == showNumber).findFirst();
            if (!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
                System.out.println("The phone number: " + phoneNumber + " is invalid.");
                return false;
            }
            if (!Pattern.matches(SEAT_NUMBER_LIST_REGEX, stringOfSeats)) {
                System.out.println("The list of seats: " + stringOfSeats + " is invalid.");
                return false;
            }
            if (showOptional.isPresent()) {
                Show show = showOptional.get();
                // Only one booking per phone# is allowed per show.
                if (memory.getBookingList().stream().map(Booking::getPhoneNumber).anyMatch(currentPhoneNumber -> Objects.equals(currentPhoneNumber, phoneNumber))) {
                    System.out.println("The phone number: " + phoneNumber + " has already made a booking for show number: " + showNumber);
                    return false;
                }
                // Check that seats have not been booked
                String[] seatsNumberArray = stringOfSeats.split(",");
                List<String> availableSeatNumbers = show.getSeatList().stream().filter(seat -> !seat.isBooked()).map(Seat::getSeatNumber).toList();
                StringJoiner unavailableSeats = new StringJoiner(", ");
                for (String seatNumber : seatsNumberArray) {
                    if (availableSeatNumbers.stream().noneMatch(asn -> Objects.equals(seatNumber, asn))) {
                        unavailableSeats.add(seatNumber);
                    }
                }
                if (unavailableSeats.length() > 0) {
                    System.out.println("The seats: " + unavailableSeats + " is unavailable for show number:" + showNumber);
                    return false;
                }

            }

        } catch (NumberFormatException nfe) {
            System.out.println("The digits in your command are invalid.");
            return false;
        }

        return true;
    }

    @Override
    public void run(String[] inputs) {
        // (To book a ticket. This must generate a unique ticket # and display)

        int showNumber = Integer.parseInt(inputs[1]);
        String phoneNumber = inputs[2];
        String stringOfSeats = inputs[3];
        String[] seatsNumberArray = stringOfSeats.split(",");

        Optional<Show> showOptional = memory.getShowList().stream().filter(currentShow -> currentShow.getShowNumber() == showNumber).findFirst();
        if (showOptional.isPresent()) {
            Show show = showOptional.get();
            List<Seat> selectedSeats = show.getSeatList().stream()
                    .filter(seat -> Arrays.stream(seatsNumberArray).anyMatch(sna -> Objects.equals(seat.getSeatNumber(), sna))).toList();
            selectedSeats.stream().forEach(seat -> seat.setBooked(true));
            int ticketNumber = memory.getTicketNumberCounter() + 1;
            memory.setTicketNumberCounter(ticketNumber);
            Booking booking = new Booking(showNumber, ticketNumber, phoneNumber, selectedSeats);
            memory.getBookingList().add(booking);
            System.out.println("Booking successful! Your unique ticket number: " + ticketNumber);
        } else {
            throw new RuntimeException("Show not found.");
        }
    }
}
