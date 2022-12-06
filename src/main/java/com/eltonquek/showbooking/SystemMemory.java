package com.eltonquek.showbooking;

import com.eltonquek.showbooking.entities.Booking;
import com.eltonquek.showbooking.entities.Show;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
@Setter
public class SystemMemory {

    List<Show> showList = new ArrayList<>();

    List<Booking> bookingList = new ArrayList<>();

    int ticketNumberCounter = 0;
}
