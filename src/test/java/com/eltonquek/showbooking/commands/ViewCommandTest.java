package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Show;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = "test")
public class ViewCommandTest {

    @Autowired
    private ViewCommand uut;

    @Autowired
    private SystemMemory memory;

    @BeforeEach
    void setup() {
        memory.getShowList().add(new Show(1, 1, 1, 1));
        memory.getShowList().add(new Show(2, 1, 1, 1));
    }

    @AfterEach
    void tearDown() {
        memory.getShowList().clear();
    }

    @ParameterizedTest
    @ValueSource(strings = {"View 1", "View 2"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"View 1 1", "View", "Vie 1", "View a", "View 3"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertFalse(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"View 1", "View 2"})
    void run_viewShow(String input) {
        String[] inputArray = input.split(" ");
        uut.run(inputArray);
        assertEquals(1, memory.getShowList().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"View 3", "View 4", "View 1 1"})
    void run_throwException_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertThrows(RuntimeException.class, () -> uut.run(inputArray));
    }
}
