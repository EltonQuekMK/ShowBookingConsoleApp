package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    private SystemMemory memory;

    @AfterEach
    void tearDown() {
        memory.getShowList().clear();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Setup 1 1 1 1", "Setup 2 26 1 1", "Setup 2 26 1 1", "Setup 3 1 10 1", "Setup 4 26 10 1"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        Assertions.assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Setup 1 0 1 1", "Setup 1 1 0 1", "Setup 1 27 1 1", "Setup 1 1 11 1", "Setup 1 a 1 1",
            "Setu 1 1 1 1", "Setup1 1 1 1 1", "Setup 1 1 1", "Setup 1 1", "Setup 1", "Setup"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        Assertions.assertFalse(uut.validate(inputArray));
    }

    @Test
    void validate_returnsFalse_existingShowNumber() {
        uut.run(new String[]{"Setup", "1", "1", "1", "1"});
        Assertions.assertFalse(uut.validate(new String[]{"Setup", "1", "1", "1", "1"}));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Setup 1 1 1 1", "Setup 2 26 1 1", "Setup 3 26 1 1", "Setup 4 1 10 1", "Setup 5 26 10 1"})
    void run_createShow(String input) {
        String[] inputArray = input.split(" ");
        uut.run(inputArray);
        Assertions.assertEquals(1, memory.getShowList().size());
    }
}
