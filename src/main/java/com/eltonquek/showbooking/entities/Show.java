package com.eltonquek.showbooking.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Show {

    private int showNumber;

    private int rows;

    private int columns;

    private int cancellationValidityInMinutes;

    private List<Seat> seatList;

    public Show(int showNumber, int rows, int columns, int cancellationValidityInMinutes) {
        this.showNumber = showNumber;
        this.rows = rows;
        this.columns = columns;
        this.cancellationValidityInMinutes = cancellationValidityInMinutes;
        this.seatList = generateSeatList(rows, columns);
    }

    private List<Seat> generateSeatList(int rows, int columns) {
        if (rows > 26 || rows < 1 || columns > 10 || columns < 1) {
            throw new RuntimeException();
        }
        List<Seat> seatList = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                String seatNumber = Character.toString('A' - 1 + i) + j;
                seatList.add(new Seat(seatNumber));
            }
        }
        return seatList;
    }
}
