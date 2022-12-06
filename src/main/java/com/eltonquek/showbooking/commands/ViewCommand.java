package com.eltonquek.showbooking.commands;

import org.springframework.stereotype.Component;

@Component
public class ViewCommand implements Command {

    @Override
    public boolean validate(String[] inputs) {
//        View <Show Number>
        System.out.println("validation~");
        return false;
    }

    @Override
    public void run(String[] inputs) {
        System.out.println("run view~");
    }
}
