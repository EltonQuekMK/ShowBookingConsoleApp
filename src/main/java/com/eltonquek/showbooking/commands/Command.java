package com.eltonquek.showbooking.commands;

import java.util.Objects;

public abstract class Command {

    abstract String key();

    abstract int numberOfInputs();

    public boolean validate(String[] inputs) {
        if (inputs.length != numberOfInputs() || !Objects.equals(inputs[0], key())) {
            return false;
        }
        return commandValidation(inputs);
    }

    abstract boolean commandValidation(String[] inputs);

    public abstract void run(String[] inputs);
}
