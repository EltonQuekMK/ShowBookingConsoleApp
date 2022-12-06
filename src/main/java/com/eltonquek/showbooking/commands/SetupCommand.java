package com.eltonquek.showbooking.commands;

import org.springframework.stereotype.Component;

@Component
public class SetupCommand implements Command {

    @Override
    public boolean validate(String[] inputs) {
        System.out.println("validation~");
        return false;
    }

    @Override
    public void run(String[] inputs) {
        System.out.println("run setup~");
    }
}
