package com.eltonquek.showbooking;

import com.eltonquek.showbooking.commands.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(args = "test")
class ShowBookingApplicationTests {

    @Autowired
    private List<Command> commandList;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(commandList);
        Assertions.assertEquals(2, commandList.size());
    }
}
