package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Seat;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import static com.eltonquek.showbooking.utils.CommandUtils.getShowFromMemory;
import static com.eltonquek.showbooking.utils.CommandUtils.showNumberDoesNotExist;
import static com.eltonquek.showbooking.utils.Constants.*;

@Component
public class BookCommand extends Command {

    @Override
    String key() {
        return "Book";
    }

    @Override
    int numberOfInputs() {
        return 4;
    }

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean commandValidation(String[] inputs) {
        // Book <Show Number> <Phone#> <Comma separated list of seats>
        try {
            int showNumber = Integer.parseInt(inputs[1]);
            String phoneNumber = inputs[2];
            String stringOfSeats = inputs[3];

            // Check that show exists
            if (showNumberDoesNotExist(memory, showNumber)) {
                System.out.format(SHOW_NUMBER_DOES_NOT_EXIST_FORMAT, showNumber);
                return false;
            }

            // Check that phone number is valid
            if (!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
                System.out.format("The phone number: %s is invalid.%n", phoneNumber);
                return false;
            }

            // Check that seat list string is valid
            if (!Pattern.matches(SEAT_NUMBER_LIST_REGEX, stringOfSeats)) {
                System.out.format("The list of seats: %s is invalid.%n", stringOfSeats);
                return false;
            }

            Show show = getShowFromMemory(memory, showNumber);
            // Check that phone# is unique for this show.
            if (memory.getBookingList().stream().map(Booking::getPhoneNumber).anyMatch(currentPhoneNumber -> Objects.equals(currentPhoneNumber, phoneNumber))) {
                System.out.format("The phone number: %s has already made a booking for show number: %d%n", phoneNumber, showNumber);
                return false;
            }

            // Check that seats have not been booked
            String[] seatsNumberArray = stringOfSeats.split(COMMA);
            List<String> availableSeatNumbers = show.getSeatList().stream().filter(seat -> !seat.isBooked()).map(Seat::getSeatNumber).toList();
            StringJoiner unavailableSeats = new StringJoiner(", ");
            for (String seatNumber : seatsNumberArray) {
                // Place unavailable seats in StringJoiner to print later
                if (availableSeatNumbers.stream().noneMatch(asn -> Objects.equals(seatNumber, asn))) {
                    unavailableSeats.add(seatNumber);
                }
            }
            if (unavailableSeats.length() > 0) {
                System.out.format("The seats: %s are unavailable for show number: %d%n", unavailableSeats, showNumber);
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
        // (To book a ticket. This must generate a unique ticket # and display)

        int showNumber = Integer.parseInt(inputs[1]);
        String phoneNumber = inputs[2];
        String stringOfSeats = inputs[3];
        String[] seatsNumberArray = stringOfSeats.split(COMMA);

        Show show = getShowFromMemory(memory, showNumber);

        // Set selected seats to booked
        List<Seat> selectedSeats = show.getSeatList().stream()
                .filter(seat -> Arrays.stream(seatsNumberArray).anyMatch(sna -> Objects.equals(seat.getSeatNumber(), sna))).toList();
        selectedSeats.stream().forEach(seat -> seat.setBooked(true));

        // Create booking
        int ticketNumber = memory.getTicketNumberCounter() + 1;
        memory.setTicketNumberCounter(ticketNumber);
        Booking booking = new Booking(showNumber, ticketNumber, phoneNumber, selectedSeats);
        memory.getBookingList().add(booking);

        // Print success message
        System.out.println("Booking successful! Your unique ticket number: " + ticketNumber);

    }
}
