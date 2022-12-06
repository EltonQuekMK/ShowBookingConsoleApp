package com.eltonquek.showbooking.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class Booking {

    private String ticketNumber;
    private String phoneNumber;
    private List<Seat> seatList;
    private LocalDateTime createdDateTime;

    public Booking(String ticketNumber, String phoneNumber, List<Seat> seatList) {
        this.ticketNumber = ticketNumber;
        this.phoneNumber = phoneNumber;
        this.seatList = seatList;
        this.createdDateTime = LocalDateTime.now();
    }
}
