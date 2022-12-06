package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Show;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = "test")
public class AvailabilityCommandTest {

    @Autowired
    private AvailabilityCommand uut;

    @Autowired
    private SystemMemory memory;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        Show show1 = new Show(1, 2, 2, 1);
        memory.getShowList().add(show1);

        Show show2 = new Show(2, 2, 2, 1);
        show2.getSeatList().get(0).setBooked(true);
        show2.getSeatList().get(1).setBooked(true);
        memory.getShowList().add(show2);

        Show show6 = new Show(6, 2, 2, 1);
        show6.getSeatList().get(0).setBooked(true);
        show6.getSeatList().get(1).setBooked(true);
        show6.getSeatList().get(2).setBooked(true);
        show6.getSeatList().get(3).setBooked(true);
        memory.getShowList().add(show6);

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        memory.getShowList().clear();
        memory.getBookingList().clear();
        memory.setTicketNumberCounter(0);
        System.setOut(originalOut);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Availability 1", "Availability 2"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Availability 1 1", "Availability", "Vie 1", "Availability a", "Availability 3"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void run_viewShow1Availability() {
        String[] inputArray = new String[]{"Availability", "1"};
        uut.run(inputArray);
        assertEquals(show1test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    @Test
    void run_viewShow2Availability() {
        String[] inputArray = new String[]{"Availability", "2"};
        uut.run(inputArray);
        assertEquals(show2test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    @Test
    void run_viewShow6Availability() {
        String[] inputArray = new String[]{"Availability", "6"};
        uut.run(inputArray);
        assertEquals(show6test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    private String show1test() {
        return """
                Show number: 1.
                Available seats: A1, A2, B1, B2
                """;
    }

    private String show2test() {
        return """
                Show number: 2.
                Available seats: B1, B2
                """;
    }

    private String show6test() {
        return """
                Show number: 6.
                No seats are available for this show.
                """;
    }
}
