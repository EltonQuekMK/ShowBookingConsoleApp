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
import java.util.Objects;

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
        List<Seat> seatList=  new ArrayList<>();
        seatList.add(new Seat("A1"));
        List<Booking> bookingList=  new ArrayList<>();
        bookingList.add(new Booking("1234", "1234", seatList));
        show1.setBookingList(bookingList);
        memory.getShowList().add(show1);


        Show show6 = new Show(6, 1, 1, 1);
        List<Seat> seatList6=  new ArrayList<>();
        seatList6.add(new Seat("A2"));
        seatList6.add(new Seat("B4"));
        seatList6.add(new Seat("C4"));
        seatList6.add(new Seat("D5"));
        List<Booking> bookingList6=  new ArrayList<>();
        bookingList6.add(new Booking("1234", "1234", seatList6));
        show6.setBookingList(bookingList6);
        memory.getShowList().add(show6);

        memory.getShowList().add(new Show(2, 1, 1, 1));

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        memory.getShowList().clear();
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
        assertTrue(Objects.equals(show1test().trim(), outContent.toString().trim().replace("\r","")));
    }

    @Test
    void run_viewShow2() {
        String[] inputArray = new String[]{"View", "2"};
        uut.run(inputArray);
        assertTrue(Objects.equals(show2test().trim(), outContent.toString().trim().replace("\r","")));
    }

    @Test
    void run_viewShow6() {
        String[] inputArray = new String[]{"View", "6"};
        uut.run(inputArray);
        assertTrue(Objects.equals(show6test().trim(), outContent.toString().trim().replace("\r","")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"View 3", "View 4", "View 1 1"})
    void run_throwException_invalidInput(String input) {
        String[] inputArray = input.split(" ");
        assertThrows(RuntimeException.class, () -> uut.run(inputArray));
    }

    private String show1test(){
        return "Show number: 1.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "     Ticket Number        Buyer Phone Number                        Seat Numbers\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "              1234                      1234                                  A1\n" +
                "---------------------------------------------------------------------------------------------\n";
    }

    private String show2test(){
        return "Show number: 2.\n" +
                "No bookings have been found for this show\n";
    }

    private String show6test(){
        return "Show number: 6.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "     Ticket Number        Buyer Phone Number                        Seat Numbers\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "              1234                      1234                      A2, B4, C4, D5\n" +
                "---------------------------------------------------------------------------------------------\n";
    }
}
