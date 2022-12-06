package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Seat;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AvailabilityCommand implements Command {

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean validate(String[] inputs) {
        // Availability  <Show Number>
        if (inputs.length != 2 || !Objects.equals(inputs[0], "Availability")) {
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
        // (To list all available seat numbers for a show. E,g A1, F4 etc)

        int showNumber = Integer.parseInt(inputs[1]);
        Optional<Show> showOptional = memory.getShowList().stream().filter(currentShow -> currentShow.getShowNumber() == showNumber).findFirst();
        if (showOptional.isPresent()) {
            Show show = showOptional.get();
            System.out.printf("Show number: %d.\n", showNumber);

            List<Seat> availableSeatList = show.getSeatList().stream().filter(seat -> !seat.isBooked()).toList();

            if (availableSeatList.size() == 0) {
                System.out.println("No seats are available for this show.");
            } else {
                String seatList = availableSeatList.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
                System.out.println("Available seats: " + seatList);
            }
        } else {
            throw new RuntimeException("Show not found.");
        }
    }
}
