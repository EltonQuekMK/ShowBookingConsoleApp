package com.eltonquek.showbooking.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args = "test")
public class SetupCommandTest {

    @Autowired
    private SetupCommand uut;

    //    To start, please setup a new show with the command \"Setup <Show Number> <Number of Rows> <Number of seats per row> <Cancellation window in minutes>\"."
    @ParameterizedTest
    @ValueSource(strings = {"Setup 1 1 1 1", "Setup 2 26 1 1", "Setup 2 26 1 1", "Setup 3 1 10 1", "Setup 4 26 10 1"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        Assertions.assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Setup 1 0 1 1", "Setup 1 1 0 1", "Setup 1 27 1 1", "Setup 1 1 11 1",
            "Setu 1 1 1 1", "Setup1 1 1 1 1", "Setup 1 1 1", "Setup 1 1", "Setup 1", "Setup"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        Assertions.assertFalse(uut.validate(inputArray));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"Setup 1 1 1 1", "Setup 2 26 1 1", "Setup 2 26 1 1", "Setup 3 1 10 1", "Setup 4 26 10 1"})
//    void run_createShow(String input){
//        String[] inputArray = input.split(" ");
//        Assertions.assertEquals();
//    }
}
