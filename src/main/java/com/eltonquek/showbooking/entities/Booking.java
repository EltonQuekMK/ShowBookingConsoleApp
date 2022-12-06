package com.eltonquek.showbooking.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class Booking {

    private int showNumber;
    private int ticketNumber;
    private String phoneNumber;
    private List<Seat> seatList;
    private LocalDateTime createdDateTime;

    public Booking(int showNumber, int ticketNumber, String phoneNumber, List<Seat> seatList) {
        this.showNumber = showNumber;
        this.ticketNumber = ticketNumber;
        this.phoneNumber = phoneNumber;
        this.seatList = seatList;
        this.createdDateTime = LocalDateTime.now();
    }
}
