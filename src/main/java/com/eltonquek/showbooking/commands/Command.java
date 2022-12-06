package com.eltonquek.showbooking.commands;

public interface Command {

    boolean validate(String[] inputs);

    void run(String[] inputs);
}
