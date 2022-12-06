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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = "test")
public class CancelCommandTest {

    @Autowired
    private CancelCommand uut;

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
        Booking booking1 = new Booking(2, 123, "88888888",
                List.of(show2.getSeatList().get(0)));
        Booking booking2 = new Booking(2, 124, "12345678",
                List.of(show2.getSeatList().get(0)));
        booking2.setCreatedDateTime(LocalDateTime.now().minusMinutes(10));
        memory.getBookingList().add(booking1);
        memory.getBookingList().add(booking2);
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
    @ValueSource(strings = {"Cancel 123 88888888"})
    void validate_returnsTrue_validInput(String input) {
        String[] inputArray = input.split(" ");
        assertTrue(uut.validate(inputArray));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Cancel 123 8888", "Cancel 1234 88888888", "Cancel 123 88888888 1", "Cancel 123", "Cancel 123 8888888a",
            "Cancel 12a 88888888", "Can 123 8888888a"})
    void validate_returnsFalse_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void validate_returnsFalse_bookingTimingMoreThanCancellationWindow() {
        String[] inputArray = new String[]{"Cancel", "124", "12345678"};
        assertFalse(uut.validate(inputArray));
    }

    @Test
    void run_cancelBooking() {
        String[] inputArray = new String[]{"Cancel", "123", "88888888"};
        int initialBookingSize = memory.getBookingList().size();

        uut.run(inputArray);

        List<Seat> show2SeatList = memory.getShowList().get(1).getSeatList();
        assertFalse(show2SeatList.get(0).isBooked());
        assertTrue(show2SeatList.get(1).isBooked());
        assertFalse(show2SeatList.get(2).isBooked());
        assertFalse(show2SeatList.get(3).isBooked());
        assertEquals(initialBookingSize - 1, memory.getBookingList().size());
    }
}
