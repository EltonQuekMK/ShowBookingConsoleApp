package com.eltonquek.showbooking.commands;

import com.eltonquek.showbooking.SystemMemory;
import com.eltonquek.showbooking.entities.Booking;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = "test")
public class BookCommandTest {

    @Autowired
    private BookCommand uut;

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
        Booking booking = new Booking(2, 123, "88888888",
                List.of(show2.getSeatList().get(0), show2.getSeatList().get(1)));
        memory.getBookingList().add(booking);
        memory.getShowList().add(show2);

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
    @ValueSource(strings = {"Book 1 12345678 A1", "Book 1 12345678 A1,A2", "Book 1 12345678 A1,A2,B2", "Book 2 12345678 B1", "Book 1 87654321 A1"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Book 1 1234678 A1", "Book 1 12345678 A1,A", "Book 1 1234567 A1,A2,A3", "Book a 12345678 A1", "Book 1 a8654321 A1",
            "Boo 1 1234678 A1", "Book 1 12345678", "Book 1 12345678 A1,A2,a3", "Book 1 12345678 Aa", "Book 1 18654321 a1"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void validate_returnsFalse_repeatedPhoneNumber() {
        String[] inputArray = new String[]{"Book", "2", "88888888", "B1"};
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void validate_returnsFalse_seatUnavailable() {
        String[] inputArray = new String[]{"Book", "2", "12345678", "A1"};
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void run_bookShow1() {
        String[] inputArray = new String[]{"Book", "1", "12345678", "A1"};
        int initialBookingSize = memory.getBookingList().size();
        uut.run(inputArray);

        Booking latestBooking = memory.getBookingList().get(1);

        assertEquals("12345678", latestBooking.getPhoneNumber());
        assertEquals(1, latestBooking.getTicketNumber());
        assertEquals(1, latestBooking.getShowNumber());
        assertEquals(1, latestBooking.getSeatList().size());
        assertEquals(initialBookingSize + 1, memory.getBookingList().size());
        assertEquals(show1test().trim(), outContent.toString().trim().replace("\r", ""));
    }

    @Test
    void run_checkTicketNumberIsUnique_bookShow2() {
        String[] inputArray = new String[]{"Book", "2", "12345678", "B1"};
        String[] inputArray2 = new String[]{"Book", "2", "43215678", "B2"};
        int initialBookingSize = memory.getBookingList().size();
        uut.run(inputArray);
        uut.run(inputArray2);

        Booking booking1 = memory.getBookingList().get(1);
        Booking booking2 = memory.getBookingList().get(2);

        assertEquals("12345678", booking1.getPhoneNumber());
        assertEquals(1, booking1.getTicketNumber());
        assertEquals(2, booking1.getShowNumber());
        assertEquals(1, booking1.getSeatList().size());

        assertEquals("43215678", booking2.getPhoneNumber());
        assertEquals(2, booking2.getTicketNumber());
        assertEquals(2, booking2.getShowNumber());
        assertEquals(1, booking2.getSeatList().size());

        assertEquals(initialBookingSize + 2, memory.getBookingList().size());
    }

    private String show1test() {
        return """
                Booking successful! Your unique ticket number: 1
                """;
    }
}
