package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class CancelCommand implements Command {

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean validate(String[] inputs) {
        // Cancel  <Ticket#>  <Phone#>
        if (inputs.length != 3 || !Objects.equals(inputs[0], "Cancel")) {
            return false;
        }

        try {
            int ticketNumber = Integer.parseInt(inputs[1]);
            String phoneNumber = inputs[2];
            Optional<Booking> bookingOptional = memory.getBookingList().stream().filter(currentBooking ->
                    currentBooking.getTicketNumber() == ticketNumber && Objects.equals(currentBooking.getPhoneNumber(), phoneNumber)).findFirst();
            if (bookingOptional.isEmpty()) {
                System.out.println("The booking with ticket number: " + ticketNumber + " and phone number: " + phoneNumber + " does not exist.");
                return false;
            } else {
                Booking booking = bookingOptional.get();
                Optional<Show> showOptional = memory.getShowList().stream().filter(show -> show.getShowNumber() == booking.getShowNumber()).findFirst();
                if (showOptional.isPresent()) {
                    int cancellationWindow = showOptional.get().getCancellationValidityInMinutes();
                    if (booking.getCreatedDateTime().isBefore(LocalDateTime.now().minusMinutes(cancellationWindow))) {
                        System.out.println("The booking with ticket number: " + ticketNumber + " has exceeded the time window of " + cancellationWindow + "minutes.");
                        return false;
                    }
                } else {
                    throw new RuntimeException("Show not found.");
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
        // (To cancel a ticket. See constraints in the section below)
        // After booking, User can cancel the seats within a time window of 2 minutes (configurable). Cancellation after that is not allowed.

        int ticketNumber = Integer.parseInt(inputs[1]);
        String phoneNumber = inputs[2];
        Optional<Booking> bookingOptional = memory.getBookingList().stream().filter(currentBooking ->
                currentBooking.getTicketNumber() == ticketNumber && Objects.equals(currentBooking.getPhoneNumber(), phoneNumber)).findFirst();
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            memory.getBookingList().remove(booking);
            booking.getSeatList().stream().forEach(seat -> seat.setBooked(false));
        }
    }
}
