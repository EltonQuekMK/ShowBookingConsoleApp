package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Seat;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = "test")
public class ViewCommandTest {

    @Autowired
    private ViewCommand uut;

    @Autowired
    private SystemMemory memory;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        Show show1 = new Show(1, 1, 1, 1);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(new Seat("A1"));
        memory.getBookingList().add(new Booking(1, 1234, "12341234", seatList));
        memory.getShowList().add(show1);

        memory.getShowList().add(new Show(2, 1, 1, 1));

        Show show6 = new Show(6, 1, 1, 1);
        List<Seat> seatList6 = new ArrayList<>();
        List<Seat> seatList7 = new ArrayList<>();
        seatList6.add(new Seat("A2"));
        seatList6.add(new Seat("B4"));
        seatList7.add(new Seat("C4"));
        seatList7.add(new Seat("D5"));
        memory.getBookingList().add(new Booking(6, 1235, "12341235", seatList6));
        memory.getBookingList().add(new Booking(6, 1236, "12341236", seatList7));
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

    @Test
    void run_viewShow1() {
        String[] inputArray = new String[]{"View", "1"};
        uut.run(inputArray);
        assertEquals(show1test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    @Test
    void run_viewShow2() {
        String[] inputArray = new String[]{"View", "2"};
        uut.run(inputArray);
        assertEquals(show2test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    @Test
    void run_viewShow6() {
        String[] inputArray = new String[]{"View", "6"};
        uut.run(inputArray);
        assertEquals(show6test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    private String show1test() {
        return """
                Show number: 1.
                ---------------------------------------------------------------------------------------------
                     Ticket Number        Buyer Phone Number                        Seat Numbers
                ---------------------------------------------------------------------------------------------
                              1234                  12341234                                  A1
                ---------------------------------------------------------------------------------------------
                """;
    }

    private String show2test() {
        return """
                Show number: 2.
                No bookings have been found for this show.
                """;
    }

    private String show6test() {
        return """
                Show number: 6.
                ---------------------------------------------------------------------------------------------
                     Ticket Number        Buyer Phone Number                        Seat Numbers
                ---------------------------------------------------------------------------------------------
                              1235                  12341235                              A2, B4
                              1236                  12341236                              C4, D5
                ---------------------------------------------------------------------------------------------
                """;
    }
}
