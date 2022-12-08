package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.eltonquek.showbooking.utils.CommandUtils.showNumberExist;
import static com.eltonquek.showbooking.utils.Constants.DIGITS_INVALID_MESSAGE;
import static com.eltonquek.showbooking.utils.Constants.SHOW_NUMBER_EXIST_FORMAT;

@Component
public class SetupCommand extends Command {

    @Override
    String key() {
        return "Setup";
    }

    @Override
    int numberOfInputs() {
        return 5;
    }

    @Autowired
    private SystemMemory memory;

    @Override
    public boolean commandValidation(String[] inputs) {
        // Setup <Show Number> <Number of Rows> <Number of seats per row> <Cancellation window in minutes>
        try {
            int showNumber = Integer.parseInt(inputs[1]);
            int rows = Integer.parseInt(inputs[2]);
            int columns = Integer.parseInt(inputs[3]);
            int cancellationWindow = Integer.parseInt(inputs[4]);

            // Check that show does not exist
            if (showNumberExist(memory, showNumber)) {
                System.out.format(SHOW_NUMBER_EXIST_FORMAT, showNumber);
                return false;
            }
            // Check that row number is valid (1 <= n <= 26)
            if (rows > 26 || rows < 1) {
                System.out.println("Number of Rows must be a number between 1 to 26.");
                return false;
            }
            // Check that number of seats per row is valid (1 <= n <= 10)
            if (columns > 10 || columns < 1) {
                System.out.println("Number of seats per row must be a number between 1 to 10.");
                return false;
            }
            // Check that cancellation window is not a negative number
            if (cancellationWindow < 0) {
                System.out.println("Cancellation window in minutes cannot be a negative number.");
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
        int showNumber = Integer.parseInt(inputs[1]);
        int rows = Integer.parseInt(inputs[2]);
        int columns = Integer.parseInt(inputs[3]);
        int cancellationWindow = Integer.parseInt(inputs[4]);

        memory.getShowList().add(new Show(showNumber, rows, columns, cancellationWindow));
        System.out.format("Show number %d created!%n", showNumber);
    }
}
