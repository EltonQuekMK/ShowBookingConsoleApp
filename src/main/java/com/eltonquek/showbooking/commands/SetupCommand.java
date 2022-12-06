package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SetupCommand implements Command {

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean validate(String[] inputs) {
        // Setup <Show Number> <Number of Rows> <Number of seats per row> <Cancellation window in minutes>
        if (inputs.length != 5 || !Objects.equals(inputs[0], "Setup")) {
            return false;
        }

        try {
            int showNumber = Integer.parseInt(inputs[1]);
            int rows = Integer.parseInt(inputs[2]);
            int columns = Integer.parseInt(inputs[3]);
            int cancellationWindow = Integer.parseInt(inputs[4]);

            if (memory.getShowList().stream().map(Show::getShowNumber).anyMatch(currentShowNumber -> currentShowNumber == showNumber)) {
                System.out.println("A show with show number: " + showNumber + " already exists.");
                return false;
            }
            if (rows > 26 || rows < 1) {
                System.out.println("Number of Rows must be a number between 1 to 26.");
                return false;
            }
            if (columns > 10 || columns < 1) {
                System.out.println("Number of seats per row must be a number between 1 to 10.");
                return false;
            }
            if (cancellationWindow < 0) {
                System.out.println("Cancellation window in minutes cannot be a negative number.");
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
        int showNumber = Integer.parseInt(inputs[1]);
        int rows = Integer.parseInt(inputs[2]);
        int columns = Integer.parseInt(inputs[3]);
        int cancellationWindow = Integer.parseInt(inputs[4]);

        memory.getShowList().add(new Show(showNumber, rows, columns, cancellationWindow));
        System.out.printf("Show number %d created.\n", showNumber);
    }
}
