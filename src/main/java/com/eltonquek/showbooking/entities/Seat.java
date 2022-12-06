package com.eltonquek.showbooking.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private boolean isBooked;

    private String seatNumber;

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
        this.isBooked = false;
    }
}
