package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.eltonquek.showbooking.utils.CommandUtils.*;
import static com.eltonquek.showbooking.utils.Constants.DIGITS_INVALID_MESSAGE;

@Component
public class CancelCommand extends Command {

    @Override
    String key() {
        return "Cancel";
    }

    @Override
    int numberOfInputs() {
        return 3;
    }

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean commandValidation(String[] inputs) {
        // Cancel  <Ticket#>  <Phone#>
        try {
            int ticketNumber = Integer.parseInt(inputs[1]);
            String phoneNumber = inputs[2];

            // Check that booking exists
            Optional<Booking> bookingOptional = getBookingOptionalFromMemory(memory, ticketNumber, phoneNumber);
            if (bookingOptional.isEmpty()) {
                System.out.format("The booking with ticket number: %d and phone number: %s does not exist.%n", ticketNumber, phoneNumber);
                return false;
            }

            // Check if booking in within cancellation window
            Booking booking = bookingOptional.get();
            Show show = getShowFromMemory(memory, booking.getShowNumber());
            int cancellationWindow = show.getCancellationValidityInMinutes();
            if (booking.getCreatedDateTime().isBefore(LocalDateTime.now().minusMinutes(cancellationWindow))) {
                System.out.format("The booking with ticket number: %d has exceeded the time window of %d minute(s).%n", ticketNumber, cancellationWindow);
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
        // (To cancel a ticket. See constraints in the section below)
        // After booking, User can cancel the seats within a time window of 2 minutes (configurable). Cancellation after that is not allowed.

        int ticketNumber = Integer.parseInt(inputs[1]);
        String phoneNumber = inputs[2];

        // Remove booking
        Booking booking = getBookingFromMemory(memory, ticketNumber, phoneNumber);
        memory.getBookingList().remove(booking);

        // Reset seats to unbooked
        booking.getSeatList().stream().forEach(seat -> seat.setBooked(false));

        //Print Message
        System.out.println("Cancellation successful!");
    }
}
