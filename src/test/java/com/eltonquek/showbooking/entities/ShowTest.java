package com.eltonquek.showbooking.entities;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowTest {

    @Test
    void generateSeatList_returnSeatList_validInput_1_1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Show show = new Show(1, 1, 1, 1);
        assertEquals(1, show.getSeatList().size());
    }

    @Test
    void generateSeatList_returnSeatList_validInput_26_1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Show show = new Show(1, 26, 1, 1);
        assertEquals(26, show.getSeatList().size());
    }

    @Test
    void generateSeatList_returnSeatList_validInput_1_10() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Show show = new Show(1, 1, 10, 1);
        assertEquals(10, show.getSeatList().size());
    }

    @Test
    void generateSeatList_returnSeatList_validInput_26_10() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Show show = new Show(1, 26, 10, 1);
        assertEquals(260, show.getSeatList().size());
    }

    @Test
    void generateSeatList_returnSeatList_invalidInput_26_11() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThrows(RuntimeException.class, () -> new Show(1, 26, 11, 1));
    }

    @Test
    void generateSeatList_returnSeatList_invalidInput_27_10() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThrows(RuntimeException.class, () -> new Show(1, 27, 10, 1));
    }

    @Test
    void generateSeatList_returnSeatList_invalidInput_0_10() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThrows(RuntimeException.class, () -> new Show(1, 0, 10, 1));
    }

    @Test
    void generateSeatList_returnSeatList_invalidInput_10_0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThrows(RuntimeException.class, () -> new Show(1, 10, 0, 1));
    }
}
