package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Seat;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.eltonquek.showbooking.utils.CommandUtils.getShowFromMemory;
import static com.eltonquek.showbooking.utils.CommandUtils.showNumberDoesNotExist;
import static com.eltonquek.showbooking.utils.Constants.*;

@Component
public class AvailabilityCommand extends Command {

    @Autowired
    private SystemMemory memory;

    @Override
    String key() {
        return "Availability";
    }

    @Override
    int numberOfInputs() {
        return 2;
    }

    @Override
    public boolean commandValidation(String[] inputs) {
        // Availability  <Show Number>
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
        // (To list all available seat numbers for a show. E,g A1, F4 etc)

        int showNumber = Integer.parseInt(inputs[1]);

        Show show = getShowFromMemory(memory, showNumber);
        System.out.format(SHOW_NUMBER_FORMAT, showNumber);

        List<Seat> availableSeatList = show.getSeatList().stream().filter(seat -> !seat.isBooked()).toList();

        if (availableSeatList.size() == 0) {
            System.out.println("No seats are available for this show.");
        } else {
            String seatList = availableSeatList.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", "));
            System.out.println("Available seats: " + seatList);
        }

    }
}
